package edu.ap.projecty.model

data class SolveExam(
    val idExam: String = "",
    val answers: List<AnswerToQuestion> = emptyList()
)

class AnswerToQuestion(
    val idQuestion: String = "",
    val answer: String = ""
)