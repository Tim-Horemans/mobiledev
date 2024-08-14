package edu.ap.projecty

import edu.ap.projecty.model.Exam

object FakeExamDatabase {

    private val exams = mutableListOf(
        Exam("Math Exam", FakeStudentDatabase.getAllStudents().toMutableList()),
        Exam("Science Exam"),
        Exam("History Exam"),
        Exam("Physics Exam"),
        Exam("Chemistry Exam"),
        Exam("English Literature Exam"),
    )

    fun getAllExams(): List<Exam> {
        return exams
    }

    fun addExam(exam: Exam) {
        exams.add(exam)
    }

    fun getExamByTitle(title: String): Exam? {
        return exams.find { it.name == title }
    }

    fun removeExam(exam: Exam) {
        exams.remove(exam)
    }
}