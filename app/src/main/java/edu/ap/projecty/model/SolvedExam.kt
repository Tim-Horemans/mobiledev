package edu.ap.projecty.model

import java.util.Dictionary
data class SolvedExam(
    val examId: String = "",
    val name: String = "",
    val studentId: String = "",
    //val questions: Map<String, String>,
    //val userAnswers: Map<String, String>,
    val totalPoints: Int = 0
)