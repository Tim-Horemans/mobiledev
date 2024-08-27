package edu.ap.projecty.user.fragments

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ap.projecty.adapters.SolveExamAdapter
import edu.ap.projecty.databinding.FragmentSolveExamBinding
import edu.ap.projecty.helper.LocationHelper
import edu.ap.projecty.helper.TimeTracker
import edu.ap.projecty.model.Exam
import edu.ap.projecty.repository.SolveExamViewModel


class SolveExamFragment : Fragment() {

    private lateinit var _binding: FragmentSolveExamBinding
    private lateinit var solveExamAdapter: SolveExamAdapter
    private lateinit var solveExamViewModel: SolveExamViewModel
    private lateinit var studentId: String
    private lateinit var studentName: String
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationHelper: LocationHelper
    private var locationPair: Pair<String, String>? = null

    private lateinit var timeTracker: TimeTracker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSolveExamBinding.inflate(inflater, container, false)
        timeTracker = TimeTracker()

        val exam = arguments?.getSerializable("Exam") as Exam
        studentId = arguments?.getString("studentId") as String
        studentName = arguments?.getString("studentName") as String
        solveExamViewModel = ViewModelProvider(this).get(SolveExamViewModel::class.java)

        locationHelper = LocationHelper(
            requireContext(),
            requireActivity(),
            LOCATION_PERMISSION_REQUEST_CODE,
            { latitude, longitude ->
                locationPair = Pair(latitude, longitude)
                Log.d("LocationHelper", "Location updated: $latitude, $longitude")
            },
            {
                Log.i("permissions", "Location permission denied")
            }
        )

        fetchLocation()

        val openQuestions = exam.openQuestions
        val closedQuestions = exam.closedQuestion
        val allQuestions = openQuestions + closedQuestions
        solveExamAdapter = SolveExamAdapter(allQuestions, solveExamViewModel)
        setupRecyclerView()

        _binding.btnSubmit.setOnClickListener {
            val vaaal = locationPair
            if (locationPair != null) {
                val elapsedTime = timeTracker.getElapsedTime()
                solveExamViewModel.submitExam(exam, studentId, studentName,locationPair!!, elapsedTime)
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Log.i("location", "Location data not available yet")
                // Notify user to wait for location data
            }
        }

        timeTracker.start()

        return _binding.root
    }

    private fun fetchLocation() {
        Log.d("LocationFetch", "Requesting location")
        locationHelper.getUserLocation()
    }

    private fun setupRecyclerView() {
        _binding.recyclerSolveExams.layoutManager = LinearLayoutManager(requireContext())
        _binding.recyclerSolveExams.adapter = solveExamAdapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationHelper.handlePermissionsResult(requestCode, grantResults)
    }
}