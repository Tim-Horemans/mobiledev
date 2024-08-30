package edu.ap.projecty.user.fragments

import edu.ap.projecty.adapters.ExamListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.R
import edu.ap.projecty.databinding.FragmentOverviewExamsBinding
import edu.ap.projecty.model.Student
import edu.ap.projecty.repository.ExamViewModel


class OverviewExamsFragment : Fragment(){

    private lateinit var examAdapter: ExamListAdapter
    private lateinit var examViewModel: ExamViewModel
    private var _binding: FragmentOverviewExamsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewExamsBinding.inflate(inflater, container, false)
        val view = binding.root
        examViewModel = ViewModelProvider(this).get(ExamViewModel::class.java)

        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        examAdapter = ExamListAdapter(emptyList()) { exam ->
            val fragment = SelectStudentFragment()
            fragment.arguments = Bundle().apply {
                putString("EXAM_ID", exam.key)
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout3, fragment)
                .addToBackStack(null)
                .commit()
           //val bundle = Bundle().apply {
           //    putSerializable("Exam", exam)
           //}
           //val fragment = SolveExamFragment().apply {
           //    arguments = bundle
           //}
           //requireActivity().supportFragmentManager.beginTransaction()
           //    .replace(R.id.frameLayout3, fragment)
           //    .addToBackStack(null)
           //    .commit()
        }

        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.adapter = examAdapter
        examViewModel.getExams().observe(viewLifecycleOwner) { examList ->
            examAdapter.updateExams(examList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}