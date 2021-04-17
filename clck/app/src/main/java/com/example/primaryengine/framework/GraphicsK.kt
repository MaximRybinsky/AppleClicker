package com.example.primaryengine.framework

import android.content.res.AssetManager
import android.graphics.Paint
import android.graphics.Typeface

interface GraphicsK {
    var assets : AssetManager
    var paint : Paint

    enum class PixmapFormat {
        ARGB8888, RGB565
    }

    fun clean(a : Int, r : Int, g : Int, b : Int)

    fun drawPixel(x : Int, y : Int, color : Int)

    fun drawLine(x : Int, y : Int, x2 : Int, y2 : Int, color : Int)

    fun drawRect(x : Int, y : Int, width : Int, height : Int, color : Int)

    fun drawPixmap(pixmap : PixmapK, x : Int, y : Int, width : Int, height : Int, srcX : Int, srcY : Int, srcWidth : Int, srcHeight : Int)

    fun drawPixmap(pixmap: PixmapK, x: Int, y: Int, width : Int, height : Int, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int, alpha : Int)

    fun drawPixmap(pixmap: PixmapK, x: Int, y: Int, width : Int, height : Int, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int, rotate_angle : Float, alpha : Int)

    fun drawPixmap(pixmap : PixmapK, x : Int, y : Int, width : Int, height : Int, alpha : Int)

    fun drawPixmap(pixmap : PixmapK, x : Int, y : Int, width : Int, height : Int)

    fun drawPixmap(pixmap : PixmapK, x : Int, y : Int)

    fun drawText(font : Typeface, text : String, x : Float, y : Float, textSize : Float, color : Int)

    fun drawText(text: String, x: Float, y: Float, textSize: Float, color: Int)

    fun drawText(text: String, x: Float, y: Float, textSize: Float, color: Int, alpha : Int)
    fun newPixmap(fileName: String, imageID: Int): PixmapK
}