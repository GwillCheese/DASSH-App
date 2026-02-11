package com.hfad.dev322projectdassh

import android.location.Location

class DistanceCalculator {
    private var totalDistance = 0.0 // in meters
    private var lastLocation: Location? = null
    
    // Minimum distance threshold to prevent GPS drift
    private val minDistanceThreshold = 5.0 // 5 meters minimum
    
    fun addLocation(location: Location): Double {
        lastLocation?.let { last ->
            val distance = last.distanceTo(location).toDouble()
            
            // Only add distance if it exceeds minimum threshold
            if (distance >= minDistanceThreshold) {
                totalDistance += distance
            }
        }
        lastLocation = location
        return totalDistance
    }
    
    fun getTotalDistance(): Double {
        return totalDistance
    }
    
    fun getTotalDistanceInKilometers(): Double {
        return totalDistance / 1000.0
    }
    
    fun getTotalDistanceInMiles(): Double {
        return totalDistance * 0.000621371
    }
    
    fun reset() {
        totalDistance = 0.0
        lastLocation = null
    }
    
    // Haversine formula for calculating distance between two coordinates
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 63710.0 // Earth's radius in kilometers
        
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        
        val a = (kotlin.math.sin(latDistance / 2) * kotlin.math.sin(latDistance / 2) +
                kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
                kotlin.math.sin(lonDistance / 2) * kotlin.math.sin(lonDistance / 2))
        
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        
        return R * c * 1000 // Convert to meters
    }
}
