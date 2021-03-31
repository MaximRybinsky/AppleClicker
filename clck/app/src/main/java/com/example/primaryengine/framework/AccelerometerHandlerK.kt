package com.example.primaryengine.framework

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class AccelerometerHandlerK(val context : Context) : SensorEventListener {
    // Держатель асселерометра, мы им не пользуемся и в нём ошибка критическая
    var accelX : Float = 0F
    var accelY : Float = 0F
    var accelZ : Float = 0F

    init {
        //!next line(14) - error! must be
        val manager : SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size != 0)  {
            val accelerometer: Sensor = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0)
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!=null) {
            accelX = event.values[0]
            accelY = event.values[1]
            accelZ = event.values[2]
        }
    }
}