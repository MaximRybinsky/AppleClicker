package com.example.primaryengine.framework

import android.content.Context
import android.view.View

class AndroidInputK(val context : Context, val view : View, val scaleX : Float, val scaleY : Float) : InputK {
//    override lateinit var accelHandler : AccelerometerHandlerK
    override lateinit var keyHandlerK : KeyboardHandlerK
    override lateinit var touchHandler : TouchHandlerK

    // устанавливаем обработчики нажатий клавиш и касаний

    init {
//        accelHandler = AccelerometerHandlerK(context)
        keyHandlerK = KeyboardHandlerK(view)
        touchHandler = MultiTouchHandlerK(view, scaleX, scaleY)
    }
}