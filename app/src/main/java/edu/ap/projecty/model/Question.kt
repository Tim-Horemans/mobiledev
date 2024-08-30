package edu.ap.projecty.model

import java.io.Serializable

open class Question(open val question: String = "", open val correctAnswer: String = ""):Serializable

data class MultipleChoiceQuestion(
    val answers: List<String> = emptyList(),
    override val question: String = "",
    override val correctAnswer: String = ""
) : Question(question), Serializable