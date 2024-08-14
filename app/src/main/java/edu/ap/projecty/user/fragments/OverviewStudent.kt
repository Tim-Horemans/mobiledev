package edu.ap.projecty.user.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.adapters.StudentListAdapter
import edu.ap.projecty.databinding.OverviewStudentsLayoutBinding
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.Student
import edu.ap.projecty.repository.StudentViewModel

class OverviewStudent : AppCompatActivity() {
    private lateinit var binding: OverviewStudentsLayoutBinding
    private lateinit var studentAdapter: StudentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OverviewStudentsLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val exam = intent.getSerializableExtra("EXAM") as? Exam
        val students = exam?.students ?: emptyList()


        val viewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
        viewModel.addStudent(Student("tom"))
        setupRecyclerView(students)
    }

    private fun setupRecyclerView(students: List<Student>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        studentAdapter = StudentListAdapter(students) { student ->

        }

        binding.recyclerView.adapter = studentAdapter
    }
}