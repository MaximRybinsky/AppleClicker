package com.example.primaryengine.framework

import android.content.res.AssetManager
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class AndroidFileIOK(val assets : AssetManager) : FileIOK {
    // Поток ввода/вывода файла
    var externalStoragePath : String // Путь

    init {
        externalStoragePath = Environment.getDataDirectory().absolutePath + File.separator // получаем путь
    }

    override fun readAsset(filename: String): InputStream {
        return assets.open(filename) // читаем файл с помощью файлового менеджера
    }

    override fun readFile(filename: String): InputStream {
        return FileInputStream(externalStoragePath + filename) // читаем файл с помощью потока
    }

    override fun writeFile(filename: String): OutputStream {
        return FileOutputStream(externalStoragePath + filename) // записываем файл с помощью потока
    }
}