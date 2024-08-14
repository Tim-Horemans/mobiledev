package edu.ap.projecty

import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.Student

object FakeStudentDatabase {

    private val students = mutableListOf(
        Student("John"),
        Student("Karel"),
        Student("Willem"),
    )

    fun getAllStudents(): List<Student> {
        return students.toList()
    }

    // Method to add a new student
    fun addStudent(student: Student) {
        students.add(student)
    }

    // Method to remove a student by name
    fun removeStudent(name: String) {
        students.removeAll { it.name == name }
    }

    // Method to find a student by name
    fun findStudentByName(name: String): Student? {
        return students.find { it.name == name }
    }

    // Optional: Method to clear all students
    fun clearAllStudents() {
        students.clear()
    }
}