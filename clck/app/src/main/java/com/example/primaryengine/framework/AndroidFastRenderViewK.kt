package com.example.primaryengine.framework

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.view.SurfaceHolder
import android.view.SurfaceView

class AndroidFastRenderViewK(val game : AndroidGameK, val frameBuffer : Bitmap) : SurfaceView(game), Runnable {
    // Отрисовщик
    lateinit var renderThread : Thread // Поток
    lateinit var holderr : SurfaceHolder // Изображение
    @Volatile
    var running = false

    fun AndroidFastRenderViewK(game : AndroidGameK, frameBuffer : Bitmap) {
        // не стоит думать над функциями, которые нигде не используются
        holderr = holder // получаем holder из активности
    }

    fun resume() {
        renderThread = Thread(this) // заносим поток активности в renderThread
        renderThread.start() // Запускаем поток
        running = true // помечаем для себя
    }

    override fun run() {
        val dstRect = Rect() // Это прямоугольник
        var startTime : Long = System.nanoTime() // отмечаем время старта
        while (running) { // запускаем цикл отрисовки изображения и просчёта некоторых переменных
            if(!holder.surface.isValid) continue

            val deltaTime : Float = (System.nanoTime()-startTime) / 1000000000.0F // отмечаем сколько прошло времени
            startTime = System.nanoTime() // занова отмечаем время старта

            game.screenn.update(deltaTime) // это функция для расчётов
            game.screenn.present(deltaTime) // это функция для рисования
            // обратите внимание на то, где вызываются эти функции. В game.screenn
            val canvas : Canvas = holder.lockCanvas() // запираем канвас
            // далее рисуем в нём и отпираем
            // нз почему так, но так надо и так работает
            canvas.getClipBounds(dstRect)
            canvas.drawBitmap(frameBuffer, null, dstRect, null)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun pause() { // останавливаем поток
        running = false
        while (true) {
            try {
                renderThread.join()
                break
            }catch (e : InterruptedException) {

            }
        }
    }
}