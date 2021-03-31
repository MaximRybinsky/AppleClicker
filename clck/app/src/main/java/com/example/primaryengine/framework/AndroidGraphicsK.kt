package com.example.primaryengine.framework

import android.content.res.AssetManager
import android.graphics.*
import android.util.Log
import com.example.primaryengine.AssetsK
import java.io.*

class AndroidGraphicsK(override var assets : AssetManager, var frameBuffer : Bitmap) : GraphicsK {
    // Инструменты для рисования
    var canvas : Canvas = Canvas(frameBuffer) // Полотно
    override var paint : Paint = Paint() // Кисть
    var srcRect : Rect = Rect() // 2 прямоугольных рамки
    var dstRect : Rect = Rect()

    override fun newPixmap(fileName: String, imageID : Int) : PixmapK { // Средство для получения изображения из файла
        var am : AssetManager = assets // получаем файловый менеджер
        var inputStream : InputStream? = null // поток ввода файла
        var bitmap : Bitmap? = null // изображение
        try {
            inputStream = am.open(fileName) // открываем файл потоком
            bitmap = BitmapFactory.decodeStream(inputStream) // преобразуем поток данных в изображение
        }catch (e : IOException) {

        }finally {
            if (inputStream != null) try {
                inputStream.close() // закрываем поток
            } catch (e : IOException) {}
        }
        if (bitmap != null) {
            return AndroidPixmapK(bitmap, imageID) // возвращаем получение изображение в виде Pixmap
        }
        throw RuntimeException("all null")
    }
    // Далее идут функции для рисования

    override fun clean(a : Int, r : Int, g : Int, b : Int) { // закрашиваем весь экран цветом color
        canvas.drawARGB(a,r,g,b)
    }

    override fun drawPixel(x: Int, y: Int, color: Int) { // рисуем один пиксел
        paint.color = color
        canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
    }

    override fun drawLine(x: Int, y: Int, x2: Int, y2: Int, color: Int) { // рисуем линию
        paint.color = color
        canvas.drawLine(x.toFloat(), y.toFloat(), x2.toFloat(), y2.toFloat(), paint)
    }

    override fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Int) { // рисуем прямоугольник
        paint.color = color
        paint.style = Paint.Style.FILL // Область прямоугольника будет закрашена
        canvas.drawRect(x.toFloat(), y.toFloat(), (x+width-1).toFloat(), (y+height-1).toFloat(), paint)
    }

    override fun drawPixmap(pixmap: PixmapK, x: Int, y: Int, width : Int, height : Int, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int) {
        // Определяем прямоугольную область на изображении
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX+srcWidth-1;
        srcRect.bottom = srcY+srcHeight-1;
        // Определяем прямоугольную область на экране
        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x+width-1;
        dstRect.bottom= y+height-1;
        // Берём прямоугольник с изображения и вставляем его в прямоугольник с экрана
        canvas.drawBitmap((pixmap as AndroidPixmapK).bitmap, srcRect, dstRect, null);
    }

    override fun drawPixmap(pixmap: PixmapK, x: Int, y: Int, width : Int, height : Int, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int, alpha : Int) {
        paint.alpha = alpha
        // Определяем прямоугольную область на изображении
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX+srcWidth-1;
        srcRect.bottom = srcY+srcHeight-1;
        // Определяем прямоугольную область на экране
        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x+width-1;
        dstRect.bottom= y+height-1;
        // Берём прямоугольник с изображения и вставляем его в прямоугольник с экрана
        canvas.drawBitmap((pixmap as AndroidPixmapK).bitmap, srcRect, dstRect, paint);
        paint.alpha = 255
    }

    override fun drawPixmap(pixmap: PixmapK, x: Int, y: Int, width: Int, height: Int, alpha: Int) {
        var dstRect = Rect(x, y, x+width-1, y+height-1)
        paint.alpha = alpha
        // Просто помещаем всё изоражение на экран, изменяя прозрачность alpha
        canvas.drawBitmap((pixmap as AndroidPixmapK).bitmap, null, dstRect, paint)
        paint.alpha = 255
    }

    override fun drawPixmap(pixmap: PixmapK, x: Int, y: Int, width: Int, height: Int) {
        var dstRect = Rect(x, y, x+width-1, y+height-1)
        // просто помещаем изображение на экран
        canvas.drawBitmap((pixmap as AndroidPixmapK).bitmap, null, dstRect, paint)
    }

    override fun drawPixmap(pixmap: PixmapK, x: Int, y: Int) {
        // помещаем изображение в точку (x,y) без изменения размеров
        canvas.drawBitmap((pixmap as AndroidPixmapK).bitmap, x.toFloat(), y.toFloat(), null)
    }

    override fun drawText(font: Typeface, text: String, x: Float, y: Float, textSize: Float, color: Int) {
        paint.textSize = textSize
        paint.setTypeface(font)
        paint.color = color
        canvas.drawText(text, x, y, paint)
    }

    override fun drawText(text: String, x: Float, y: Float, textSize: Float, color: Int) {
        paint.textSize = textSize
        paint.color = color
        canvas.drawText(text, x, y, paint)
    }

    override fun drawText(text: String, x: Float, y: Float, textSize: Float, color: Int, alpha : Int) {
        paint.textSize = textSize
        paint.color = color
        paint.alpha = alpha
        canvas.drawText(text, x, y, paint)
        paint.alpha = 255
    }

    fun getWidth() : Int {
        return frameBuffer.width
    }

    fun getHeight() : Int {
        return frameBuffer.height
    }

}