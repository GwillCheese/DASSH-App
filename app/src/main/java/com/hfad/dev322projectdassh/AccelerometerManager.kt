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
    
    fun setListener(listener: AccelerometerListener) {
        this.listener = listener
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
            listener?.onAccelerometerDataChanged(event.values[0], event.values[1], event.values[2])
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No implementation needed for this use case
    }
}
