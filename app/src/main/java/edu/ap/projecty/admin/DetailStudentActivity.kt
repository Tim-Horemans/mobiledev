package edu.ap.projecty.admin

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.R
import edu.ap.projecty.adapters.ExamListAdapter
import edu.ap.projecty.databinding.ActivityDetailStudentBinding
import edu.ap.projecty.model.SolvedExam
import edu.ap.projecty.model.Student
import edu.ap.projecty.repository.ExamViewModel
import edu.ap.projecty.repository.SolvedExamViewModel
import edu.ap.projecty.repository.StudentViewModel


class DetailStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStudentBinding
    private lateinit var examsViewModel: ExamViewModel
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var solvedExamViewModel: SolvedExamViewModel
    private var solvedExamList: List<SolvedExam> = emptyList() // Initialize as empty list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val student = intent.getSerializableExtra("STUDENT") as? Student
        val studentName = student?.name

        solvedExamViewModel = ViewModelProvider(this).get(SolvedExamViewModel::class.java)

        val solvedExamsListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf<String>()
        )
        binding.lvSolvedExams.adapter = solvedExamsListAdapter

        if (student != null) {
            solvedExamViewModel.getSolvedExamsByStudentId(student.key).observe(this) { solvedExams ->
                if (solvedExams != null) {
                    solvedExamList = solvedExams
                    solvedExamsListAdapter.clear()
                    solvedExamsListAdapter.addAll(solvedExamList.map { it.name })
                    solvedExamsListAdapter.notifyDataSetChanged()
                }
            }
        }

        examsViewModel = ViewModelProvider(this).get(ExamViewModel::class.java)
        studentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        val recyclerView = binding.recyclerViewExams
        val examsLinkedView = binding.recyclerViewExamsLinkedToStudent

        val adapterLinkedExam = ExamListAdapter(emptyList()) {}
        examsLinkedView.adapter = adapterLinkedExam
        examsLinkedView.layoutManager = LinearLayoutManager(this)

        val adapter = ExamListAdapter(emptyList()) { selectedExam ->
            if (student != null) {
                addExamToStudent(selectedExam.key, student.key)
            }
        }

        binding.lvSolvedExams.onItemClickListener = OnItemClickListener { arg0, arg1, arg2, arg3 ->
            showAlertDialog(layoutInflater)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        examsViewModel.getExams().observe(this) { exams ->
            adapter.updateExams(exams)
        }

        student?.let {
            studentViewModel.getExamsFromStudent(it.exams).observe(this) { linkedExams ->
                adapterLinkedExam.updateExams(linkedExams)
            }
        }

        binding.ivAddExam.setOnClickListener {
            if (recyclerView.visibility == View.GONE) {
                recyclerView.visibility = View.VISIBLE
            }
        }

        binding.tvStudentName.text = studentName ?: "Unknown Student"
    }

    private fun addExamToStudent(examKey: String, studentId: String) {
        studentViewModel.addExamToStudent(examKey, studentId)
    }

    private fun showAlertDialog(layoutInflater: LayoutInflater) {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_solved_exam, null)

        val textView = view.findViewById<TextView>(R.id.dialog_message)

        val builder = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }

        // Create and show the dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }
}