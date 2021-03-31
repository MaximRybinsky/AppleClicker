package com.example.primaryengine.framework

import android.util.Log
import android.view.View
import com.example.primaryengine.framework.InputK.KeyEvent

class KeyboardHandlerK(val view : View) : View.OnKeyListener { // Обработчик нажатий клавиатуры
    // Измененная копия обработчика касаний MultiTouchHandlerK
    // Там мы обрабатывали нажатия на экран, а здесь обрабатываем нажатие клавиш
    //
    // KeyboardHandlerK мы почти не будем пользоваться, но он нужен в обязательном порядке для работы программы.
    var pressedKey : BooleanArray = booleanArrayOf(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)
    var keyEventPool : PoolK<KeyEvent>
     var keyEvents : MutableList<KeyEvent> = mutableListOf()
     var keyEventBuffer : MutableList<KeyEvent> = mutableListOf()

    init {
        var factory : PoolK.PoolObjectFactory<KeyEvent> = object : PoolK.PoolObjectFactory<KeyEvent> {
            override fun createObject() : KeyEvent {
                return KeyEvent()
            }
        }
        keyEventPool = PoolK<KeyEvent>(factory, 100)
        view.setOnKeyListener(this)
        view.isFocusableInTouchMode = true
        view.requestFocus()
    }

    override fun onKey(v: View?, keyCode: Int, event: android.view.KeyEvent?): Boolean {
        if (v == null || event == null) return false;
        synchronized(this) {
            var keyEvent : KeyEvent = keyEventPool.newObject()
            keyEvent.keyCode = keyCode
            keyEvent.keyChar = event.getUnicodeChar().toChar()
            when(event.action) {
                android.view.KeyEvent.ACTION_DOWN -> {
                    keyEvent.type = keyEvent.KEY_DOWN
                    if (keyCode in 0..127) pressedKey[keyCode] = true
                }
                android.view.KeyEvent.ACTION_UP -> {
                    keyEvent.type = keyEvent.KEY_UP
                    if (keyCode in 0..127) pressedKey[keyCode] = false
                }
            }
            keyEventBuffer.add(keyEvent)
        }
        return false
    }

    fun isKeyPressed(keyCode : Int) : Boolean {
        if (keyCode in 0..127) return pressedKey[keyCode]
        return false
    }

    fun getKeyEventss() : MutableList<KeyEvent> {
        synchronized(this) {
            for (touch in keyEvents) keyEventPool.free(touch)
            keyEvents.clear()
            keyEvents.addAll(keyEventBuffer)
            keyEventBuffer.clear()
            return keyEvents
        }
    }

}