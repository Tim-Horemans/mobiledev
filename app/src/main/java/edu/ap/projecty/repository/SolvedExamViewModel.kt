package edu.ap.projecty.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ap.projecty.model.SolvedExam
data class StudentDetails(
    val name: String,
    val points: Int,
    val totalQuestion: Int,
    val duration: Long
)
class SolvedExamViewModel : ViewModel() {
    private val _groupedSolvedExams = MutableLiveData<Map<String, List<StudentDetails>>>()
    val groupedSolvedExams: LiveData<Map<String, List<StudentDetails>>> get() = _groupedSolvedExams
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

    fun getAllSolvedExams(): LiveData<List<SolvedExam>> {
        val solvedExamsLiveData = MutableLiveData<List<SolvedExam>>()
        val reference = FirebaseDatabaseManager.getSolvedExamCollectionReference()

        reference
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
                Log.e("getAllSolvedExams", "Error retrieving all solved exams", e)
                solvedExamsLiveData.value = emptyList()
            }

        return solvedExamsLiveData
    }

    fun loadGroupedSolvedExams() {
        val solvedExamsRef = FirebaseDatabaseManager.getSolvedExamCollectionReference()

        solvedExamsRef.get()
            .addOnSuccessListener { solvedExamsSnapshot ->
                val solvedExamsList = solvedExamsSnapshot.documents.mapNotNull { doc ->
                    val solvedExam = doc.toObject(SolvedExam::class.java)
                    solvedExam?.let {
                        it.examId = doc.id
                        it
                    }
                }

                val groupedSolvedExams = solvedExamsList.groupBy { it.name }
                    .mapValues { entry ->
                        entry.value.map { solvedExam ->
                            StudentDetails(
                                name = solvedExam.studentName,
                                points = solvedExam.totalPoints,
                                totalQuestion = solvedExam.totalQuestions,
                                duration = solvedExam.elapsedTime
                            )
                        }
                    }

                _groupedSolvedExams.value = groupedSolvedExams
            }
            .addOnFailureListener { e ->
                Log.e("ExamViewModel", "Failed to fetch solved exams: ${e.message}")
            }
    }
}