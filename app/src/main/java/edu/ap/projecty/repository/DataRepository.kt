package edu.ap.projecty.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


object FirebaseDatabaseManager {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://exam-531d9-default-rtdb.europe-west1.firebasedatabase.app/")

    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    fun getExamReference() = database.getReference("exams/")

    fun getSolvedExamReference() = database.getReference("solved_exams/")

    fun getStudentsReference() = database.getReference("students/")

    fun getStudent(studentId: String) = database.getReference("students/$studentId")

    fun getStudentCollectionReference() = firestore.collection("student")

    fun getExamCollectionReference() = firestore.collection("exam")
}