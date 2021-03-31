package com.example.primaryengine.framework

import android.media.MediaPlayer

interface MusicK {

    var mediaPlayer : MediaPlayer
    var isPrepared : Boolean

    fun play()

    fun stop()

    fun pause()

    fun isStopped(): Boolean // Why ?

    fun dispose()
}