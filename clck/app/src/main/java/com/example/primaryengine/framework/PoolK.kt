package com.example.primaryengine.framework

class PoolK<T>(val factory : PoolObjectFactory<T>, val maxSize : Int) {
    interface PoolObjectFactory<T> { // Определяем интерфейс с функцией
        fun createObject() : T       // это нам надо чисто для того, чтобы потом не запутаться и ничего не забыть
    }                                // если есть другая причина, то она мне неизвестна

    var freeObject : MutableList<T> = ArrayList<T>(maxSize) // это для создания переменной: выполняет регулировочную и вспомогательную функции

    fun newObject() : T {  // Функция создания нового объекта класса T. То есть любого класса.
        var objectasd : T? = null  // переменная, которая будет возвращаться
        if (freeObject.size == 0) { // Если у нас нет freeObject("свободного объекта")
            objectasd = factory.createObject() // мы создаём новый объект
        } else { // Иначе
            objectasd = freeObject.removeAt(freeObject.size-1) // Мы забираем объект из памяти, стирая его из freeObject
        }               // P.S.: мб нет, можно погуглить "MutableList removeAt"
        return objectasd
    }

    fun free(objectTemp : T) { // Добавляем в freeObject объект objectTemp(Temporary object)
        if (freeObject.size < maxSize) freeObject.add(objectTemp)
    }

    // Вообще, Pool нам нужен для того, чтобы не загружать память лишний раз.
    // Нужна переменная - взяли её - взяли новую - взяли новую - ...
    // Pool позволяет зациклить данный процесс, сведя к минимуму затраты оперативной памяти
    // Нужна переменная - взяли её - перезаписали её - ...
    // Отличительной особенностью данного метода является неограниченное количество объектов, то есть переменных.
    // P.S.: описание неполное, но достаточное для понимания.
}