package edu.ap.projecty.admin.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import edu.ap.projecty.R
import edu.ap.projecty.databinding.FragmentClosedQuestionBinding


class ClosedQuestion : Fragment() {
    private var _binding: FragmentClosedQuestionBinding? = null
    private lateinit var answerClosedFragment: AnswerClosedQuestion
    private var correctAnswer : String = ""
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

    fun getResult(): Triple<String, List<String>, String> {
        val question = binding.questionInput.text.toString()
        val answers = answerClosedFragment.getPossibleAnswers()
        val correctAnswer = answerClosedFragment.getSelectedRadioButtonText() ?: "geen antwoord"

        return Triple(question, answers, correctAnswer)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}