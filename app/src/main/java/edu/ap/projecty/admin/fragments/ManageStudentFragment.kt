package edu.ap.projecty.admin.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.R
import edu.ap.projecty.adapters.StudentListAdapter
import edu.ap.projecty.admin.DetailStudentActivity
import edu.ap.projecty.databinding.FragmentManageStudentBinding
import edu.ap.projecty.repository.StudentViewModel


class ManageStudentFragment : Fragment() {
    private var _binding: FragmentManageStudentBinding? = null
    private lateinit var studentsViewModel:StudentViewModel
    private lateinit var studentAdapter:StudentListAdapter
    private lateinit var addStudentContainer: FrameLayout

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManageStudentBinding.inflate(inflater, container, false)
        studentsViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        addStudentContainer = binding.addStudentContainer
        addStudentContainer.visibility = View.GONE

        binding.toggleButton.setOnClickListener {
            toggleFrameLayoutVisibility()
        }

        studentAdapter = StudentListAdapter(emptyList()) { student ->
            val intent = Intent(activity, DetailStudentActivity::class.java).apply {
                putExtra("STUDENT", student)
            }
            startActivity(intent)
        }
        binding.recycleViewStudents.adapter = studentAdapter
        binding.recycleViewStudents.layoutManager = LinearLayoutManager(context)

        childFragmentManager.beginTransaction()
            .replace(R.id.addStudentContainer, AddStudentFragment())
            .commit()

        studentsViewModel.getStudents().observe(viewLifecycleOwner) { students ->
            students?.let {
                studentAdapter.updateStudents(it)
            }
        }


        return binding.root
    }

    private fun toggleFrameLayoutVisibility() {
        if (addStudentContainer.visibility == View.VISIBLE) {
            addStudentContainer.visibility = View.GONE
        } else {
            addStudentContainer.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}