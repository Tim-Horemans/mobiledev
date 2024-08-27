package edu.ap.projecty.model

data class SolvedExam(
    var examId: String = "",
    var name: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val totalPoints: Int = 0,
    val totalQuestions: Int = 0,
    val latitude: String = "",
    val longtitude: String = "",
    val elapsedTime: Long = 0L,
    //val questions: Map<String, String>,
    //val userAnswers: Map<String, String>,

)