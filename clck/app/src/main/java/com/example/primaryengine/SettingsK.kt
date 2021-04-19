package com.example.primaryengine

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.primaryengine.framework.AndroidGameK

class SettingsK {
    companion object {
        // тут по сути переменные глобальные, а так же функции для записи и чтения текста в файлах
        //settings vars
        var volume = 1f
        var sound_volume = 1f
        var if_draw_debug_info : Boolean = true
        var fps = 100f
        // переменная для сохранения данных
        lateinit var prefs : SharedPreferences
        lateinit var edit : SharedPreferences.Editor
        // инициализация переменной с помощью класса AndroidGameK
        fun init_prefs(game : AndroidGameK) {
            prefs = game.getSharedPreferences(fileSaveName, Context.MODE_PRIVATE)
            edit = prefs.edit()
        }

        private var fileSaveName : String = "settings.zm"

        fun load_save(hero: Hero, mainWood: Wood, secondWood: Wood, energ : Wood.Energ) : MutableList<Float> {
            var list = mutableListOf<Float>()
            // Загружаем настройки
            volume = prefs.getFloat("volume", 1f)
            sound_volume = prefs.getFloat("sound_volume", 1f)
            if_draw_debug_info = prefs.getBoolean("if_draw_debug_info", false)
            fps = prefs.getFloat("fps", 100f)
            // Загружаем героя
            hero.damage = prefs.getInt("damage", 1)
            hero.woodsCounter = prefs.getInt("woodscounter", 0)
            hero.kcal = prefs.getInt("kcal", 0)
            hero.arm1 = prefs.getInt("arm1", 1)
            hero.arm1_cost = prefs.getInt("arm1_cost", 1000)
            hero.arm1_buff = prefs.getInt("arm1_buff", 1)
            hero.arm2 = prefs.getInt("arm2", 1)
            hero.arm2_cost = prefs.getInt("arm2_cost", 1000)
            hero.arm2_buff = prefs.getInt("arm2_buff", 1)
            hero.stomach = prefs.getInt("stomach", 1)
            hero.stomach_cost = prefs.getInt("stomach_cost", 3000)
            hero.stomach_buff = prefs.getInt("stomach_buff", 5)
            hero.leg1 = prefs.getInt("leg1", 1)
            hero.leg1_cost = prefs.getInt("leg1_cost", 1500)
            hero.leg1_buff = prefs.getInt("leg1_buff", 3)
            hero.leg2 = prefs.getInt("leg2", 1)
            hero.leg2_cost = prefs.getInt("leg2_cost", 1500)
            hero.leg2_buff = prefs.getInt("leg2_buff", 3)
            hero.cloth = prefs.getInt("cloth", 1)
            hero.cloth_cost = prefs.getInt("cloth_cost", 10000)
            hero.cloth_buff = prefs.getInt("cloth_buff", 15)
            hero.hat = prefs.getInt("hat", 1)
            hero.hat_cost = prefs.getInt("hat_cost", 15000)
            hero.hat_buff = prefs.getInt("hat_buff", 20)
            // Загружаем сложность
            list.add(prefs.getFloat("diff", 1.0f))
            // Загружаем главное дерево
            mainWood.ID = prefs.getInt("mID", 1)
            mainWood.HP = prefs.getInt("mHP", 0)
            mainWood.HP_step = prefs.getInt("mHP_step", 10)
            // Загружаем количество фруктов на главном дереве
            list.add(prefs.getInt("mcount", 0).toFloat())
            // Загружаем второстепенное дерево
            secondWood.ID = prefs.getInt("sID", 1)
            secondWood.HP = prefs.getInt("sHP", 0)
            secondWood.HP_step = prefs.getInt("sHP_step", 10)
            // Загружаем количество фруктов на второстепенном дереве
            list.add(prefs.getInt("scount", 0).toFloat())
            // Загружаем энергетик
            energ.exp = prefs.getInt("e_exp", 120)
            energ.energ_boost = prefs.getFloat("e_boost", 1.0f)
            energ.energ_dead_time_const = prefs.getInt("e_dead_time_const", 60)
            energ.energ_dead_time = prefs.getInt("e_dead_time", 0)
            energ.energ_timer = prefs.getInt("e_timer", 0)
            energ.energ_timer_const = prefs.getInt("timer_const", 15)

            return list
        }

        fun write_save(hero: Hero, diff : Float, mainWood: Wood, secondWood: Wood, countMainFruits : Int, countSecondFruits : Int, energ : Wood.Energ) { // записываем в файл
            // Сохраняем настройки
            edit.putFloat("volume", volume)
            edit.putFloat("sound_volume", sound_volume)
            edit.putBoolean("if_draw_debug_info", if_draw_debug_info)
            edit.putFloat("fps", fps)
            // Сохраняем героя
            edit.putInt("damage", hero.damage)
            edit.putInt("woodscounter", hero.woodsCounter)
            edit.putInt("kcal", hero.kcal)
            edit.putInt("arm1", hero.arm1)
            edit.putInt("arm1_cost", hero.arm1_cost)
            edit.putInt("arm1_buff", hero.arm1_buff)
            edit.putInt("arm2", hero.arm2)
            edit.putInt("arm2_cost", hero.arm2_cost)
            edit.putInt("arm2_buff", hero.arm2_buff)
            edit.putInt("stomach", hero.stomach)
            edit.putInt("stomach_cost", hero.stomach_cost)
            edit.putInt("stomach_buff", hero.stomach_buff)
            edit.putInt("leg1", hero.leg1)
            edit.putInt("leg1_cost", hero.leg1_cost)
            edit.putInt("leg1_buff", hero.leg1_buff)
            edit.putInt("leg2", hero.leg2)
            edit.putInt("leg2_cost", hero.leg2_cost)
            edit.putInt("leg2_buff", hero.leg2_buff)
            edit.putInt("cloth", hero.cloth)
            edit.putInt("cloth_cost", hero.cloth_cost)
            edit.putInt("cloth_buff", hero.cloth_buff)
            edit.putInt("hat", hero.hat)
            edit.putInt("hat_cost", hero.hat_cost)
            edit.putInt("hat_buff", hero.hat_buff)
            // Сохраняем сложность
            edit.putFloat("diff", diff)
            // Сохраняем главное дерево
            edit.putInt("mID", mainWood.ID)
            edit.putInt("mHP", mainWood.HP)
            edit.putInt("mHP_step", mainWood.HP_step)
            // Сохраняем количество фруктов на главном дереве
            edit.putInt("mcount", countMainFruits)
            // Сохраняем второстепенное дерево
            edit.putInt("sID", secondWood.ID)
            edit.putInt("sHP", secondWood.HP)
            edit.putInt("sHP_step", secondWood.HP_step)
            // Сохраняем количество фруктов на второстепенном дереве
            edit.putInt("scount", countSecondFruits)
            // Сохраняем энергетик
            edit.putInt("e_exp", energ.exp)
            edit.putFloat("e_boost", energ.energ_boost)
            edit.putInt("e_dead_time_const", energ.energ_dead_time_const)
            edit.putInt("e_dead_time", energ.energ_dead_time)
            edit.putInt("e_timer", energ.energ_timer)
            edit.putInt("e.timer_const", energ.energ_timer_const)

            // Сохраняем файл
            edit.apply()
        }

        fun delete_save() {
            edit.clear()
            edit.apply()
        }
    }
}