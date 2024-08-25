package edu.ap.projecty.model

import java.io.Serializable

data class Student(
    var key : String = "",
    val name: String = "",
    val exams: Map<String, String> = mapOf()
) : Serializable