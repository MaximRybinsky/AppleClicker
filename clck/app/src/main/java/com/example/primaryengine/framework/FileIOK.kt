package com.example.primaryengine.framework

import java.io.InputStream
import java.io.OutputStream

interface FileIOK {
    fun readAsset(filename : String) : InputStream

    fun readFile(filename : String) : InputStream

    fun writeFile(filename : String) : OutputStream
}