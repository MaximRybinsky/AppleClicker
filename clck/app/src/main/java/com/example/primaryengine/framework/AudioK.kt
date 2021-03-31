package com.example.primaryengine.framework

interface AudioK {
    fun newMusic(filename : String) : MusicK

    fun newSound(filename: String) : SoundK
}