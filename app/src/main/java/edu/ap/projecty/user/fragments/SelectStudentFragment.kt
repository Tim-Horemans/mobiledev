package edu.ap.projecty.user.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ap.projecty.R
import edu.ap.projecty.adapters.SelectUserAdapter
import edu.ap.projecty.model.Student
import edu.ap.projecty.repository.ExamViewModel
import edu.ap.projecty.repository.StudentViewModel

class SelectStudentFragment : Fragment() {

    private lateinit var selectUserAdapter: SelectUserAdapter
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var examViewModel: ExamViewModel
    private var studentList: List<Student> = listOf()
    private var examId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_student, container, false)
        studentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
        examViewModel = ViewModelProvider(this).get(ExamViewModel::class.java)

        arguments?.let {
            examId = it.getString("EXAM_ID", "")
        }

        studentViewModel.getStudentByExam(examId).observe(viewLifecycleOwner, Observer { students ->
            studentList = students
            selectUserAdapter.updateStudents(studentList)
        })

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        selectUserAdapter = SelectUserAdapter(studentList) { selectedStudent ->
            navigateToExam(selectedStudent.key)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = selectUserAdapter

        selectUserAdapter.updateStudents(studentList)

        return view
    }

    private fun navigateToExam(studentId: String) {
        examViewModel.loadExamWithId(examId).observe(viewLifecycleOwner, Observer { exam ->
            if (exam != null) {
                val bundle = Bundle().apply {
                    putSerializable("Exam", exam)
                    putString("studentId", studentId)
                }

                val fragment = SolveExamFragment().apply {
                    arguments = bundle
                }

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout3, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "No exam for student", Toast.LENGTH_SHORT).show()
            }
        })
    }
}