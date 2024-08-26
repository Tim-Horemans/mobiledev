package edu.ap.projecty.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.Timer
import java.util.TimerTask

class SolveExamService : Service() {

    private val TAG = "ExamDataService"
    private var examDuration = 0L
    private lateinit var timer: Timer

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        startExamTimer()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun startExamTimer() {
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                examDuration++
                Log.d(TAG, "Exam duration: $examDuration seconds")
            }
        }, 0, 1000)
    }

    fun stopExamTimer() {
        timer.cancel()
        Log.d(TAG, "Exam timer stopped at $examDuration seconds")
    }

    fun collectData(location: String) {
        val examData = mapOf(
            "duration" to examDuration,
            "location" to location
        )
        Log.d(TAG, "Sending exam data: $examData")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopExamTimer()
        Log.d(TAG, "Service destroyed")
    }
}