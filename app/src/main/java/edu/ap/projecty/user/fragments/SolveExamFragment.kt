package edu.ap.projecty.user.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.adapters.SolveExamAdapter
import edu.ap.projecty.databinding.FragmentSolveExamBinding
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.SolvedExam
import edu.ap.projecty.repository.SolveExamViewModel
import edu.ap.projecty.service.SolveExamService


class SolveExamFragment : Fragment() {

    private lateinit var _binding : FragmentSolveExamBinding
    private lateinit var solveExamAdapter : SolveExamAdapter
    private lateinit var solveExamViewModel: SolveExamViewModel
    private lateinit var examService: SolveExamService
    private lateinit var studentId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSolveExamBinding.inflate(inflater, container, false)
        examService = SolveExamService()

        val exam = arguments?.getSerializable("Exam") as Exam
        studentId = arguments?.getString("studentId") as String

        solveExamViewModel = ViewModelProvider(this).get(SolveExamViewModel::class.java)

        val openQuestions = exam.openQuestions
        val closedQuestions = exam.closedQuestion
        val allQuestions = openQuestions + closedQuestions
        solveExamAdapter = SolveExamAdapter(allQuestions, solveExamViewModel)
        setupRecyclerView()

        requireActivity().startService(Intent(context, examService::class.java))
        _binding.btnSubmit.setOnClickListener {
            val answers = solveExamAdapter.getUserAnswers()
            solveExamViewModel.submitExam(exam, studentId)
            examService.collectData( "locatie")
        }

        return _binding.root
    }

    private fun setupRecyclerView() {
        _binding.recyclerSolveExams.layoutManager = LinearLayoutManager(requireContext())
        _binding.recyclerSolveExams.adapter = solveExamAdapter
    }
}