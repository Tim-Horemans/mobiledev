package edu.ap.projecty.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.R
import edu.ap.projecty.adapters.ExamListAdapter
import edu.ap.projecty.databinding.ActivityDetailStudentBinding
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.SolvedExam
import edu.ap.projecty.model.Student
import edu.ap.projecty.repository.ExamViewModel
import edu.ap.projecty.repository.SolvedExamViewModel
import edu.ap.projecty.repository.StudentViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


class DetailStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStudentBinding
    private lateinit var examsViewModel: ExamViewModel
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var solvedExamViewModel: SolvedExamViewModel
    private var solvedExamList: List<SolvedExam> = emptyList()
    private var examList: List<Exam> = emptyList()
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnreturn.setOnClickListener {
            finish()
        }
        val student = intent.getSerializableExtra("STUDENT") as? Student
        val studentName = student?.name

        solvedExamViewModel = ViewModelProvider(this).get(SolvedExamViewModel::class.java)
        examsViewModel = ViewModelProvider(this).get(ExamViewModel::class.java)
        studentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        val solvedExamsListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf<String>()
        )
        binding.lvSolvedExams.adapter = solvedExamsListAdapter

        student?.let { it ->
            solvedExamViewModel.getSolvedExamsByStudentId(it.key).observe(this) { solvedExams ->
                solvedExams?.let {
                    solvedExamList = it
                    solvedExamsListAdapter.clear()
                    solvedExamsListAdapter.addAll(solvedExamList.map { exam -> exam.name })
                    solvedExamsListAdapter.notifyDataSetChanged()
                }
            }
        }

        val examsLinkedView = binding.recyclerViewExamsLinkedToStudent

        binding.lvSolvedExams.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val selectedSolvedExam = solvedExamList[position]
            val examName = selectedSolvedExam.name
            val points = selectedSolvedExam.totalPoints
            val lat = selectedSolvedExam.latitude
            val long = selectedSolvedExam.longtitude
            val duration = selectedSolvedExam.elapsedTime
            val totalQuestions = selectedSolvedExam.totalQuestions

            showAlertDialog(examName, points, totalQuestions,lat.toDouble(), long.toDouble(), duration)
        }


        val adapterLinkedExam = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf<String>()
        )
        examsLinkedView.adapter = adapterLinkedExam
        student?.let { it ->
            studentViewModel.getExamsFromStudent(it.exams).observe(this) { linkedExams ->
                linkedExams?.let {
                    examList = it
                    adapterLinkedExam.clear()
                    adapterLinkedExam.addAll(linkedExams.map { exam -> exam.name })
                    adapterLinkedExam.notifyDataSetChanged()
                }
            }
        }

        binding.ivAddExam.setOnClickListener {
            showSelectExamDialog(student)
        }

        binding.tvStudentName.text = studentName ?: "Unknown Student"
    }

    private fun addExamToStudent(examKey: String, studentId: String) {
        studentViewModel.addExamToStudent(examKey, studentId)
    }

    private fun showAlertDialog(
        examName: String,
        points: Int?,
        totalQuestion: Int,
        lat: Double?,
        long: Double?,
        duration: Long?
    ) {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_solved_exam, null)

        val examNameTextView: TextView = view.findViewById(R.id.dialog_exam_name)
        val examPointsTextView: TextView = view.findViewById(R.id.dialog_exam_points)
        val examDurationTextView: TextView = view.findViewById(R.id.dialog_exam_duration)

        examNameTextView.text = "Exam Name: $examName"
        examPointsTextView.text = "Points: ${points}/${totalQuestion}"
        examDurationTextView.text = "Duration: ${formatDuration(duration)}"

        mapView = view.findViewById(R.id.mapview)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        val geoPoint = if (lat != null && long != null) {
            GeoPoint(lat, long)
        } else {
            GeoPoint(51.5074, -0.1278)
        }
        mapView.controller.setCenter(geoPoint)
        mapView.controller.setZoom(15.0)

        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.title = examName
        mapView.overlays.add(marker)

        val builder = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }
    private fun showSelectExamDialog(student: Student?) {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.add_exam_dialog, null)

        val listView = view.findViewById<ListView>(R.id.listViewExams)
        val examAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf<String>()
        )
        listView.adapter = examAdapter

        examsViewModel.getExams().observe(this) { exams ->
            exams?.let {
                examList = it // Update the global list
                examAdapter.clear()
                examAdapter.addAll(exams.map { exam -> exam.name })
                examAdapter.notifyDataSetChanged()
            }
        }

        val builder = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(true)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()

        listView.setOnItemClickListener { _, _, position, _ ->
            if (position < examList.size) {
                val selectedExam = examList[position]
                student?.let {
                    addExamToStudent(selectedExam.key, it.key)
                }
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
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