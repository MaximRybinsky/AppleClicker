package com.example.primaryengine.framework

import android.view.MotionEvent
import android.view.View
import com.example.primaryengine.framework.InputK.TouchEvent

class MultiTouchHandlerK(val view : View, val scaleX : Float, val scaleY : Float) : TouchHandlerK {
    // "Держатель многих нажатий". Класс, экземпляр которого, обрабатывает нажатия на экран.
    // Это делается с помощью многих переменных и наследования от TouchHandlerK(Который наследуется от View.OnTouchListener)
    var isTouched : BooleanArray = booleanArrayOf(false,false,false,false,false)
    // переменная isTouched - это массив boolean. По-моему там 20 элементов.
    // Каждому нажатию соответствует порядковый номер.
    var touchX : IntArray = intArrayOf(0,0,0,0,0)
    var touchY : IntArray = intArrayOf(0,0,0,0,0)
    // переменные touchX и touchY хранят информацию о координатах нажатия
    var touchEventPool : PoolK<TouchEvent>
    // переменная выше отвечает за генерацию и взаимодействие с переменными нажатий TouchEvent
    override var touchEvents : MutableList<TouchEvent> = mutableListOf()
    // массив выше - это конечный массив элементов TouchEvent.
    // Именно его мы непосредственно используем в своих дальнейших целях. По этой же причине он override: чтобы мы могли брать эту переменную из экземпляра класса MultiTouchHandlerK
    var touchEventsBuffer : MutableList<TouchEvent> = mutableListOf()
    // массив выше - это промежуточный массив элементов TouchEvent.

    init {

        var factory : PoolK.PoolObjectFactory<TouchEvent> = object :
            PoolK.PoolObjectFactory<TouchEvent> {
            override fun createObject() : TouchEvent {
                return TouchEvent()
            }
        } // factory - экземпляр анонимного класса, который наследуется от PoolK.PoolObjectFactory и переписывает функцию createObject
          // чтобы возвращать нужный объект класса  TouchEvent
        touchEventPool = PoolK(factory, 5) // инициализировали Pool
        view.setOnTouchListener(this) // установили touchListener на текущую активность
    }

    override fun isTouchDown(pointer: Int): Boolean { // Возвращаем boolean значение нажатия
        synchronized(this) {
            if (pointer in 0..4) return isTouched[pointer]  // То есть, есть ли нажатие или нет
            return false
        }
    }

    override fun getTouchX(pointer: Int): Int { // Возвращаем координату Х нажатия
        synchronized(this) {
            if (pointer in 0..4) return touchX[pointer]
            return 0
        }
    }

    override fun getTouchY(pointer: Int): Int { // Возвращаем координату Y нажатия
        synchronized(this) {
            if (pointer in 0..4) return touchY[pointer]
            return 0
        }
    }

    override fun getTouchEventss() : MutableList<TouchEvent> { // Возвращаем все нажатия одним массивом
        synchronized(this) {
            for (touch in touchEvents) touchEventPool.free(touch) // сначала очищаем Pool объектов
            touchEvents.clear() // затем очищаем конечный массив
            touchEvents.addAll(touchEventsBuffer) // в конечный массив добавляем все элементы промежуточного массива
            touchEventsBuffer.clear() // очищаем промежуточный
            return touchEvents
        }
    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean { // Функция, которая выполняется при касании тачскрина
        synchronized(this) {
            if (event == null) return false // event - событие // Если нет события, то мы ничего не делаем
            var actionMasked = event.actionMasked // получаем код действия // Например, было ли нажатие, либо палец переместили, либо палец убрали с экрана. Это не полный список, а ознакомительный фрагмент.
            var pointerIndex = event.actionIndex  // получаем индекс действия. Типо числа, по которым можно обратиться к действию
            var pointerId = event.getPointerId(pointerIndex) // получаем порядковый номер действия. Индекс действия под одним пальцем может меняться, но каждому пальцу на экране присваивается порядковый номер.
            var touchEvent : TouchEvent = touchEventPool.newObject()
            // создаём переменную нажатия
            when(actionMasked) { // сравниваем с полученным кодом события
                MotionEvent.ACTION_DOWN -> { // Если произошло нажатие на экран
                    touchEvent = touchEventPool.newObject() // создаём новый объект(а именно "return TouchEvent()" )
                    touchEvent.type = touchEvent.TOUCH_DOWN // определяем его тип
                    isTouched[pointerId] = true // определяем существование нажатия
                    // определяем ID нажатия
                    touchEvent.pointer = pointerId
                    // далее определяем координаты нажатия
                    touchX[pointerId] = (event.getX(pointerIndex)*scaleX).toInt()
                    touchEvent.x = touchX[pointerId]
                    touchY[pointerId] = (event.getY(pointerIndex)*scaleY).toInt()
                    touchEvent.y = touchY[pointerId]
                    // созданную и определённую переменную touchEvent добавляем в промежуточный массив
                    touchEventsBuffer.add(touchEvent)
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    touchEvent = touchEventPool.newObject() // создаём новый объект(а именно "return TouchEvent()" )
                    touchEvent.type = touchEvent.TOUCH_DOWN // определяем его тип
                    isTouched[pointerId] = true // определяем существование нажатия
                    // определяем ID нажатия
                    touchEvent.pointer = pointerId
                    // далее определяем координаты нажатия
                    touchX[pointerId] = (event.getX(pointerIndex)*scaleX).toInt()
                    touchEvent.x = touchX[pointerId]
                    touchY[pointerId] = (event.getY(pointerIndex)*scaleY).toInt()
                    touchEvent.y = touchY[pointerId]
                    // созданную и определённую переменную touchEvent добавляем в промежуточный массив
                    touchEventsBuffer.add(touchEvent)
                }
                MotionEvent.ACTION_MOVE -> { // Если произошло движения нажатия
                    val pointerCount : Int = event.pointerCount // Тут почти всё то же самое
                    // Разница только в том, что одновременно обрабатываются все нажатия
                    for (i in 0..pointerCount-1) {
                        touchEvent = touchEventPool.newObject()
                        touchEvent.type = touchEvent.TOUCH_DRAGGED
                        touchEvent.pointer = pointerId
                            touchX[pointerId] = (event.getX(i)*scaleX).toInt()
                        touchEvent.x = touchX[pointerId]
                            touchY[pointerId] = (event.getY(i)*scaleY).toInt()
                        touchEvent.y = touchY[pointerId]
                        touchEventsBuffer.add(touchEvent)
                    }
                    touchEvent.type = touchEvent.TOUCH_DRAGGED
                }
                MotionEvent.ACTION_UP -> { // Если убрали палец с экрана
                    // Всё то же самое, изменились только некоторые значения
                    touchEvent = touchEventPool.newObject()
                    touchEvent.type = touchEvent.TOUCH_UP
                    touchEvent.pointer = pointerId
                    touchX[pointerId] = (event.getX(pointerIndex)*scaleX).toInt()
                    touchEvent.x = touchX[pointerId]
                    touchY[pointerId] = (event.getY(pointerIndex)*scaleY).toInt()
                    touchEvent.y = touchY[pointerId]
                    isTouched[pointerId] = false
                    touchEventsBuffer.add(touchEvent) // i am add this
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    touchEvent = touchEventPool.newObject()
                    touchEvent.type = touchEvent.TOUCH_UP
                    touchEvent.pointer = pointerId
                    touchX[pointerId] = (event.getX(pointerIndex)*scaleX).toInt()
                    touchEvent.x = touchX[pointerId]
                    touchY[pointerId] = (event.getY(pointerIndex)*scaleY).toInt()
                    touchEvent.y = touchY[pointerId]
                    isTouched[pointerId] = false
                    touchEventsBuffer.add(touchEvent) // i am add this
                }
            }
            return true
        }
    }
}