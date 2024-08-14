package edu.ap.projecty.user.fragments

import ExamListAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.FakeExamDatabase
import edu.ap.projecty.R
import edu.ap.projecty.databinding.FragmentOverviewExamsBinding


class OverviewExamsFragment : Fragment() {

    private lateinit var examAdapter: ExamListAdapter
    private var _binding: FragmentOverviewExamsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewExamsBinding.inflate(inflater, container, false)
        val view = binding.root

        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        val examList = FakeExamDatabase.getAllExams()

        examAdapter = ExamListAdapter(examList) { exam ->
            // Handle click event here, e.g., navigate to another fragment or activity
            // Example: Navigate to OverviewStudentFragment or startActivity
            // val intent = Intent(requireContext(), OverviewStudent::class.java)
            // intent.putExtra("EXAM", exam)
            // startActivity(intent)
        }

        binding.recyclerView2.adapter = examAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}