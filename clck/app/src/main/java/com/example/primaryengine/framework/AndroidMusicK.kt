package com.example.primaryengine.framework

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import java.io.IOException
import java.lang.IllegalStateException
import java.lang.RuntimeException

class AndroidMusicK(asset : AssetFileDescriptor) : MusicK, MediaPlayer.OnCompletionListener {

    override var mediaPlayer : MediaPlayer = MediaPlayer() // mediaPlayer выполняет основную функцию по работе с музыкой
    override var isPrepared = false // Вспомогаетльная информативная функция

    init {
        try {
            mediaPlayer.setDataSource(asset.fileDescriptor, asset.startOffset, asset.length) // устанавливаем файл в mediaPlayer
            mediaPlayer.prepare() // подготавливаем mediaPlayer, типо загружаем его в память
            isPrepared = true // помечаем для себя
            mediaPlayer.setOnCompletionListener(this) // устанавливаем слушатель на текущую активность
        } catch (e : IOException) {
            throw RuntimeException("Couldn't load music")
        }
    }

    override fun play() { // функция воспроизведения музыки
        if (mediaPlayer.isPlaying) return // Если уже играет музыка, то не запускаем ничего

        try {
            synchronized(this) {
                if (!isPrepared) mediaPlayer.prepare() // Подготавливаем, если не подготовили
                mediaPlayer.start() // включаем
            }
        } catch(e : IllegalStateException) {
        } catch(e : IOException) {
        }
    }

    override fun stop() {
        synchronized(this) {
            mediaPlayer.stop() // останавливаем
            isPrepared = false
        }
    }

    override fun pause() {
        synchronized(this) {
            mediaPlayer.pause() // приостанавливаем
        }
    }

    override fun isStopped(): Boolean {
        return !isPrepared // Получаем информацию
    }

    override fun dispose() {
        if (mediaPlayer.isPlaying) mediaPlayer.stop() // останавливаем, если надо
        mediaPlayer.release() // выгружаем из памяти
    }

    override fun onCompletion(mp: MediaPlayer?) {
        // ничего не делаем по завершению музыки. Нам и не надо
    }
}