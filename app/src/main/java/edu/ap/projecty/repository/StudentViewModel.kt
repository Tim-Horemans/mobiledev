package edu.ap.projecty.repository;

import android.util.Log
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener



import edu.ap.projecty.model.Student;

class StudentViewModel : ViewModel() {
    private val students: MutableLiveData<List<Student>> = MutableLiveData()
    fun getStudents(): LiveData<List<Student>> = students

    init {
        loadStudents()
    }

    private fun loadStudents() {
        val studentId = "exampleExamId" // Replace this with the actual examId
        val reference = FirebaseDatabaseManager.getExamReference(studentId)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val studentList = mutableListOf<Student>()
                for (studentSnapshot in snapshot.children) {
                    val student = studentSnapshot.getValue(Student::class.java)
                    student?.let { studentList.add(it) }
                }
                students.value = studentList.toList()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun addStudent(student: Student) {
        val reference = FirebaseDatabaseManager.getExamReference("studenten")
        val studentId = reference.push().key ?: return

        reference.child(studentId).setValue(student)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                Log.i("niet gleukt,", exception.toString())
            }
    }
}
