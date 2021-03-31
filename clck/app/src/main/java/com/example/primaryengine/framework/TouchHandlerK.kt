package com.example.primaryengine.framework

import android.view.View

interface TouchHandlerK : View.OnTouchListener {
    var touchEvents : MutableList<InputK.TouchEvent>
    fun isTouchDown(pointer : Int) : Boolean

    fun getTouchX(pointer : Int) : Int

    fun getTouchY(pointer : Int) : Int

    fun getTouchEventss() : MutableList<InputK.TouchEvent>
}