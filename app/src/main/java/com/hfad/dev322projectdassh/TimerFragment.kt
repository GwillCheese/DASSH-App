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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


class TimerFragment : Fragment(), AccelerometerListener, GpsLocationListener {
    private lateinit var stopwatch: Chronometer
    private val stopwatchManager = StopwatchManager()

    //Sensor Stuff
    private lateinit var accelerometerManager: AccelerometerManager

    //GPS Stuff
    private lateinit var appLocationManager: AppLocationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)
        
        //Get a reference to the stopwatch
        stopwatch = view.findViewById<Chronometer>(R.id.stopwatch)
        
        //Sensor stuff
        accelerometerManager = AccelerometerManager(requireContext())
        accelerometerManager.setListener(this)

        //GPS stuff
        appLocationManager = AppLocationManager(requireContext())
        appLocationManager.setListener(this)

        //Restore the previous state
        stopwatchManager.restoreState(savedInstanceState, stopwatch)

        //start button
        val startButton = view.findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            stopwatchManager.start(stopwatch)
        }

        //pause button
        val pauseButton = view.findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            stopwatchManager.pause(stopwatch)
        }

        //reset button
        val resetButton = view.findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            stopwatchManager.reset(stopwatch)
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
        stopwatchManager.handleOnPause(stopwatch)
    }

    override fun onResume() {
        super.onResume()

        accelerometerManager.startListening()
        if (appLocationManager.isLocationPermissionGranted()) {
            appLocationManager.startLocationUpdates()
        }
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

    override fun onLocationReceived(location: Location) {
        // Handle location updates here
        // Example: Display location, save it, use it for tracking
    }

    override fun onLocationPermissionGranted() {
        Toast.makeText(requireContext(), "Location Permission Granted", Toast.LENGTH_SHORT).show()
    }

    override fun onLocationPermissionDenied() {
        Toast.makeText(requireContext(), "Location Permission Denied", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) { // locationPermissionCode from AppLocationManager
            appLocationManager.handlePermissionResult(grantResults)
        }
    }

    private fun getLocation() {
        if (!appLocationManager.isLocationPermissionGranted()) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        } else {
            appLocationManager.startLocationUpdates()
        }
    }


}