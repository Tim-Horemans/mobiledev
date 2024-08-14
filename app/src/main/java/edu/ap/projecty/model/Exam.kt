package edu.ap.projecty.model

import java.io.Serializable

data class Exam(
    var name: String = "",
    var students: MutableList<Student> = mutableListOf(),
    var question: MutableList<Question> = mutableListOf()
) : Serializable {
    override fun toString(): String {
        val studentCount = students.size
        val questionCount = question.size
        return "Exam(name='$name', studentsCount=$studentCount, questionsCount=$questionCount)"
    }
}