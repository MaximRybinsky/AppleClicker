package com.example.primaryengine.framework

interface GameK {

    var renderView : AndroidFastRenderViewK
    var graphics : GraphicsK
    var audio : AudioK
    var screenn : ScreenK
    var input : InputK
    var fileIO : FileIOK

    fun setScreen(screen : ScreenK)

    fun getStartScreen() : ScreenK
}