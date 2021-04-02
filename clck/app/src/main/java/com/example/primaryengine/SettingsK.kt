package com.example.primaryengine

import android.util.Log
import com.example.primaryengine.framework.FileIOK
import java.io.*

class SettingsK {
    companion object {
        // тут по сути переменные глобальные, а так же функции для записи и чтения текста в файлах
        //settings vars
        var volume = 1f
        var sound_volume = 1f
        var if_draw_debug_info : Boolean = true
        var fps = 100f

        private var fileSaveName : String = "settings.zm"

        var count : Int = 0
        var count2 : String = ""
        //save vars
        var strs = mutableListOf<String>()
        var current_map_ID : Int = 5
        var temp_str : String = ""

        fun read_data() { // это для удобства форматирования
            // То есть всё необходимое извлекается из строки
            var i : Int = 0
            var ch : CharArray = count2.toCharArray()
            var temp : String = ""
            while(ch[i] != ',') {
                temp+=ch[i]
                i++
            }
            count = Integer.parseInt(temp)
        }

        fun write_data() {

        }

        fun load_save(files : FileIOK) {
            var bf : BufferedReader? = null
            try {
                bf = BufferedReader(InputStreamReader(files.readFile(fileSaveName))) // делаем поток чтения файла
                strs = mutableListOf<String>()
                strs.addAll(bf.readLines())
            }catch (e : IOException) {
            }catch (e : NumberFormatException) {
            }finally {
                try {
                    if (bf != null) bf.close() // закрываем поток
                }catch (e : IOException) {
                }
            }
        }

        fun write_save(file : FileIOK) { // записываем в файл
            delete_file_save(file)
            var bw : BufferedWriter? = null
            try {
                bw = BufferedWriter(OutputStreamWriter(file.writeFile(fileSaveName)))
                strs.forEach {
                    bw.write(it)
                }
            } catch (e : IOException) {

            }finally {
                try {
                    if (bw != null) bw.close()
                } catch (e : IOException) {

                }
            }
        }

        private fun delete_file_save(files : FileIOK) { // опустощаем файл
            var bw : BufferedWriter? = null
            try {
                bw = BufferedWriter(OutputStreamWriter(files.writeFile(fileSaveName)))
                bw.write("")
            } catch (e : IOException) {
            }finally {
                try {
                    if (bw != null) bw.close()
                } catch (e : IOException) {
                }
            }
        }


    }
}