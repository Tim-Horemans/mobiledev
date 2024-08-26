package edu.ap.projecty.model

import java.util.Dictionary

data class SolvedExam(val idExam: String,
                      val idStudent: String,
    val questions: Map<String, String>) {
}