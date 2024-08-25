package edu.ap.projecty.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import edu.ap.projecty.R
import edu.ap.projecty.model.MultipleChoiceQuestion
import edu.ap.projecty.model.Question
import edu.ap.projecty.repository.SolveExamViewModel
import kotlinx.coroutines.delay

class SolveExamAdapter(
    private var data: List<Question>,
    private var solveExamViewModel: SolveExamViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val userAnswers = mutableMapOf<String, String>()

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is MultipleChoiceQuestion -> 1
            else -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.solve_closed_question, parent, false)
                ClosedQuestionViewHolder(view)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.solve_open_question, parent, false)
                OpenQuestionViewHolder(view)
            }
            else -> throw IllegalArgumentException("Geen viewtype")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val question = data[position]
        when (holder) {
            is OpenQuestionViewHolder -> holder.bind(question)
            is ClosedQuestionViewHolder -> holder.bind(question as MultipleChoiceQuestion)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class OpenQuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.vraagInput)
        private val answerInput: EditText = itemView.findViewById(R.id.editText)

        fun bind(question: Question) {
            questionTextView.text = question.question

            answerInput.doAfterTextChanged{ text ->
                solveExamViewModel.addAnswer(question.question, text.toString() )
                userAnswers[question.question] = text.toString()
            }
        }
    }

    inner class ClosedQuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.vraagInput)
        private val radioGroup: RadioGroup = itemView.findViewById(R.id.answer_group)

        fun bind(question: MultipleChoiceQuestion) {
            questionTextView.text = question.question

            question.answers.forEach { answer ->
                val radioButton = RadioButton(itemView.context).apply {
                    text = answer
                }
                radioGroup.addView(radioButton)

                if (userAnswers[question.question] == answer) {
                    radioButton.isChecked = true
                }

                radioButton.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        solveExamViewModel.addAnswer(question.question, answer)
                        userAnswers[question.question] = answer
                    }
                }
            }
        }
    }

    fun getUserAnswers(): Map<String, String> {
        return userAnswers
    }
}