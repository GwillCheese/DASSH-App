package com.hfad.dev322projectdassh

class SpeedCalculator {
    private var totalDistance = 0.0 // in meters
    private var totalTime = 0L // in milliseconds
    
    fun addDistance(distance: Double) {
        totalDistance += distance
    }
    
    fun addTime(time: Long) {
        totalTime += time
    }
    
    fun getCurrentSpeed(distance: Double, timeMs: Long): Double {
        if (timeMs == 0L) return 0.0
        return (distance / timeMs) * 1000.0 // m/s
    }
    
    fun getAverageSpeed(): Double {
        if (totalTime == 0L) return 0.0
        return (totalDistance / totalTime) * 1000.0 // m/s
    }
    
    fun getAverageSpeedKmh(): Double {
        return getAverageSpeed() * 3.6 // Convert m/s to km/h
    }
    
    fun getAverageSpeedMph(): Double {
        return getAverageSpeed() * 2.237 // Convert m/s to mph
    }
    
    fun reset() {
        totalDistance = 0.0
        totalTime = 0L
    }
    
    // Format speed for display
    fun formatSpeed(speedMs: Double): String {
        return String.format("%.1f m/s", speedMs)
    }
    
    fun formatSpeedKmh(speedMs: Double): String {
        return String.format("%.1f km/h", speedMs * 3.6)
    }
    
    fun formatSpeedMph(speedMs: Double): String {
        return String.format("%.1f mph", speedMs * 2.237)
    }
}
