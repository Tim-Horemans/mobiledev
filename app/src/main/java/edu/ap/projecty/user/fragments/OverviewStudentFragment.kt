package edu.ap.projecty.user.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.adapters.StudentListAdapter
import edu.ap.projecty.databinding.FragmentOverviewStudentBinding
import edu.ap.projecty.repository.StudentViewModel


class OverviewStudentFragment : Fragment() {
    private lateinit var studentAdapter: StudentListAdapter
    private var _binding: FragmentOverviewStudentBinding? = null
    private lateinit var viewmodelStudent: StudentViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewStudentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewmodelStudent = ViewModelProvider(this).get(StudentViewModel::class.java)

        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        studentAdapter = StudentListAdapter(emptyList()) { exam ->
        }

        binding.recyclerView.adapter = studentAdapter

        viewmodelStudent.getStudents().observe(viewLifecycleOwner) { studentList ->
            studentAdapter.updateStudents(studentList)
        }
    }
}