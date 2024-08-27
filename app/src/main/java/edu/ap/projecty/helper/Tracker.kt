package edu.ap.projecty.helper

class TimeTracker {

    private var startTime: Long = 0
    private var endTime: Long = 0
    private var isTracking: Boolean = false


    fun start() {
        if (!isTracking) {
            startTime = System.currentTimeMillis()
            isTracking = true
        }
    }

    fun getElapsedTime(): Long {
        return if (isTracking) {
            // If still tracking, return time elapsed since start
            System.currentTimeMillis() - startTime
        } else {
            // Otherwise, return the total time from start to stop
            endTime - startTime
        }
    }
}