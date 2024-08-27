package edu.ap.projecty.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ap.projecty.model.SolvedExam

class SolvedExamViewModel : ViewModel() {
    fun getSolvedExamsByStudentId(studentId: String): LiveData<List<SolvedExam>> {
        val solvedExamsLiveData = MutableLiveData<List<SolvedExam>>()
        val refrence = FirebaseDatabaseManager.getSolvedExamCollectionReference()

        refrence.whereEqualTo("studentId", studentId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val solvedExams = querySnapshot.documents.mapNotNull { it.toObject(SolvedExam::class.java) }
                    solvedExamsLiveData.value = solvedExams
                } else {
                    solvedExamsLiveData.value = emptyList()
                }
            }
            .addOnFailureListener { e ->
                Log.e("getSolvedExamsByStudentId", "Error retrieving solved exams for student ID: $studentId", e)
                solvedExamsLiveData.value = emptyList()
            }

        return solvedExamsLiveData
    }
}