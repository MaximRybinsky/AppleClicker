package com.example.primaryengine.framework

interface InputK {
//    var accelHandler : AccelerometerHandlerK
    var keyHandlerK : KeyboardHandlerK
    var touchHandler : TouchHandlerK

    class KeyEvent {
        val KEY_DOWN : Int = 0
        val KEY_UP : Int = 1

        var type : Int = 0
        var keyCode : Int = 0
        var keyChar : Char = 'W'
    }

    class TouchEvent {
        val TOUCH_DOWN : Int = 0
        val TOUCH_UP : Int = 1
        val TOUCH_DRAGGED : Int = 2

        var type : Int = 0
        var x : Int = 0
        var y : Int = 0
        var pointer : Int = 0
    }
}