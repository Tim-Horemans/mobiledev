package edu.ap.projecty.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SolveExamService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}