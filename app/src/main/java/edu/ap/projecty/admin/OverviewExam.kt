package edu.ap.projecty.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import edu.ap.projecty.R
import edu.ap.projecty.databinding.OverviewExamsLayoutBinding
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.Student
import edu.ap.projecty.repository.ExamViewModel
import edu.ap.projecty.repository.SolvedExamViewModel
import edu.ap.projecty.repository.StudentDetails

class OverviewExam : AppCompatActivity() {

    private lateinit var binding: OverviewExamsLayoutBinding
    private lateinit var examViewModel: SolvedExamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = OverviewExamsLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnreturn.setOnClickListener {
            finish()
        }
        examViewModel = ViewModelProvider(this).get(SolvedExamViewModel::class.java)

        examViewModel.groupedSolvedExams.observe(this) { groupedExamsMap ->
            updateUI(groupedExamsMap)
        }

        examViewModel.loadGroupedSolvedExams()
    }

    private fun updateUI(groupedExamsMap: Map<String, List<StudentDetails>>) {
        val container = binding.container
        container.removeAllViews()

        for ((exam, students) in groupedExamsMap) {
            val examTitleTextView = TextView(this).apply {
                text = exam
                textSize = 18f
                setPadding(0, 16, 0, 8)
            }
            container.addView(examTitleTextView)

            for (student in students) {
                val studentNameTextView = TextView(this).apply {
                    text = "Name: ${student.name}"
                    textSize = 16f
                    setPadding(32, 8, 0, 4)
                }
                container.addView(studentNameTextView)

                val studentPointsTextView = TextView(this).apply {
                    text = "Points: ${student.points}/${student.totalQuestion}"
                    textSize = 16f
                    setPadding(32, 4, 0, 4)
                }
                container.addView(studentPointsTextView)

                val studentDurationTextView = TextView(this).apply {
                    text = "Duration: ${formatDuration(student.duration)} seconds"
                    textSize = 16f
                    setPadding(32, 4, 0, 8)
                }
                container.addView(studentDurationTextView)
            }
        }
    }

    private fun formatDuration(duration: Long?): String {
        return if (duration != null) {
            val seconds = (duration / 1000) % 60
            val minutes = (duration / (1000 * 60)) % 60
            val hours = (duration / (1000 * 60 * 60)) % 24

            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            "Unknown"
        }
    }
}