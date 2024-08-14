package edu.ap.projecty.admin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.ap.projecty.R
import edu.ap.projecty.databinding.FragmentClosedQuestionBinding


class ClosedQuestion : Fragment() {
    private var _binding: FragmentClosedQuestionBinding? = null
    private lateinit var answerClosedFragment: AnswerClosedQuestion

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClosedQuestionBinding.inflate(inflater, container, false)
        answerClosedFragment = childFragmentManager.findFragmentById(R.id.containerAnswers) as AnswerClosedQuestion

        binding.addAnswerButton.setOnClickListener {
            answerClosedFragment.addRadioButton()
        }
        return binding.root
    }

    fun getResult(): Pair<String, List<String>> {
        val question = binding.questionInput.text.toString()
        val answers = answerClosedFragment.getPossibleAnswers()
        return Pair(question, answers)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}