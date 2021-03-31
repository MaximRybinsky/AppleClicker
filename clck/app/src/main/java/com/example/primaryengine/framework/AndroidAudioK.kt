package com.example.primaryengine.framework

import android.app.Activity
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import java.io.IOException

class AndroidAudioK(activity : Activity) : AudioK {
    var assets : AssetManager // файловая система, если по-простому
    var soundPoolSound : SoundPool // Объект для работы с короткими звуковыми дорожками, которые часто повоторяются. Например, эффекты

    init {
        activity.volumeControlStream = AudioManager.STREAM_MUSIC // Устанавливаем активности контроль громкости звука
        assets = activity.assets // получаем файловую систему
        var attributes : AudioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        // attributes - атрибуты для звуков SoundPool. Простая формальность.
        soundPoolSound = SoundPool.Builder().setMaxStreams(20).setAudioAttributes(attributes).build()
        // определяем soundPool. Устанавливаем максимальное количество одновременно проигрываемых звуков 20.
        // устанавливаем заданные атрибуты
    }

    override fun newMusic(filename: String): MusicK {
        try {
            val asset : AssetFileDescriptor = assets.openFd(filename) // Читаем файл с файловой системы
            return AndroidMusicK(asset) // передаём файл в конструктор, возвращаем объект с музыкой
        }catch (e : IOException) {
            throw RuntimeException("AndroidAudioK.kt:newMusic(...) error")
        }
    }

    override fun newSound(filename: String): SoundK {
        val asset : AssetFileDescriptor = assets.openFd(filename) // открываем файл
        var soundId : Int = soundPoolSound.load(asset, 0) // получаем soundID. Он нам нужен для того, чтобы получить нужный звук
        return AndroidSoundK(soundPoolSound, soundId) // Совмещаем soundPool и soundId в одном классе
    }
}