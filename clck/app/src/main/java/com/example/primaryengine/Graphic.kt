package com.example.primaryengine

import com.example.primaryengine.framework.PixmapK

open abstract class Drawing_object {
    open var image : PixmapK = AssetsK.oneEmptyPixel
    open var animation = Animation()
    open var y : Int = 0
    open var y_original : Int = 0
    open var x : Int = 0
    open var x_original : Int = 0
    open var width : Int = 0
    open var height : Int = 0
    open var srcX : Int = 0
    open var srcY : Int = 0
    open var srcWidth : Int = 0
    open var srcHeight : Int = 0
    open var alpha : Int = 255
}

open class Animation {
    var animations = mutableListOf<Frame>()
    private var count_of_animations = 0

    fun addAnimation(sprite : PixmapK) {
        count_of_animations++
        animations.add(Frame(sprite))
    }
}

class Frame(var image : PixmapK) {
    var frames = mutableListOf<Image>()
    private var count_of_images : Int = 0
    var current_frame : Int = 0
    var current_image = 0
    get() {
        var temp = current_frame
        var i = 0
        for (frame in frames) {
            i=0
            while (i<frame.frames_for_image) {
                if (temp==0) return frame.number_of_image
                temp--
                i++
            }
        }
        return current_image
    }
    var count_of_frames : Int = 0
    var playing = false

    class Image(var number_of_image : Int, var srcX : Int, var srcY : Int, var srcWidth : Int, var srcHeight : Int, var frames_for_image : Int)

    fun addFrame(_srcX : Int, _srcY : Int, _srcWidth : Int, _srcHeight : Int, _frames_for_image : Int) {
        count_of_images++
        frames.add(Image(count_of_images - 1, _srcX, _srcY, _srcWidth, _srcHeight, _frames_for_image))
        count_of_frames = count_of_frames + _frames_for_image
    }
}

class SpritesDsts {
    val mainSpriteImage = AssetsK.mainSprite
    val mainSprite = mutableListOf(mutableListOf(0, 0, 160, 180),//apple
        mutableListOf(160, 0, 160, 180))//aplecot
}