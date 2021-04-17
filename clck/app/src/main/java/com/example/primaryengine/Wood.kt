package com.example.primaryengine

class Wood (val ID : Int, diff : Float) : Drawing_object() {
    var HP = ((1+Math.random()*4)*diff).toInt()
    var maxHP = 0

    init {
        image = AssetsK.tree
        y = 710
        y_original=y
        x = 730
        x_original=x
        width = 106
        height = 116
        when (ID) {
            0 -> {
                HP = 0
            }
            1 -> {
                HP *= 4
                srcX = 0
                srcY = 0
                srcWidth = 544
                srcHeight = 638
            }
            2 -> {
                HP *= 9
                srcX = 0
                srcY = 0
                srcWidth = 544
                srcHeight = 638
            }
        }
        maxHP = HP
    }

    class Fruit (ID : Int, diff : Float) : Drawing_object() {
        var exp = diff.toInt()/2 + 10
        var image2 = AssetsK.oneEmptyPixel // погрызан
        var ifDrop = false
        var vectorX = 0.0f
        var t_vectorX = 0.0f
        var vectorY = 0.0f
        var t_vectorY = 0.0f
        init {
            width = 15
            height = 15
            when (ID) {
                1 -> {
                    image = AssetsK.apple
                    image2 = AssetsK.apple_stub
                    exp += 1
                    srcX = 0
                    srcY = 0
                    srcWidth = 96
                    srcHeight = 117
                }
                2 -> {
                    image = AssetsK.pear
                    image2 = AssetsK.pear_stub
                    exp += 3
                    srcX = 0
                    srcY = 0
                    srcWidth = 94
                    srcHeight = 139
                }
            }
        }

    }
}
