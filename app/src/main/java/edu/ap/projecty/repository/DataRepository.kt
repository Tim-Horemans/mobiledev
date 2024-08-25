package edu.ap.projecty.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


object FirebaseDatabaseManager {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://exam-531d9-default-rtdb.europe-west1.firebasedatabase.app/")

    fun getExamReference() = database.getReference("exams/")


    fun getStudentsReference() = database.getReference("students/")

    fun getStudent(studentId: String) = database.getReference("students/$studentId")


}