package edu.ap.projecty.admin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import edu.ap.projecty.R


class AnswerClosedQuestion : Fragment() {
    private lateinit var radioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_answer_closed, container, false)
        radioGroup = view.findViewById(R.id.radioGroup)

        return view
    }

    fun addRadioButton() {
        if (radioGroup.childCount >= 5){
            Toast.makeText(requireContext(), "Limiet 5 vragen", Toast.LENGTH_SHORT).show()
            return;
        }
        val customRadioButtonView = layoutInflater.inflate(R.layout.answerradio, radioGroup, false)
        val radioButton = RadioButton(requireContext())
        radioButton.id = View.generateViewId()
        val editText = customRadioButtonView.findViewById<EditText>(R.id.editTextOption)
        editText.setText("antwoord")
        radioGroup.addView(customRadioButtonView)
    }

    fun getPossibleAnswers(): List<String> {
        val possibleAnswers = mutableListOf<String>()
        for (i in 0 until radioGroup.childCount) {
            val view = radioGroup.getChildAt(i)
            if (view is ViewGroup) {
                val editText = view.findViewById<EditText>(R.id.editTextOption)
                val answerText = editText.text.toString()

                possibleAnswers.add(answerText)
            }
        }
        return possibleAnswers
    }

    fun getSelectedRadioButtonText(): String? {
        for (i in 0 until radioGroup.childCount) {
            val view = radioGroup.getChildAt(i)
            if (view is ViewGroup) {
                val radioButton = view.findViewById<RadioButton>(R.id.radioButtonOption)
                if (radioButton != null && radioButton.isChecked) {
                    val editText = view.findViewById<EditText>(R.id.editTextOption)
                    return editText?.text?.toString()
                }
            } else if (view is RadioButton) {
                if (view.isChecked) {
                    return view.text.toString()
                }
            }
        }
        return null
    }
}