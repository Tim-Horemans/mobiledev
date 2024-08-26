package edu.ap.projecty.admin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.ap.projecty.databinding.ActivityAddExamBinding
import edu.ap.projecty.databinding.FragmentAddStudentBinding
import edu.ap.projecty.model.Student
import edu.ap.projecty.repository.StudentViewModel

class AddStudentFragment : Fragment() {
    private lateinit var binding: FragmentAddStudentBinding
    private lateinit var viewModelStudent: StudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        viewModelStudent = ViewModelProvider(this)[StudentViewModel::class.java]

        binding.submit.setOnClickListener {
            addStudent()
        }

        return binding.root
    }

    private fun addStudent(){
        val name = binding.editTextText.text
        if (name.isNotEmpty()) {
            viewModelStudent.addStudent(Student(name = name.toString()))
        }
    }
}