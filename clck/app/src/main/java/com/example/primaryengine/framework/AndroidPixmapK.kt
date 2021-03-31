package com.example.primaryengine.framework

import android.graphics.Bitmap

class AndroidPixmapK(val bitmap: Bitmap, override var imageID: Int) : PixmapK {
    // Картинка
    override fun dispose() {bitmap.recycle()}
}