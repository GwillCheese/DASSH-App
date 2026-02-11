package com.hfad.dev322projectdassh

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat

interface GpsLocationListener {
    fun onLocationReceived(location: Location)
    fun onLocationPermissionGranted()
    fun onLocationPermissionDenied()
}

class AppLocationManager(private val context: Context) : LocationListener {
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var listener: GpsLocationListener? = null
    private val locationPermissionCode = 2
    
    // GPS accuracy filtering
    private val maxAcceptableAccuracy = 20f // 20 meters maximum accuracy
    
    fun setListener(listener: GpsLocationListener) {
        this.listener = listener
    }
    
    fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }
    }
    
    fun stopLocationUpdates() {
        locationManager.removeUpdates(this)
    }
    
    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    
    fun isGpsEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    
    override fun onLocationChanged(location: Location) {
        // Filter out inaccurate GPS readings
        if (location.accuracy <= maxAcceptableAccuracy) {
            listener?.onLocationReceived(location)
        }
    }
    
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // Handle provider status changes if needed
    }
    
    override fun onProviderEnabled(provider: String) {
        // Handle provider enabled
    }
    
    override fun onProviderDisabled(provider: String) {
        // Handle provider disabled
    }
    
    fun handlePermissionResult(grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            listener?.onLocationPermissionGranted()
            startLocationUpdates()
        } else {
            listener?.onLocationPermissionDenied()
        }
    }
}
