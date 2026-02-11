package com.hfad.dev322projectdassh

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.osmdroid.views.MapView


class TimerFragment : Fragment(), AccelerometerListener, GpsLocationListener, StepListener {
    private lateinit var stopwatch: Chronometer
    private val stopwatchManager = StopwatchManager()

    //Sensor Stuff
    private lateinit var accelerometerManager: AccelerometerManager

    //GPS Stuff
    private lateinit var appLocationManager: AppLocationManager
    
    //Tracking Stuff
    private lateinit var distanceCalculator: DistanceCalculator
    private lateinit var speedCalculator: SpeedCalculator
    private var startTime: Long = 0
    
    //UI Elements
    private lateinit var stepsTextView: TextView
    private lateinit var distanceTextView: TextView
    private lateinit var speedTextView: TextView
    private lateinit var mapView: MapView
    private lateinit var mapManager: MapManager
    private lateinit var statusTextView: TextView
    private lateinit var gpsStatusTextView: TextView
    
    // Permission request launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            appLocationManager.startLocationUpdates()
            gpsStatusTextView.text = "GPS Active"
            Toast.makeText(requireContext(), "Location permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)
        
        //Get a reference to the stopwatch
        stopwatch = view.findViewById<Chronometer>(R.id.stopwatch)
        
        //Get references to UI elements
        stepsTextView = view.findViewById(R.id.steps_count)
        distanceTextView = view.findViewById(R.id.distance_count)
        speedTextView = view.findViewById(R.id.speed_count)
        mapView = view.findViewById(R.id.map_view)
        statusTextView = view.findViewById(R.id.status_text)
        gpsStatusTextView = view.findViewById(R.id.gps_status_text)
        
        //Initialize map
        mapManager = MapManager(requireContext())
        mapManager.initializeMap(mapView)
        
        //Sensor stuff
        accelerometerManager = AccelerometerManager(requireContext())
        accelerometerManager.setListener(this)
        accelerometerManager.setStepListener(this)

        //GPS stuff
        appLocationManager = AppLocationManager(requireContext())
        appLocationManager.setListener(this)
        
        //Tracking stuff
        distanceCalculator = DistanceCalculator()
        speedCalculator = SpeedCalculator()

        //Restore the previous state
        stopwatchManager.restoreState(savedInstanceState, stopwatch)

        //start button
        val startButton = view.findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            startTime = System.currentTimeMillis()
            stopwatchManager.start(stopwatch)
            statusTextView.text = "Workout in Progress"
            requestLocationPermission()
        }

        //pause button
        val pauseButton = view.findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            val elapsedTime = System.currentTimeMillis() - startTime
            speedCalculator.addTime(elapsedTime)
            stopwatchManager.pause(stopwatch)
            appLocationManager.stopLocationUpdates()
            statusTextView.text = "Workout Paused"
            gpsStatusTextView.text = "GPS Paused"
        }

        //reset button
        val resetButton = view.findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            resetAllTracking()
            stopwatchManager.reset(stopwatch)
            statusTextView.text = "Ready to Start"
            gpsStatusTextView.text = "GPS Inactive"
        }
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()

        accelerometerManager.stopListening()
        appLocationManager.stopLocationUpdates()
        mapManager.onPause()
        stopwatchManager.handleOnPause(stopwatch)
    }

    override fun onResume() {
        super.onResume()

        accelerometerManager.startListening()
        if (appLocationManager.isLocationPermissionGranted()) {
            appLocationManager.startLocationUpdates()
        }
        mapManager.onResume()
        stopwatchManager.handleOnResume(stopwatch)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        stopwatchManager.saveState(savedInstanceState, stopwatch)
        super.onSaveInstanceState(savedInstanceState)
    }


    override fun onAccelerometerDataChanged(x: Float, y: Float, z: Float) {
        // Handle accelerometer data here
        // Example: Detect shake, motion, etc.
    }

    override fun onStepDetected(stepCount: Int) {
        stepsTextView.text = stepCount.toString()
    }

    override fun onLocationReceived(location: Location) {
        val distance = distanceCalculator.addLocation(location)
        speedCalculator.addDistance(distance)
        mapManager.addLocationToRoute(location)
        updateMetricsDisplay()
    }
    
    private fun updateMetricsDisplay() {
        val distanceKm = distanceCalculator.getTotalDistanceInKilometers()
        val avgSpeedKmh = speedCalculator.getAverageSpeedKmh()
        
        distanceTextView.text = String.format("%.2f km", distanceKm)
        speedTextView.text = String.format("%.1f km/h", avgSpeedKmh)
    }

    override fun onLocationPermissionGranted() {
        Toast.makeText(requireContext(), "Location Permission Granted", Toast.LENGTH_SHORT).show()
    }

    override fun onLocationPermissionDenied() {
        Toast.makeText(requireContext(), "Location Permission Denied", Toast.LENGTH_SHORT).show()
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                appLocationManager.startLocationUpdates()
                gpsStatusTextView.text = "GPS Active"
                Toast.makeText(requireContext(), "Location permission granted", Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    
    private fun resetAllTracking() {
        accelerometerManager.resetSteps()
        distanceCalculator.reset()
        speedCalculator.reset()
        startTime = 0
        appLocationManager.stopLocationUpdates()
        mapManager.clearRoute()
        
        // Reset UI displays
        stepsTextView.text = "0"
        distanceTextView.text = "0.0 km"
        speedTextView.text = "0.0 km/h"
        statusTextView.text = "Ready to Start"
        gpsStatusTextView.text = "GPS Inactive"
    }


}