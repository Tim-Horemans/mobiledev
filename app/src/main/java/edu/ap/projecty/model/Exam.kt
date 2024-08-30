package edu.ap.projecty.model

import java.io.Serializable

data class Exam(
    var key : String = "",
    var name: String = "",
    var students: MutableList<Student> = mutableListOf(),
    var openQuestions: MutableList<Question> = mutableListOf(),
    var closedQuestion: MutableList<MultipleChoiceQuestion> = mutableListOf()
) : Serializable {
    override fun toString(): String {
        val studentCount = students.size
        val questionCount = openQuestions.size
        return "Exam(name='$name', studentsCount=$studentCount, questionsCount=$questionCount)"
    }
}