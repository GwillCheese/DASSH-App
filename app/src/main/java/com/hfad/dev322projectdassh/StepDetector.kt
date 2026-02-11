package com.hfad.dev322projectdassh

interface StepListener {
    fun onStepDetected(stepCount: Int)
}

class StepDetector {
    private var stepCount = 0
    private var listener: StepListener? = null
    private var lastMagnitude = 0f
    private var isPeakDetected = false
    private var lastStepTime = 0L
    
    // Threshold for step detection (increased for accuracy)
    private val stepThreshold = 2.5f
    // Minimum time between steps (300ms to prevent double-counting)
    private val minStepInterval = 300L
    
    fun setListener(listener: StepListener) {
        this.listener = listener
    }
    
    fun processAccelerometerData(x: Float, y: Float, z: Float) {
        // Calculate magnitude of acceleration
        val magnitude = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val currentTime = System.currentTimeMillis()
        
        // Enhanced peak detection with timing filter
        if (magnitude > stepThreshold && !isPeakDetected) {
            isPeakDetected = true
        } else if (magnitude < stepThreshold && isPeakDetected) {
            // We've detected a peak and now we're below threshold
            isPeakDetected = false
            
            // Check if enough time has passed since last step
            if (currentTime - lastStepTime >= minStepInterval) {
                stepCount++
                lastStepTime = currentTime
                listener?.onStepDetected(stepCount)
            }
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
        lastStepTime = 0L
        listener?.onStepDetected(0)
    }
}
