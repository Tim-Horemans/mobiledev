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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.DocumentSnapshot
class ExamViewModel : ViewModel() {
    private val exams: MutableLiveData<List<Exam>> = MutableLiveData()
    val groupedExams: MutableLiveData<Map<Exam, List<Student>>> = MutableLiveData()

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
                        exam.key = document.id
                    }
                    exam?.let { examList.add(it) }
                }
                exams.value = examList.toList()
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
                if (exam != null) {
                    exam.key = examDocumentReference.id
                }
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

     fun loadGroupedExams() {

        val examReference = FirebaseDatabaseManager.getExamCollectionReference()
        val studentReference = FirebaseDatabaseManager.getStudentCollectionReference()

        // Fetch exams
        examReference.get()
            .addOnSuccessListener { examSnapshot ->
                val examsList = mutableListOf<Exam>()
                for (document in examSnapshot.documents) {
                    val exam = document.toObject(Exam::class.java)
                    exam?.let {
                        it.key = document.id // Use document.id as the ID for exams
                        examsList.add(it)
                    }
                }

                // Fetch students
                studentReference.get()
                    .addOnSuccessListener { studentSnapshot ->
                        val studentMap = mutableMapOf<String, MutableList<Student>>()
                        for (doc in studentSnapshot.documents) {
                            val student = doc.toObject(Student::class.java)
                            student?.let {
                                for (examId in it.exams) {
                                    studentMap.computeIfAbsent(examId) { mutableListOf() }.add(it)
                                }
                            }
                        }

                        // Map exams to their students
                        val groupedExamsMap = examsList.associateWith { exam ->
                            studentMap[exam.key] ?: emptyList()
                        }

                        groupedExams.value = groupedExamsMap
                    }
                    .addOnFailureListener { e ->
                        Log.e("ExamViewModel", "Failed to fetch students: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                Log.e("ExamViewModel", "Failed to fetch exams: ${e.message}")
            }
    }
}