package edu.ap.projecty.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.health.TimerStat
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
        }, 0, 1000) // Timer task runs every second
    }

    fun stopExamTimer() {
        timer.cancel()
        Log.d(TAG, "Exam timer stopped at $examDuration seconds")
    }

    fun collectAndSendData(answers: Map<String, String>, location: String) {
        // Bundle the data
        val examData = mapOf(
            "answers" to answers,
            "duration" to examDuration,
            "location" to location
        )
        // Code to send data to backend
        Log.d(TAG, "Sending exam data: $examData")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopExamTimer()
        Log.d(TAG, "Service destroyed")
    }
}