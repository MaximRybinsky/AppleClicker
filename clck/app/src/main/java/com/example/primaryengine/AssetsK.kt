package com.example.primaryengine

import android.graphics.Typeface
import com.example.primaryengine.framework.MusicK
import com.example.primaryengine.framework.PixmapK
import com.example.primaryengine.framework.SoundK

class AssetsK {
companion object {
    // по сути, это склад файлов, которые мы используем
    lateinit var oneEmptyPixel : PixmapK // -1
    lateinit var hero : PixmapK //1
    lateinit var hero_sprite : PixmapK //3 // 0;0   438;0   876;0   1312;0  438x353
    lateinit var apple : PixmapK //4
    lateinit var apple_stub : PixmapK //5
    lateinit var pear : PixmapK //6
    lateinit var pear_stub : PixmapK //7
    lateinit var tree : PixmapK //8
    lateinit var b_close : PixmapK //9
    lateinit var b_close_off : PixmapK //10
    lateinit var b_music : PixmapK //11
    lateinit var b_music_off : PixmapK //12
    lateinit var b_settings : PixmapK //13
    lateinit var b_settings_off : PixmapK //14
    lateinit var b_sound : PixmapK //15
    lateinit var b_sound_off : PixmapK //16
    lateinit var settings_window : PixmapK //17
    lateinit var background : PixmapK //18
    lateinit var bg_cloud_1 : PixmapK //19
    lateinit var bg_cloud_2 : PixmapK //20
    lateinit var bg_cloud_3 : PixmapK //21
    lateinit var bg_sun : PixmapK //22
    lateinit var i_arm : PixmapK //23
    lateinit var i_cloth : PixmapK //24
    lateinit var i_hat : PixmapK //25
    lateinit var i_leg : PixmapK //26
    lateinit var i_stomach : PixmapK //27
    lateinit var skills : PixmapK //28
    lateinit var b_language : PixmapK //29
    lateinit var redBull : PixmapK // 30
    lateinit var redBull_off : PixmapK // 31
    lateinit var x2 : PixmapK // 32
    //М. добавил новые фрукты и деревья
    lateinit var cherry : PixmapK // 33
    lateinit var cherry_stub : PixmapK // 34
    lateinit var orange : PixmapK // 35
    lateinit var orange_stub : PixmapK // 36
    lateinit var peach : PixmapK // 37
    lateinit var peach_stub : PixmapK // 38
    lateinit var plum : PixmapK // 39
    lateinit var plum_stub : PixmapK // 40
    lateinit var tree_cherry : PixmapK // 41
    lateinit var tree_orange : PixmapK // 42
    lateinit var tree_peach : PixmapK // 43
    lateinit var tree_plum : PixmapK // 44
    lateinit var tree_pear : PixmapK // 45


    lateinit var goethe : Typeface

    lateinit var hrum : SoundK
    lateinit var crack1 : SoundK
    lateinit var crack2 : SoundK
    lateinit var pop1 : SoundK
    lateinit var pop2 : SoundK
    lateinit var shake1 : SoundK
    lateinit var shake2 : SoundK
    lateinit var soda : SoundK
    lateinit var SaS_RibbitKing : MusicK

    fun getPixmap(index : Int) : PixmapK {
        return when(index) {
            -1 -> oneEmptyPixel
            1 -> hero
            3 -> hero_sprite
            4 -> apple
            5 -> apple_stub
            6 -> pear
            7 -> pear_stub
            8 -> tree
            9 -> b_close
            10 -> b_close_off
            11 -> b_music
            12 -> b_music_off
            13 -> b_settings
            14 -> b_settings_off
            15 -> b_sound
            16 -> b_sound_off
            17 -> settings_window
            18 -> background
            19 -> bg_cloud_1
            20 -> bg_cloud_2
            21 -> bg_cloud_3
            22 -> bg_sun
            23 -> i_arm
            24 -> i_cloth
            25 -> i_hat
            26 -> i_leg
            27 -> i_stomach
            28 -> skills
            29 -> b_language
            30 -> redBull
            31 -> redBull_off
            33 -> cherry
            34 -> cherry_stub
            35 -> orange
            36 -> orange_stub
            37 -> peach
            38 -> peach_stub
            39 -> plum
            40 -> plum_stub
            41 -> tree_cherry
            42 -> tree_orange
            43 -> tree_peach
            44 -> tree_plum
            45 -> tree_pear
            else -> oneEmptyPixel
        }
    }

//    fun getSound(index : Int) : SoundK {
//        return when(index) {
//
//            else ->
//        }
//    }
}
}