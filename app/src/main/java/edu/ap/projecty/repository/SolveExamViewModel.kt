package edu.ap.projecty.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.Question
import edu.ap.projecty.model.SolvedExam
import edu.ap.projecty.model.UserAnswer
import java.util.Dictionary

class SolveExamViewModel: ViewModel() {

    private val _userAnswers = MutableLiveData<MutableMap<String, String>>()

    init {
        _userAnswers.value = mutableMapOf()
    }

    fun addAnswer(question: String, answerText: String) {
        _userAnswers.value?.let { answers ->
            answers[question] = answerText
            _userAnswers.value = answers
        }
    }

    fun submitExam(exam: Exam, studentId: String) {
        _userAnswers.value?.let { answers ->
            for ((question, answer) in answers) {
                Log.d("SolveExamViewModel", "Question: $question, Answer: $answer")
            }
        }

        val closedQuestions = exam.closedQuestion
        val openQuestions = exam.openQuestions

        val closedPoints = _userAnswers.value?.let { compareQuestionWithAnswers(closedQuestions, it) } ?: 0
        val openPoints = _userAnswers.value?.let { compareQuestionWithAnswers(openQuestions, it) } ?: 0
        val totalPoints = closedPoints + openPoints

        Log.d("SolveExamViewModel", "Total points: $totalPoints")

        postBundeldExam(SolvedExam(exam.key, studentId, exam.name,totalPoints))
    }

    private fun postBundeldExam(bundeldExam: SolvedExam){
        val reference = FirebaseDatabaseManager.getSolvedExamCollectionReference()
        reference.add(bundeldExam)
    }
    private fun compareQuestionWithAnswers(question: List<Question>, answer: Map<String, String>): Int {
        var points = question.size

        for (quest in question) {
            val questionText = quest.question
            val questionCorrectAnswer = quest.correctAnswer

            val answer = answer[questionText]
            if (answer != questionCorrectAnswer)
                points--
        }

        return points
    }
    fun loadExamWithId(examId: String): LiveData<Exam> {
        val liveData = MutableLiveData<Exam>()

        val examDocumentReference = FirebaseDatabaseManager.getExamCollectionReference().document(examId)
        examDocumentReference.addSnapshotListener { snapshot, error ->
            if (snapshot != null && snapshot.exists()) {
                val exam = snapshot.toObject(Exam::class.java)
                if (exam != null) {
                    exam.key = examDocumentReference.id
                }
                liveData.postValue(exam)
            } else {
                liveData.postValue(null)
            }
        }

        return liveData
    }
}
