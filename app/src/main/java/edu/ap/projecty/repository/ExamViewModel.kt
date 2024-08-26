package edu.ap.projecty.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.Student

class ExamViewModel : ViewModel() {
    private val exams: MutableLiveData<List<Exam>> = MutableLiveData()
    fun getExams(): LiveData<List<Exam>> = exams

     init {
        loadExams()
    }

    private fun loadExams() {
        val reference = FirebaseDatabaseManager.getExamCollectionReference()

        reference.addSnapshotListener { snapshot, error ->
            if (snapshot != null && !snapshot.isEmpty) {
                val examList = mutableListOf<Exam>()
                for (document in snapshot.documents) {
                    val exam = document.toObject(Exam::class.java)
                    if (exam != null) {
                        exam.key = document.id // Assign the document ID to the exam's key
                    }
                    exam?.let { examList.add(it) }
                }
                exams.value = examList.toList() // Assigning the list to the MutableLiveData
            } else {
                Log.d("Firestore", "No data found")
            }
        }
    }

    fun loadExamWithId(examId: String): LiveData<Exam> {
        val liveData = MutableLiveData<Exam>()

        val examDocumentReference = FirebaseDatabaseManager.getExamCollectionReference().document(examId)
        examDocumentReference.addSnapshotListener { snapshot, error ->
            if (snapshot != null && snapshot.exists()) {
                val exam = snapshot.toObject(Exam::class.java)
                liveData.postValue(exam)
            } else {
                liveData.postValue(null)
            }
        }

        return liveData
    }

    fun addExam(exam: Exam){
        val reference = FirebaseDatabaseManager.getExamCollectionReference()
        reference.add(exam)
            .addOnSuccessListener {
                Log.i("ExamViewModel", "Exam added successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("ExamViewModel", "Failed to add exam: ${exception.message}")
            }
    }
}