package edu.ap.projecty.user

import ExamListAdapter
import android.content.Intent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.FakeExamDatabase
import edu.ap.projecty.databinding.OverviewExamsLayoutBinding

class OverviewExams : AppCompatActivity() {

    private lateinit var binding: OverviewExamsLayoutBinding
    private lateinit var examAdapter: ExamListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OverviewExamsLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
        val examList = FakeExamDatabase.getAllExams()

        examAdapter = ExamListAdapter(examList) { exam ->
            val intent = Intent(this, OverviewStudent::class.java)
            intent.putExtra("EXAM", exam) // Pass the whole Exam object
            startActivity(intent)
        }

        binding.recyclerView2.adapter = examAdapter
    }
}