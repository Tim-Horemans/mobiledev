package edu.ap.projecty.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.ap.projecty.FakeExamDatabase
import edu.ap.projecty.databinding.ActivityAddExamBinding
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.MultipleChoiceQuestion
import edu.ap.projecty.model.Question
import edu.ap.projecty.repository.ExamViewModel
import edu.ap.projecty.repository.StudentViewModel

class AddExam : AppCompatActivity() {

    private lateinit var binding: ActivityAddExamBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: ExamViewModel
    private lateinit var exam: Exam
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ExamViewModel::class.java)
        exam = Exam()
        initializeResultLauncher()

        binding.button3.setOnClickListener {
            handleAddExam()
        }

        binding.button4.setOnClickListener {
            openAddQuestionToExamActivity()
        }
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
        val intent = Intent(this, AddQuestionToExam::class.java)
        resultLauncher.launch(intent)
    }

    private fun handleAddExam() {
        val examName = binding.editTextText2.text.toString()
        if (examName.isNotBlank()) {
            exam.name = examName
            viewModel.addExam(exam)
        } else {
            showToast("Exam name cannot be empty.")
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}