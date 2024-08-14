package edu.ap.projecty.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.ap.projecty.FakeStudentDatabase
import edu.ap.projecty.R
import edu.ap.projecty.databinding.ActivityAddStudentBinding
import edu.ap.projecty.databinding.OverviewExamsLayoutBinding
import edu.ap.projecty.model.Student

class AddStudent : AppCompatActivity(){
    private lateinit var binding: ActivityAddStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button3.setOnClickListener {
            addStudent()
        }
    }

    private fun addStudent(){
        val name = binding.editTextText2.toString()
        if (name.isNotEmpty()) {
            val student = Student(name )
            FakeStudentDatabase.addStudent(student)
            Toast.makeText(this, "Student added: $student", Toast.LENGTH_SHORT).show()
        }
    }
}