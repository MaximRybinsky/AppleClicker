package com.example.primaryengine

class Wood (var ID : Int, diff : Float) : Drawing_object() {
    var HP = ((4+Math.random()*10)*diff).toInt()
    var HP_step = 0

    init {
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
                image = AssetsK.tree
                HP *= 2
            }
            2 -> {
                image = AssetsK.tree_pear
                HP *= 2
            }
            3 -> {
                image = AssetsK.tree_cherry
                HP *= 2
            }
            4 -> {
                image = AssetsK.tree_orange
                HP *= 2
            }
            5 -> {
                image = AssetsK.tree_peach
                HP *= 2
            }
            6 -> {
                image = AssetsK.tree_plum
                HP *= 2
            }
        }
    }

    open class Fruit (ID : Int, diff : Float) : Drawing_object() {
        var exp = (diff.toInt()*2.2 + 10).toInt()
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
                    exp += 50
                }
                2 -> {
                    image = AssetsK.pear
                    image2 = AssetsK.pear_stub
                    exp += 50
                }
                3 -> {
                    image = AssetsK.cherry
                    image2 = AssetsK.cherry_stub
                    exp += 50
                }
                4 -> {
                    image = AssetsK.orange
                    image2 = AssetsK.orange_stub
                    exp += 50
                }
                5 -> {
                    image = AssetsK.peach
                    image2 = AssetsK.peach_stub
                    exp += 50
                }
                6 -> {
                    image = AssetsK.plum
                    image2 = AssetsK.plum_stub
                    exp += 50
                }
            }
        }

    }

    class Energ(ID: Int, diff: Float) : Fruit(ID, diff) {
        var energ_boost = 1.0f
        var energ_dead_time_const = 60
        var energ_dead_time = 0
        var energ_timer_const = 15
        var energ_timer = 0

        init {
            when(ID) {
                -15 -> {
                    image = AssetsK.redBull
                    image2 = AssetsK.redBull_off
                    exp = 150
                    x = 550
                    y = 1020
                    width = 149
                    height = 170
                }
            }
        }
    }
}

class Cloud(ID: Int) : Drawing_object() {
    var speed = 0

    init {
        x = 900
        y = 50 + (Math.random()*300).toInt()
        width = (184.0f*(0.75 + Math.random()*0.5)).toInt()
        height = (88.0f*(0.75 + Math.random()*0.5)).toInt()
        speed = 1 + (Math.random()*4).toInt()

        when (ID) {
            1 -> {
                image = AssetsK.bg_cloud_1
            }
            2 -> {
                image = AssetsK.bg_cloud_2
            }
            3 -> {
                image = AssetsK.bg_cloud_3
            }
        }
    }
}

class Sun : Drawing_object() {
    var srcX : Int = 0
    var srcY : Int = 0
    var srcWidth : Int = 0
    var srcHeight : Int = 0
    open var alpha : Int = 255

    var angle = 0f

    init {
        image = AssetsK.bg_sun
        x = 500
        y = 100
        width = 144
        height = 144
        srcWidth = 144
        srcHeight = 144
    }
}
