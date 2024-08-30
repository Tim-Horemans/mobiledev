package edu.ap.projecty.repository;


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.Student


class StudentViewModel : ViewModel() {
    private val students: MutableLiveData<List<Student>> = MutableLiveData()
    private val examsLiveData = MutableLiveData<List<Exam>>()

    fun getStudents(): LiveData<List<Student>> = students

    init {
        loadStudents()
    }

    private fun loadStudents() {
        val reference = FirebaseDatabaseManager.getStudentCollectionReference()

        reference.addSnapshotListener { snapshot, error ->
            if (snapshot != null && !snapshot.isEmpty) {
                val studentList = mutableListOf<Student>()
                for (document in snapshot.documents) {
                    val student = document.toObject(Student::class.java)
                    if (student != null) {
                        student.key = document.id
                    }
                    student?.let { studentList.add(it) }
                }
                students.value = studentList.toList()
            } else {
                Log.d("Firestore", "No data found")
            }
        }
    }

    public fun getStudentByExam(givenExamId: String): LiveData<List<Student>> {
        val liveData = MutableLiveData<List<Student>>()

        val studentsCollection = FirebaseDatabaseManager.getStudentCollectionReference()
        val query = studentsCollection.whereArrayContains("exams", givenExamId)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                val studentsForGivenExam = mutableListOf<Student>()
                for (document in querySnapshot.documents) {
                    val student = document.toObject(Student::class.java)
                    if (student != null) {
                        student.key = document.id
                        studentsForGivenExam.add(student)
                    }
                }
                liveData.postValue(studentsForGivenExam)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching data", exception)
                liveData.postValue(emptyList())
            }

        return liveData
    }

    fun getExamsFromStudent(examsId: List<String>): LiveData<List<Exam>> {
        val examsLiveData = MutableLiveData<List<Exam>>()
        val examsList = mutableListOf<Exam>()

        val examCollection = FirebaseDatabaseManager.getExamCollectionReference()
        for (examId in examsId) {
            val examRef = examCollection.document(examId)
            examRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val exam = documentSnapshot.toObject(Exam::class.java)
                        if (exam != null && exam !in examsList) {
                            examsList.add(exam)
                            examsLiveData.value = examsList
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("getExamsFromStudent", "Error retrieving exam with ID: $examId", e)
                }
        }
        return examsLiveData
    }


    fun addStudent(student: Student) {
        val reference = FirebaseDatabaseManager.getStudentCollectionReference()
        reference.add(student)
    }

    fun addExamToStudent(examId: String, studentId: String){
        val studentReference = FirebaseDatabaseManager.getStudentCollectionReference().document(studentId)

        studentReference.update("exams", FieldValue.arrayUnion(examId))
            .addOnSuccessListener {
                Log.d("Firestore", "Exam ID added successfully to the student's exam list.")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding exam ID", e)
            }
    }


}
