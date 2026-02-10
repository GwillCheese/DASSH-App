package com.hfad.dev322projectdassh

interface StepListener {
    fun onStepDetected(stepCount: Int)
}

class StepDetector {
    private var stepCount = 0
    private var listener: StepListener? = null
    private var lastMagnitude = 0f
    private var isPeakDetected = false
    
    // Threshold for step detection (adjust based on testing)
    private val stepThreshold = 1.5f
    
    fun setListener(listener: StepListener) {
        this.listener = listener
    }
    
    fun processAccelerometerData(x: Float, y: Float, z: Float) {
        // Calculate magnitude of acceleration
        val magnitude = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        
        // Simple peak detection algorithm
        if (magnitude > stepThreshold && !isPeakDetected) {
            isPeakDetected = true
        } else if (magnitude < stepThreshold && isPeakDetected) {
            // We've detected a peak and now we're below threshold
            isPeakDetected = false
            stepCount++
            listener?.onStepDetected(stepCount)
        }
        
        lastMagnitude = magnitude
    }
    
    fun getStepCount(): Int {
        return stepCount
    }
    
    fun resetSteps() {
        stepCount = 0
        lastMagnitude = 0f
        isPeakDetected = false
        listener?.onStepDetected(0)
    }
}
