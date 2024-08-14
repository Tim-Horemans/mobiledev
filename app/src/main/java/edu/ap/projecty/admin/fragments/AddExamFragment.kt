package edu.ap.projecty.admin.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import edu.ap.projecty.R
import edu.ap.projecty.admin.AddQuestionToExam
import edu.ap.projecty.databinding.ActivityAddExamBinding
import edu.ap.projecty.databinding.FragmentAddExamBinding
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.MultipleChoiceQuestion
import edu.ap.projecty.model.Question
import edu.ap.projecty.repository.ExamViewModel


class AddExamFragment : Fragment() {

    private var _binding: ActivityAddExamBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: ExamViewModel
    private lateinit var exam: Exam

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityAddExamBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(ExamViewModel::class.java)
        exam = Exam()
        initializeResultLauncher()

        binding.button3.setOnClickListener {
            handleAddExam()
        }

        binding.button4.setOnClickListener {
            openAddQuestionToExamActivity()
        }

        return view
    }

    private fun initializeResultLauncher() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val questionResult = data?.getSerializableExtra("Result") as? Question
                handleQuestionResult(questionResult)
            }
        }
    }

    private fun openAddQuestionToExamActivity() {
        val intent = Intent(requireContext(), AddQuestionToExam::class.java)
        resultLauncher.launch(intent)
    }

    private fun handleAddExam() {
        val examName = binding.editTextText2.text.toString()
        if (examName.isNotBlank()) {
            exam.name = examName
            viewModel.addExam(exam)
        }
    }

    private fun handleQuestionResult(question: Question?) {
        when (question) {
            is MultipleChoiceQuestion -> {
                Log.i("AddExam", "Received MultipleChoiceQuestion: ${question.question}")
                Log.i("AddExam", "Possible answers: ${question.answers.joinToString()}")
                exam.question.add(question)
            }
            is Question -> {
                Log.i("AddExam", "Received SimpleQuestion: ${question.question}")
                exam.question.add(question)
            }
            null -> {
                Log.i("AddExam", "No valid question result returned")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}