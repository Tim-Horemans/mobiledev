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
        val reference = FirebaseDatabaseManager.getExamReference()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val examList = mutableListOf<Exam>()
                for (examSnapshot in snapshot.children) {
                    val exam = examSnapshot.getValue(Exam::class.java)
                    if (exam != null) {
                        exam.key = examSnapshot.key.toString()
                    }
                    exam?.let { examList.add(it) }
                }
                exams.value = examList.toList()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ExamViewModel", "Error loading exams: ${error.message}")
            }
        })
    }

    fun loadExamWithId(examId: String): LiveData<Exam> {
        val liveData = MutableLiveData<Exam>()
        val reference = FirebaseDatabaseManager.getExamReference().child(examId)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val exam = snapshot.getValue(Exam::class.java)
                if (exam != null) {
                    liveData.value = exam
                } else {
                    liveData.value = null
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ExamViewModel", "Error loading exam: ${error.message}")
            }
        })

        return liveData
    }
    fun addExam(exam: Exam) {
        val reference = FirebaseDatabaseManager.getExamReference()
        val examId = reference.push().key ?: return

        reference.child(examId).setValue(exam)
            .addOnSuccessListener {
                Log.i("ExamViewModel", "Exam added successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("ExamViewModel", "Failed to add exam: ${exception.message}")
            }
    }


}