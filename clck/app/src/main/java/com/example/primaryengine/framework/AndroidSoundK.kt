package com.example.primaryengine.framework

import android.media.SoundPool

class AndroidSoundK(val soundPool: SoundPool, val soundId : Int) : SoundK {
    // По сути, AndroidSoundK - это soundPool и soundId, и пара функций
    override fun play(volume: Float) {
        soundPool.play(soundId, volume, volume, 0, 0, 1F)
    }

    override fun dispose() {
        soundPool.unload(soundId)
    }
}