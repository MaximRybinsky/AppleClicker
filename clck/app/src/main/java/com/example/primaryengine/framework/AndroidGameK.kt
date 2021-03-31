package com.example.primaryengine.framework

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.Window
import android.view.WindowManager

abstract class AndroidGameK : Activity(), GameK {
    // Здесь мы собираем всё необходимое:
    // Отрисовщика
    override lateinit var renderView : AndroidFastRenderViewK
    // Инструменты для рисования
    override lateinit var graphics : GraphicsK
    // Звук
    override lateinit var audio : AudioK
    // Непосредственно сам экран
    override lateinit var screenn : ScreenK
    // ввод данных с помощью экрана и клавиатуры
    override lateinit var input : InputK
    // поток ввода/вывода файлов
    override lateinit var fileIO : FileIOK

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        // делаем Во_Весь_Экран
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // проверяем экранную ориентацию(вертикально-горизонтально)
        // и устанавливаем разрешение экрана, с которым будем работать
        var isLandscape : Boolean = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        var frameBufferWidth = if (isLandscape) 1280 else 720
        var frameBufferHeight = if (isLandscape) 720 else 1280
        var frameBuffer : Bitmap = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Bitmap.Config.RGB_565)
        // находим реальное значение экрана смартфона
        // и устанавливаем соотношение размеров экранов
        val displayy : Display = windowManager.defaultDisplay
        var size : Point = Point()
        displayy.getSize(size)
        var scaleX : Float = frameBufferWidth.toFloat() / size.x.toFloat()
        var scaleY : Float = frameBufferHeight.toFloat() / size.y.toFloat()
        // инициализируем всё необходимое
        renderView = AndroidFastRenderViewK(this, frameBuffer)
        graphics = AndroidGraphicsK(assets, frameBuffer)
        fileIO = AndroidFileIOK(assets)
        audio = AndroidAudioK(this)
        input = AndroidInputK(this, renderView, scaleX, scaleY)
        screenn = getStartScreen()
        // устанавливаем объект, который будет отображаться.
        setContentView(renderView)
    }

    override fun onResume() { // Выполняется каждый раз, когда открывается окно. Будь оно впервые создано или восстановленно из свёрнутого состояния.
        super.onResume()
        screenn.resume()
        renderView.resume()
    }

    override fun onPause() { // Выполняется каждый раз, когда сворачивается окно. Будь оно свёрнуто или отключено.
        super.onPause()
        renderView.pause()
        screenn.pause()
        if (isFinishing) screenn.dispose()
    }

    override fun setScreen(screen2: ScreenK) { // Устанавливает screen2 в качестве текущего экрана отображения изображения
        if (screen2 == null) throw IllegalArgumentException("screen2 = null")
        screenn.pause()
        screenn.dispose()
        screen2.resume()
        screen2.update(0F)
        screenn=screen2
    }
}