package com.example.primaryengine.framework

abstract class ScreenK(val game : GameK) {

    abstract fun update(deltaTime : Float)

    abstract fun present(deltaTime : Float)

    abstract fun pause()

    abstract fun resume()

    abstract fun dispose()
}