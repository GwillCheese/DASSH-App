package com.hfad.dev322projectdassh

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

interface AccelerometerListener {
    fun onAccelerometerDataChanged(x: Float, y: Float, z: Float)
}

class AccelerometerManager(private val context: Context) : SensorEventListener {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private var listener: AccelerometerListener? = null
    private val stepDetector = StepDetector()
    
    fun setListener(listener: AccelerometerListener) {
        this.listener = listener
    }
    
    fun setStepListener(listener: StepListener) {
        stepDetector.setListener(listener)
    }
    
    fun startListening() {
        accelerometer?.let { 
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }
    
    fun stopListening() {
        sensorManager.unregisterListener(this)
    }
    
    fun isAvailable(): Boolean {
        return accelerometer != null
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val (x, y, z) = event.values
            listener?.onAccelerometerDataChanged(x, y, z)
            stepDetector.processAccelerometerData(x, y, z)
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No implementation needed for this use case
    }
    
    fun getStepCount(): Int {
        return stepDetector.getStepCount()
    }
    
    fun resetSteps() {
        stepDetector.resetSteps()
    }
}
