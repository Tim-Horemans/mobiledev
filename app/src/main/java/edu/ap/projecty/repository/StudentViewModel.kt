package edu.ap.projecty.repository;


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        val reference = FirebaseDatabaseManager.getStudentsReference()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val studentList = mutableListOf<Student>()
                for (studentSnapshot in snapshot.children) {
                    val student = studentSnapshot.getValue(Student::class.java)
                    if (student != null) {
                        student.key = studentSnapshot.key.toString()
                    }
                    student?.let { studentList.add(it) }
                }
                students.value = studentList.toList()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    public fun getStudentByExam(givenExamId: String): LiveData<List<Student>> {
        val liveData = MutableLiveData<List<Student>>()
        val database = FirebaseDatabaseManager.getStudentsReference()


        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val studentsForGivenExam = mutableListOf<Student>()

                for (studentSnapshot in dataSnapshot.children) {
                    val exams = studentSnapshot.child("exams").children

                    for (examSnapshot in exams) {
                        val examId = examSnapshot.getValue(String::class.java)
                        if (examId == givenExamId) {
                            val student = studentSnapshot.getValue(Student::class.java)
                            if (student != null) {
                                studentsForGivenExam.add(student)
                            }
                            break
                        }
                    }
                }
                liveData.postValue(studentsForGivenExam)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException())
                liveData.postValue(emptyList())
            }
        })

        return liveData
    }

    public fun getExamsFromStudent(examsId: List<String>): LiveData<List<Exam>> {
        val examReference = FirebaseDatabaseManager.getExamReference()
        val examsList = mutableListOf<Exam>()

        for (examId in examsId) {
            val examRef = examReference.child(examId)
            examRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val exam = snapshot.getValue(Exam::class.java)
                    if (exam != null) {
                        if (exam !in examsList) {
                            examsList.add(exam)
                        }
                    }
                    examsLiveData.value = examsList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("loadExamsFromStudent", "Error with examId: $examId")
                }
            })
        }

        return examsLiveData
    }


    fun addStudent(student: Student) {
        val reference = FirebaseDatabaseManager.getStudentsReference()
        val studentId = reference.push().key ?: return

        reference.child(studentId).setValue(student)
    }

    fun addExamToStudent(examId: String, studentId: String){
        val studentReference = FirebaseDatabaseManager.getStudent(studentId)
        val examsReference = studentReference.child("exams")

        examsReference.push().setValue(examId)
    }
}
