package edu.ap.projecty.admin

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import edu.ap.projecty.R
import edu.ap.projecty.admin.fragments.ClosedQuestion
import edu.ap.projecty.admin.fragments.OpenQuestion
import edu.ap.projecty.databinding.ActivityAddQuestionToExamBinding
import edu.ap.projecty.model.MultipleChoiceQuestion
import edu.ap.projecty.model.Question
import java.util.Objects

class AddQuestionToExam : AppCompatActivity() {
    private lateinit var binding: ActivityAddQuestionToExamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuestionToExamBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button6.setOnClickListener {
            showOpenQuestionFragment()
        }

        binding.button7.setOnClickListener {
            showClosedQuestionFragment()
        }

        binding.submitQuestion.setOnClickListener {
            handleFragmentData()
        }
    }

    private fun showOpenQuestionFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, OpenQuestion(), "FIRST")
        fragmentTransaction.commit()
    }

    private fun showClosedQuestionFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, ClosedQuestion(), "SECOND")
        fragmentTransaction.commit()
    }

    private fun handleFragmentData() {
        val closedQuestionFragment = supportFragmentManager.findFragmentByTag("SECOND") as? ClosedQuestion
        val openQuestionFragment = supportFragmentManager.findFragmentByTag("FIRST") as? OpenQuestion
        val resultIntent = Intent()

        if (closedQuestionFragment != null && closedQuestionFragment.isVisible) {
            val result = closedQuestionFragment.getResult()
            val question = MultipleChoiceQuestion(result.second, result.first, result.third)
            Log.i("result in add quest", result.toString())
            resultIntent.putExtra("Result", question)

        } else if (openQuestionFragment != null && openQuestionFragment.isVisible) {
            val result = openQuestionFragment.getResult()
            val question = Question(result.first, result.second)
            Log.i("result in add quest", result.first)
            resultIntent.putExtra("Result", question)
        }
         else {
            println("No fragment is visible")
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}