package edu.ap.projecty.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ap.projecty.model.Question
import edu.ap.projecty.model.UserAnswer

class SolveExamViewModel: ViewModel() {

    private val _userAnswers = MutableLiveData<MutableList<UserAnswer>>()
    val userAnswers: LiveData<MutableList<UserAnswer>> get() = _userAnswers

    private val _examState = MutableLiveData<ExamState>()
    val examState: LiveData<ExamState> get() = _examState

    init {
        _userAnswers.value = mutableListOf()
        _examState.value = ExamState.NOT_STARTED
    }

    fun addAnswer(question: String, answerText: String) {
        _userAnswers.value?.add(UserAnswer(question, answerText))
    }

    fun submitExam() {
        _userAnswers.value?.let { answers ->
            // Log each answer
            for (answer in answers) {
                Log.d("SolveExamViewModel", "Question: ${answer.question}, Answer: ${answer.answer}")
            }
        }

       //_examState.value = ExamState.SUBMITTED
       //CoroutineScope(Dispatchers.IO).launch {
       //    userAnswers.value?.let {
       //        answerRepository.submitAnswers(it)
       //    }
       //}
    }

    fun startExam() {
        _examState.value = ExamState.ONGOING
    }

    fun finishExam() {
        _examState.value = ExamState.FINISHED
        submitExam()
    }
}

enum class ExamState {
    NOT_STARTED,
    ONGOING,
    FINISHED,
    SUBMITTED
}