package com.hfad.dev322projectdassh

import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer

class StopwatchManager {
    var running: Boolean = false
        private set
    private var offset: Long = 0
    
    companion object {
        const val OFFSET_KEY = "offset"
        const val RUNNING_KEY = "running"
        const val BASE_KEY = "base"
    }
    
    fun start(stopwatch: Chronometer) {
        if (!running) {
            setBaseTime(stopwatch)
            stopwatch.start()
            running = true
        }
    }
    
    fun pause(stopwatch: Chronometer) {
        if (running) {
            saveOffset(stopwatch)
            stopwatch.stop()
            running = false
        }
    }
    
    fun reset(stopwatch: Chronometer) {
        offset = 0
        setBaseTime(stopwatch)
        if (running) {
            stopwatch.stop()
            running = false
        }
    }
    
    fun saveState(outState: Bundle, stopwatch: Chronometer) {
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, stopwatch.base)
    }
    
    fun restoreState(savedInstanceState: Bundle?, stopwatch: Chronometer) {
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else {
                setBaseTime(stopwatch)
            }
        }
    }
    
    fun handleOnResume(stopwatch: Chronometer) {
        if (running) {
            setBaseTime(stopwatch)
            stopwatch.start()
            // Don't reset offset to 0 here - preserve the timing
        }
    }
    
    fun handleOnPause(stopwatch: Chronometer) {
        if (running) {
            saveOffset(stopwatch)
            stopwatch.stop()
        }
    }
    
    private fun setBaseTime(stopwatch: Chronometer) {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }
    
    private fun saveOffset(stopwatch: Chronometer) {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
}
