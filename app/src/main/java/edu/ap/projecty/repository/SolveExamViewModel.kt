package edu.ap.projecty.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ap.projecty.model.Question
import edu.ap.projecty.model.SolvedExam
import edu.ap.projecty.model.UserAnswer
import java.util.Dictionary

class SolveExamViewModel: ViewModel() {

    private val _userAnswers = MutableLiveData<MutableMap<String, String>>()
    val userAnswers: LiveData<MutableMap<String, String>> get() = _userAnswers

    init {
        _userAnswers.value = mutableMapOf()
    }

    fun addAnswer(question: String, answerText: String) {
        _userAnswers.value?.let { answers ->
            answers[question] = answerText
            _userAnswers.value = answers
        }
    }

    fun submitExam(solvedExam: SolvedExam) {
        _userAnswers.value?.let { answers ->
            for (answer in answers) {
                Log.d("SolveExamViewModel", "Question: ${answer.key}, Answer: ${answer.value}")
            }
        }


        val reference = FirebaseDatabaseManager.getSolvedExamReference()
        reference.push().setValue(solvedExam)
    }


}
