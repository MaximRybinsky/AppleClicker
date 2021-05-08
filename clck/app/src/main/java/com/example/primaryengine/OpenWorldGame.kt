package com.example.primaryengine

import android.graphics.Color
import android.provider.Settings
import android.util.Log
import com.example.primaryengine.framework.*

class OpenWorldGame(game : GameK) : ScreenK(game) {
    // engine vars
    private var stTime : Float = System.nanoTime() / 1000000000.0f
    private var currTime : Float = System.nanoTime() / 1000000000.0f
    // fps vars
    private var startTime : Float = System.nanoTime() / 1000000000.0f
    private var currentTime : Float = System.nanoTime() / 1000000000.0f
    private var tickTime = 0f
    private var s_fps = SettingsK.fps
    private val tick : Float = 1/s_fps
    private var fps = 0
    private var fps_temp = fps
    private var dfps = 0
    private var dfps_temp = fps
    private var frame_of_open = 50
    private var frame_of_change_wood = 50
    // переменные ниже для сохранения статуса нажатия на кнопки, типо удерживается палец на кнопке настроек
    // когда палец отпускается в пределах координат кнопки, срабатывает механизм, см функции touches, TOUCH_UP
    private var if_settings_open = false
    private var if_touch_b_settings = false
    private var if_skills_open = false
    private var if_touch_b_skills = false
    private var if_touch_sound = false
    private var if_touch_music = false
    private var if_touch_close = false
    private var if_touch_language = false
    private var if_touch_arm1 = false
    private var if_touch_arm2 = false
    private var if_touch_stomach = false
    private var if_touch_leg1 = false
    private var if_touch_leg2 = false
    //gameplay vars
    // Энергетик
    private var energ = Wood.Energ(-15, 0f)
    private var if_energ_touch = false
    //main hero vars
    private val hero = Hero()
    var diff = 1.0f
    //woods
    // старое дерево нужно только для того, чтобы текущее оно уходило в невидимость
    private var oldWood = Wood(0, diff)
    // 3 списка с фруктами для трех состояний: падают с дерева, прыгнули в рот, огрызки
    private var dropWoodFruits = mutableListOf<Wood.Fruit>() // 1 состояние
    private var navigateFruits = mutableListOf<Wood.Fruit>() // 2 состояние
    private var endFruits = mutableListOf<Wood.Fruit>() // 3 состояние
    private var mainWood = Wood(1, diff)
    private var mainWoodFruits = mutableListOf<Wood.Fruit>()
    private var ifTouch = false
    private var secondWood = Wood(1, diff)
    private var secondWoodFruits = mutableListOf<Wood.Fruit>()
    //graphics vars
    //images
    var for_images = mutableListOf<Drawing_object>()
    // переменные для тряски дерева. сохраняют количество пикселов, на которое сдвинется дерево
    var move_x = mutableListOf<Int>()
    var move_y = mutableListOf<Int>()
    // bg облока и солнце
    private var clouds = mutableListOf<Cloud>()
    private var sun = Sun()

    init {
        // Загружается prefs переменная
        SettingsK.init_prefs(game as AndroidGameK)
        // Загружаются сохранения
        var list = SettingsK.load_save(hero, mainWood, secondWood, energ)
        // распределяются переменные: сложность, количество фруктов
        diff = list[0]
        addFruits(mainWoodFruits, mainWood, list[1].toInt())
        // Масштабирование фруктов
        mainWoodFruits.forEach {
            it.x_original = it.x - (70 + ((it.x-730)*5.66f).toInt())
            it.y_original = it.y - (250 + ((it.y-710)*5.69f).toInt())
        }
        addFruits(secondWoodFruits, secondWood, list[2].toInt())
        // Если тайминг энергетика > 0, то очищаем его
         if (energ.energ_dead_time > 0) {
             energ.exp = 0
             energ.x = -500
             energ.y = -500
         }
        // включается музыка
        AssetsK.SaS_RibbitKing.mediaPlayer.setVolume(SettingsK.volume, SettingsK.volume)
        AssetsK.SaS_RibbitKing.mediaPlayer.isLooping = true
        AssetsK.SaS_RibbitKing.play()
        // генерируется 2 облока
        clouds.add(Cloud(1))
        clouds.add(Cloud(3))
        clouds.first().x = 400 + (Math.random()*400).toInt()
        clouds.last().x = 400 + (Math.random()*400).toInt()
        // debug окно
        SettingsK.if_draw_debug_info = true
        SettingsK.delete_save()
    }

    override fun update(deltaTime: Float) {
        // 3 строки ниже: ограничение числа кадров
        tickTime += deltaTime
        while (tickTime>=tick) {
            tickTime -= tick
            // 8 строк ниже : подсчет кадров в секунду
            fps_temp++
            currentTime = System.nanoTime() / 1000000000.0F
            currTime = System.nanoTime() / 1000000000.0F
            if (currentTime - startTime >= 1) {
                currentTime = System.nanoTime() / 1000000000.0F
                startTime = currentTime
                fps = fps_temp
                fps_temp = 0
                dfps = dfps_temp
                dfps_temp = 0
                // проверка анимации персонажа относительно наличия фруктов в определенном состоянии
                if (dropWoodFruits.size > 0 || navigateFruits.size > 0) {
                    hero.animation.animations[1].playing = true
                    hero.animation.animations[2].playing = false
                }else if (endFruits.size > 0) {
                    hero.animation.animations[1].playing = false
                    hero.animation.animations[2].playing = true
                }else {
                    hero.animation.animations[1].playing = false
                    hero.animation.animations[2].playing = false
                }

                // Уменьшение таймеров энергетика
                if (energ.energ_dead_time > 0) {
                    energ.energ_dead_time--
                    // Если таймер смерти энергетика кончился, то восстанавливаем энергетик
                    if (energ.energ_dead_time == 0) energ = Wood.Energ(-15,0f)
                }
                if (energ.energ_timer > 0) energ.energ_timer--

                // генерация облаков
                if (Math.random()>0.75) {
                    clouds.add(Cloud((1 + Math.random()*3).toInt()))
                }
            }


            if (frame_of_open>0) {
                frame_of_open--
            }else {
                if (if_settings_open) {
                    touches_settings()
                }else if (if_skills_open) {
                    touches_skills()
                }else {
                    // Смена деревьев
                    if (frame_of_change_wood > 0) {
                        changeWood()
                    }else
                    // Эти действия недоступны, пока меняеются деревья
                    {
                        // вычисляет нажатия на экран
                        touches()
                    }

                    //двигаем облака и солнце
                    move_bg()
                    //настраиваем координаты падающих фруктов
                    droppedFruits()
                    // считаем кадры в анимациях
                    prepareAnimation()
                    // сортирует массив for_image элементов Drawing_object, чтобы был эффект наложения картинок в правильной перспективе
                    prepareImages()
                    //проверяем здоровье main wood
                    checkHP()

                    //это для звука фруктов: каждые 0.4 секунд проигрывается звук хруста
                    // т.к. звуков несколько, они проигрываются наугад
                    if (hero.animation.animations[2].playing) {
                        if (currTime - stTime > 0.4) {
                            currTime = System.nanoTime() / 1000000000.0f
                            stTime = currTime
                            if (Math.random() > 0.5) {
                                AssetsK.crack1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.crack2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                }
            }
        }
    }
    // рисуем
    override fun present(deltaTime: Float) {
        // ups counter
        dfps_temp++
        //  1280 x 720 -- Resolution
        // (alpha << 24) | (red << 16 ) | (green<<8) | blue  \\ARGB to int//
        //init graphic variable
        val g : GraphicsK = game.graphics

        // 3 функции отрисовки для 3 состояний окна
        if (if_skills_open) drawSkills(g)
        else if (if_settings_open) drawSettings(g)
        else drawMain(g)


        //debug_info
        if (SettingsK.if_draw_debug_info) {
            g.drawText("ups:" + fps.toString(),50f, 50f, 50f, Color.BLACK)
            g.drawText("fps:" + dfps.toString(), 50f, 150f, 50f, Color.BLACK)
            g.drawText("dmg:" + hero.damage, 50f, 250f, 50f, Color.BLACK)
            g.drawText("hp:" + mainWood.HP + "/" + mainWood.HP_step, 50f, 350f, 50f, Color.BLACK)
            g.drawText("energ:" + energ.energ_timer + "/" + energ.energ_dead_time, 50f, 450f, 50f, Color.BLACK)
            g.drawText("diff:" + diff, 50f, 550f, 50f, Color.BLACK)
        }
    }

    fun drawMain(g : GraphicsK) {
        //draw bg
        g.drawPixmap(AssetsK.background, 0,0)
        //draw sun
        g.drawPixmap(sun.image, sun.x, sun.y, sun.width, sun.height, sun.srcX, sun.srcY, sun.srcWidth, sun.srcHeight, sun.angle, sun.alpha)
        //draw clouds
        for (cloud in clouds) {
            g.drawPixmap(cloud.image, cloud.x, cloud.y, cloud.width, cloud.height)
        }
        //draw main wood
        g.drawPixmap(mainWood.image, mainWood.x, mainWood.y, mainWood.width, mainWood.height)
        //draw hero
        // проверяется каждая анимация: идет анимация или нет. Если одна анимация воспроизводится, остальные рисоваться не будут из-за break
        // анимации работают в приоритете их первичности в списке (0 - первая, наивысший приоритет, 1 - вторая), см Hero, animations
        for (i in 0..2)
            if (hero.animation.animations[i].playing) {
                var t = hero.animation.animations[i].frames[hero.animation.animations[i].current_image]
                g.drawPixmap(hero.image, hero.x, hero.y, hero.width, hero.height, t.srcX, t.srcY, t.srcWidth, t.srcHeight)
                break
            }
        if (!hero.animation.animations[0].playing && !hero.animation.animations[1].playing && !hero.animation.animations[2].playing){
            g.drawPixmap(hero.image, hero.x, hero.y, hero.width, hero.height, hero.srcX, hero.srcY, hero.srcWidth, hero.srcHeight)
        }
        //draw main wood fruits
        for (fruit in mainWoodFruits) {
            g.drawPixmap(fruit.image, fruit.x, fruit.y, fruit.width, fruit.height)
        }
        //draw old wood
        g.drawPixmap(oldWood.image, oldWood.x, oldWood.y, oldWood.width, oldWood.height)
        //draw old wood fruits
        for (fruit in dropWoodFruits) {
            g.drawPixmap(fruit.image, fruit.x, fruit.y, fruit.width, fruit.height)
        }
        for (fruit in navigateFruits) {
            g.drawPixmap(fruit.image, fruit.x, fruit.y, fruit.width, fruit.height)
        }
        for (fruit in endFruits) {
            g.drawPixmap(fruit.image2, fruit.x, fruit.y, fruit.width, fruit.height)
        }
        //draw settings button
        if (!if_settings_open) {
            g.drawPixmap(AssetsK.b_settings, 50, 1020, 177, 164)
        }else {
            g.drawPixmap(AssetsK.b_settings_off, 50, 1020, 177, 164)
        }
        //draw energ
        g.drawPixmap(energ.image, energ.x, energ.y, energ.width, energ.height)
        // draw x2 energ bonus
        if (energ.energ_timer > 0) g.drawPixmap(AssetsK.x2, 50, 150, 249, 177, 0, 0, 249, 177)
        //draw text
        //exp
        g.drawText(hero.kcal.toString() + " kcal", 250f, 150f, 100f, Color.YELLOW)
    }

    fun drawSettings(g : GraphicsK) {
        g.drawPixmap(AssetsK.settings_window, 30, 10, 659, 913)
        g.drawPixmap(AssetsK.b_close, 579, 37)
        if (SettingsK.volume == 1f) {
            g.drawPixmap(AssetsK.b_music, 290, 271)
        }else {
            g.drawPixmap(AssetsK.b_music_off,290, 271)
        }
        if (SettingsK.sound_volume == 1f) {
            g.drawPixmap(AssetsK.b_sound, 135, 261)
        }else {
            g.drawPixmap(AssetsK.b_sound_off, 135, 261)
        }
        g.drawPixmap(AssetsK.b_language, 450, 298)
    }

    fun drawSkills(g : GraphicsK) {
        g.drawPixmap(AssetsK.skills, 0, 0, 721, 1280)
        g.drawText(hero.kcal.toString() + " kcal", 250f, 150f, 100f, Color.YELLOW)
        g.drawText("Damage: " + hero.damage, 250f, 250f, 100f, Color.YELLOW)
        g.drawText(hero.arm1.toString(), 60f, 313f, 50f, Color.YELLOW)
        g.drawText(hero.arm2.toString(), 488f, 313f, 50f, Color.YELLOW)
        g.drawText(hero.stomach.toString(), 275f, 515f, 50f, Color.YELLOW)
        g.drawText(hero.leg1.toString(), 60f, 680f, 50f, Color.YELLOW)
        g.drawText(hero.leg2.toString(), 488f, 680f, 50f, Color.YELLOW)
        g.drawText(hero.arm1_cost.toString(), 60f, 473f, 50f, Color.YELLOW)
        g.drawText(hero.arm2_cost.toString(), 488f, 473f, 50f, Color.YELLOW)
        g.drawText(hero.stomach_cost.toString(), 275f, 675f, 50f, Color.YELLOW)
        g.drawText(hero.leg1_cost.toString(), 60f, 840f, 50f, Color.YELLOW)
        g.drawText(hero.leg2_cost.toString(), 488f, 840f, 50f, Color.YELLOW)
        g.drawText("+" + hero.arm1_buff.toString(), 60f, 393f, 50f, Color.YELLOW)
        g.drawText("+" + hero.arm2_buff.toString(), 488f, 393f, 50f, Color.YELLOW)
        g.drawText("+" + hero.stomach_buff.toString(), 275f, 595f, 50f, Color.YELLOW)
        g.drawText("+" + hero.leg1_buff.toString(), 60f, 760f, 50f, Color.YELLOW)
        g.drawText("+" + hero.leg2_buff.toString(), 488f, 760f, 50f, Color.YELLOW)
        //draw hero
        for (i in 0..2)
            if (hero.animation.animations[i].playing) {
                var t = hero.animation.animations[i].frames[hero.animation.animations[i].current_image]
                g.drawPixmap(hero.image, hero.x, hero.y, hero.width, hero.height, t.srcX, t.srcY, t.srcWidth, t.srcHeight)
                break
            }
        if (!hero.animation.animations[0].playing && !hero.animation.animations[1].playing && !hero.animation.animations[2].playing){
            g.drawPixmap(hero.image, hero.x, hero.y, hero.width, hero.height, hero.srcX, hero.srcY, hero.srcWidth, hero.srcHeight)
        }
    }

    private fun touches() {
        // проверка нажатий на экран
        var touchEvents: MutableList<InputK.TouchEvent> = game.input.touchHandler.getTouchEventss()
        for (touch in touchEvents) {
            when (touch.type) {
                touch.TOUCH_DOWN -> {
                    // цель конкретной проверки можно выяснить с помощью координат, либо непосредственно по тому, что делает код внутри проверки
                    // touch main wood
                    if (SupportK.inBounds(touch, mainWood.x, mainWood.y, mainWood.width, mainWood.height)) {
                        ifTouch = true
                        // Вычисляем смещение
                        move_x.add((Math.random()*20 - 10).toInt())
                        move_y.add((Math.random()*20 - 10).toInt())
                        // Смещаем
                        mainWood.x += move_x.last()
                        mainWood.y += move_y.last()
                        mainWoodFruits.forEach {
                            it.x += move_x.last()
                            it.y += move_y.last()
                        }
                    }
                    // touch hero
                    if (SupportK.inBounds(touch, hero.x+100, hero.y+90, 250, 200)) {
                        hero.animation.animations[0].playing = true
                    }
                    // touch settings
                    if (SupportK.inBounds(touch, 50, 1020, 177, 164)) {
                        if_touch_b_settings = true
                    }
                    // touch energ
                    if (SupportK.inBounds(touch, energ.x, energ.y, energ.width, energ.height)) {
                        if_energ_touch = true
                    }
                }
                touch.TOUCH_UP -> {
                    // touch up main wood
                    if (ifTouch) if (SupportK.inBounds(touch, mainWood.x, mainWood.y, mainWood.width, mainWood.height)) {
                        damage()
                    }
                    // touch up settings
                    if (SupportK.inBounds(touch, 50, 1020, 177, 164)) {
                        if (if_touch_b_settings) {
                            if_settings_open = true
                        }
                    }
                    // touch up energ
                    if (if_energ_touch) if (SupportK.inBounds(touch, energ.x, energ.y, energ.width, energ.height)) {
                            if (energ.energ_timer == 0) {
                                energ.energ_dead_time = energ.energ_dead_time_const
                                energ.energ_timer = energ.energ_timer_const
                                AssetsK.soda.play(SettingsK.sound_volume)

                                energ.image = energ.image2
                                energ.ifDrop = true
                                energ.vectorX = (-5 + Math.random()*10).toFloat()/4
                                energ.vectorY = (3 + Math.random()*7).toFloat()/4
                                hero.kcal += energ.exp
                            }
                        }
                    if_energ_touch = false
                    ifTouch = false
                    if_touch_b_settings = false
                    // stop touching hero
                    hero.animation.animations[0].playing = false
                    // Возвращаем исходное положение
                    while (move_x.size > 0) {
                        mainWood.x -= move_x.first()
                        mainWood.y -= move_y.first()
                        mainWoodFruits.forEach {
                            it.x -= move_x.first()
                            it.y -= move_y.first()
                        }
                        move_x.removeAt(0)
                        move_y.removeAt(0)
                    }
                }
            }
        }
    }

    private fun touches_settings() {
        // проверка нажатий на экран
        var touchEvents: MutableList<InputK.TouchEvent> = game.input.touchHandler.getTouchEventss()
        for (touch in touchEvents) {
            when (touch.type) {
                touch.TOUCH_DOWN -> {
                    //close
                    if (SupportK.inBounds(touch, 579, 37, 177, 164)) {
                        if_touch_close = true
                    }
                    //hero
                    if (SupportK.inBounds(touch, hero.x+50, hero.y, hero.width-100, hero.height-70)) {
                        if_touch_b_skills = true
                    }
                    //sound
                    if (SupportK.inBounds(touch, 135, 261, 165, 173)) {
                        if_touch_sound = true
                    }
                    //music
                    if (SupportK.inBounds(touch, 290, 271, 146, 167)) {
                        if_touch_music = true
                    }
                    //language
                    if (SupportK.inBounds(touch, 450, 298, 148, 128)) {
                        if_touch_language = true
                    }
                }
                touch.TOUCH_UP -> {
                    //close
                    if (SupportK.inBounds(touch, 579, 37, 177, 164)) {
                        if_settings_open = false
                    }
                    //hero
                    if (SupportK.inBounds(touch, hero.x+50, hero.y, hero.width-100, hero.height-70)) {
                        if_settings_open = false
                        if_skills_open = true
                        if (Math.random()*2 > 1.0) {
                            AssetsK.pop1.play(SettingsK.sound_volume)
                        }else {
                            AssetsK.pop2.play(SettingsK.sound_volume)
                        }
                    }
                    //sound
                    if (SupportK.inBounds(touch, 135, 261, 165, 173)) {
                        if (SettingsK.sound_volume == 1f) {
                            SettingsK.sound_volume = 0f
                        }else {
                            SettingsK.sound_volume = 1f
                        }
                    }
                    //music
                    if (SupportK.inBounds(touch, 290, 271, 146, 167)) {
                        if (SettingsK.volume == 1f) {
                            SettingsK.volume = 0f
                        }else {
                            SettingsK.volume = 1f
                        }
                        AssetsK.SaS_RibbitKing.mediaPlayer.setVolume(SettingsK.volume, SettingsK.volume)
                    }
                    //language
                    if (SupportK.inBounds(touch, 450, 298, 148, 128)) {
                        SettingsK.delete_save()
                    }
                    if_touch_close = false
                    if_touch_b_skills = false
                    if_touch_sound = false
                    if_touch_music = false
                    if_touch_language = false
                }
            }
        }
    }

    private fun touches_skills() {
        // проверка нажатий на экран
        var touchEvents: MutableList<InputK.TouchEvent> = game.input.touchHandler.getTouchEventss()
        for (touch in touchEvents) {
            when (touch.type) {
                touch.TOUCH_DOWN -> {
                    //arm1
                    if (SupportK.inBounds(touch, 50, 280, 185, 200)) {
                        if_touch_arm1 = true
                    }
                    //arm2
                    if (SupportK.inBounds(touch, 480, 280, 185, 200)) {
                        if_touch_arm2 = true
                    }
                    //stomach
                    if (SupportK.inBounds(touch, 265, 481, 185, 200)) {
                        if_touch_stomach = true
                    }
                    //leg1
                    if (SupportK.inBounds(touch, 50, 646, 185, 200)) {
                        if_touch_leg1 = true
                    }
                    //leg2
                    if (SupportK.inBounds(touch, 480, 646, 185, 200)) {
                        if_touch_leg2 = true
                    }
                    //hero
                    if (SupportK.inBounds(touch, hero.x+50, hero.y, hero.width-100, hero.height-70)) {
                        if_touch_close = true
                    }

                }
                touch.TOUCH_UP -> {
                    //arm1
                    if (SupportK.inBounds(touch, 50, 280, 185, 200)) {
                        if (hero.kcal >= hero.arm1_cost) {
                            hero.kcal -= hero.arm1_cost
                            hero.arm1++
                            hero.arm1_cost *= 2
                            hero.damage += hero.arm1_buff
                            hero.arm1_buff += 2
                            if (Math.random()*2 > 1.0) {
                                AssetsK.pop1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.pop2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                    //arm2
                    if (SupportK.inBounds(touch, 480, 280, 185, 200)) {
                        if (hero.kcal >= hero.arm2_cost) {
                            hero.kcal -= hero.arm2_cost
                            hero.arm2++
                            hero.arm2_cost *= 2
                            hero.damage += hero.arm2_buff
                            hero.arm2_buff += 2
                            if (Math.random()*2 > 1.0) {
                                AssetsK.pop1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.pop2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                    //stomach
                    if (SupportK.inBounds(touch, 265, 481, 185, 200)) {
                        if (hero.kcal >= hero.stomach_cost) {
                            hero.kcal -= hero.stomach_cost
                            hero.stomach++
                            hero.stomach_cost *= 2
                            hero.damage += hero.stomach_buff
                            hero.stomach_buff += 15
                            if (Math.random()*2 > 1.0) {
                                AssetsK.pop1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.pop2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                    //leg1
                    if (SupportK.inBounds(touch, 50, 646, 185, 200)) {
                        if (hero.kcal >= hero.leg1_cost) {
                            hero.kcal -= hero.leg1_cost
                            hero.leg1++
                            hero.leg1_cost *= 2
                            hero.damage += hero.leg1_buff
                            hero.leg1_buff += 6
                            if (Math.random()*2 > 1.0) {
                                AssetsK.pop1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.pop2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                    //leg2
                    if (SupportK.inBounds(touch, 480, 646, 185, 200)) {
                        if (hero.kcal >= hero.leg2_cost) {
                            hero.kcal -= hero.leg2_cost
                            hero.leg2++
                            hero.leg2_cost *= 2
                            hero.damage += hero.leg2_buff
                            hero.leg2_buff += 6
                            if (Math.random()*2 > 1.0) {
                                AssetsK.pop1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.pop2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                    //hero
                    if (SupportK.inBounds(touch, hero.x+50, hero.y, hero.width-100, hero.height-70)) {
                        if_skills_open = false
                        if (Math.random()*2 > 1.0) {
                            AssetsK.pop1.play(SettingsK.sound_volume)
                        }else {
                            AssetsK.pop2.play(SettingsK.sound_volume)
                        }
                    }
                    if_touch_arm1 = false
                    if_touch_arm2 = false
                    if_touch_stomach = false
                    if_touch_leg1 = false
                    if_touch_leg2 = false
                    if_touch_close = false
                }
            }
        }
    }

    // дерево получает урон
    private fun damage() {
        // Уменьшает хп
        mainWood.HP = mainWood.HP - hero.damage
        // Пока таймер энергетика больше
        if (energ.energ_timer > 0) {
            mainWood.HP = mainWood.HP - (hero.damage*energ.energ_boost).toInt()
        }
        // проигрываем случайный звук
        if (Math.random() > 0.5) {
            AssetsK.shake1.play(SettingsK.sound_volume)
        }else {
            AssetsK.shake2.play(SettingsK.sound_volume)
        }
    }

    private fun checkHP() {
        if (mainWood.HP < mainWood.HP_step*(mainWoodFruits.size-1) && mainWoodFruits.size>0) {
            var t = mainWoodFruits.first()
            t.ifDrop = true
            t.vectorX = (-5 + Math.random()*10).toFloat()/4
            t.vectorY = (3 + Math.random()*7).toFloat()/4
            dropWoodFruits.add(t)
            hero.kcal += t.exp
            mainWoodFruits.removeAt(0)
        }
        // Если кончилось HP или фрукты, то ...
        if (mainWood.HP < 1 || mainWoodFruits.size == 0) {
            diff += 0.05f
            hero.woodsCounter++
            // ...дерево погибает
            changeWood()
        }
    }

    private fun changeWood() {
        if (frame_of_change_wood == 0) {
            // сначала устанавливается счетчик update на 50. То есть за 50 ups будет выполнена перестановка деревьев
            frame_of_change_wood = 50
            // далее деревья и фрукты меняются
            oldWood = mainWood
            dropAll(mainWoodFruits)
            mainWood = secondWood
//            mainWoodFruits.clear()
            mainWoodFruits.addAll(secondWoodFruits)
            mainWoodFruits.forEach {
                // эти значения указываются для масштабирования
                it.x_original = it.x - (oldWood.x + ((it.x-730)*5.66f).toInt())
                it.y_original = it.y - (oldWood.y + ((it.y-710)*5.69f).toInt())
            }
            var id = (1+Math.random()*6).toInt()
            secondWood = Wood(id, diff)
            secondWoodFruits.clear()
            addFruits(secondWoodFruits, secondWood)
        }
        //main x70y250w600h660 second x630y710w106h116
        //main x70+(57-566)y250+(57-341)w32h32 second x630+(10-100)y710+(10-60)w7h7
        if (frame_of_change_wood > 0) {
            // тут изменяются координаты, размеры и непрозрачность
            oldWood.x -= 13
            oldWood.y += 9
            if (frame_of_change_wood > 40) {
                oldWood.width -= 9
                oldWood.height -= 10
            }else {
                oldWood.width -= 10
                oldWood.height -= 11
            }

            mainWood.x -= 13
            mainWood.y -= 9
            if (frame_of_change_wood > 40) {
                mainWood.width += 9
                mainWood.height += 10
            }else {
                mainWood.width += 10
                mainWood.height += 11
            }
            mainWoodFruits.forEach {
                it.x -= it.x_original/frame_of_change_wood
                it.y -= it.y_original/frame_of_change_wood
                it.x_original -= it.x_original/frame_of_change_wood
                it.y_original -= it.y_original/frame_of_change_wood
                it.width++
                it.height++
            }

            frame_of_change_wood--

            if (frame_of_change_wood == 0) {
                mainWood.y -= 10
                mainWood.width += 4
                mainWood.height += 4
                // делаем сейв
                SettingsK.write_save(hero, diff, mainWood, secondWood, mainWoodFruits.size, secondWoodFruits.size, energ)
            }
        }
    }

    private fun addFruits(list : MutableList<Wood.Fruit>, wood : Wood) {
        // добавляет фрукты в список list
        val count = (5+Math.random()*15).toInt()
        for (i in 0..count) {
            list.add(Wood.Fruit(wood.ID, diff))
            list.get(list.lastIndex).let {
                it.x = 740 + (Math.random()*85).toInt()
                it.y = 730 + (Math.random()*45).toInt()
            }
        }
        wood.HP_step = wood.HP / count
    }

    private fun addFruits(list : MutableList<Wood.Fruit>, wood : Wood, count : Int) {
        // добавляет фрукты в список list
        for (i in 0..count-1) {
            list.add(Wood.Fruit(wood.ID, diff))
            list.get(list.lastIndex).let {
                it.x = 740 + (Math.random()*85).toInt()
                it.y = 730 + (Math.random()*45).toInt()
            }
        }
    }

    private fun dropAll(list : MutableList<Wood.Fruit>) {
        // сбрасывает все фрукты с дерева
        list.forEach {
            it.ifDrop = true
            it.vectorX = (-5 + Math.random()*10).toFloat()/4
            it.vectorY = (3 + Math.random()*7).toFloat()/4
            dropWoodFruits.add(it)
            hero.kcal += it.exp
        }
        list.clear()
    }

    private fun droppedFruits() {
        // тут изменяются координаты и состояние падающих фруктов
        var i = 0
        while (i < dropWoodFruits.size) {
            val it = dropWoodFruits[i]
            it.t_vectorX += it.vectorX
            it.t_vectorY -= it.vectorY

            if (Math.abs(it.t_vectorX) > 1) {
                it.x += it.t_vectorX.toInt()
                it.t_vectorX -= it.t_vectorX.toInt()
            }
            if (Math.abs(it.t_vectorY) > 1) {
                it.y += it.t_vectorY.toInt()
                it.t_vectorY -= it.t_vectorY.toInt()
            }

            it.vectorY -= 0.03f

            if (it.x > 800)  {
                dropWoodFruits.remove(it)
                i--
            }else
            if (it.y > 920) {
                it.vectorY = 1.25f
                it.vectorX = (360 - it.x)/170f
                navigateFruits.add(it)
                dropWoodFruits.remove(it)
                i--
            }
            i++
        }
        i = 0
        while (i < navigateFruits.size) {
            val it = navigateFruits[i]
            it.t_vectorX += it.vectorX
            it.t_vectorY -= it.vectorY

            if (Math.abs(it.t_vectorX) > 1) {
                it.x += it.t_vectorX.toInt()
                it.t_vectorX -= it.t_vectorX.toInt()
            }
            if (Math.abs(it.t_vectorY) > 1) {
                it.y += it.t_vectorY.toInt()
                it.t_vectorY -= it.t_vectorY.toInt()
            }

            it.vectorY -= 0.03f

            if (it.x > 800)  {
                navigateFruits.remove(it)
                i--
            }else
            if (it.y > 1140) {
                endFruits.add(it)
                it.vectorY = 1.6f
                it.vectorX = (-5 + Math.random()*10).toFloat()
                navigateFruits.remove(it)
                i--
            }
            i++
        }
        i = 0
        while (i < endFruits.size) {
            val it = endFruits[i]
            it.t_vectorX += it.vectorX
            it.t_vectorY -= it.vectorY

            if (Math.abs(it.t_vectorX) > 1) {
                it.x += it.t_vectorX.toInt()
                it.t_vectorX -= it.t_vectorX.toInt()
            }
            if (Math.abs(it.t_vectorY) > 1) {
                it.y += it.t_vectorY.toInt()
                it.t_vectorY -= it.t_vectorY.toInt()
            }

            it.vectorY -= 0.03f

            if (!(it.x in -100..800) || it.y > 1300)  {
                endFruits.remove(it)
                i--
            }
            i++
        }

        if (energ.ifDrop && energ.x in -50..800 && energ.y < 1300) {
            energ.t_vectorX += energ.vectorX
            energ.t_vectorY -= energ.vectorY

            if (Math.abs(energ.t_vectorX) > 1) {
                energ.x += energ.t_vectorX.toInt()
                energ.t_vectorX -= energ.t_vectorX.toInt()
            }
            if (Math.abs(energ.t_vectorY) > 1) {
                energ.y += energ.t_vectorY.toInt()
                energ.t_vectorY -= energ.t_vectorY.toInt()
            }

            energ.vectorY -= 0.03f
        }
    }

    private fun move_bg() {
        var i = 0
        while (i < clouds.size) {
            val it = clouds.get(i)
            it.x -= it.speed

            if (it.x < -260)  {
                clouds.remove(it)
                i--
            }
            i++
        }
        sun.angle++
    }

    private fun prepareImages() {
        // собираются все изображения с анимацией для их последующей загрузки в prepareAnimation
        for_images.clear()
        for_images.add(hero)
//        for_images.add(mainWood)
//        for_images.add(secondWood)
    }

    private fun prepareAnimation() {
        // пересчитываются кадры всех включенных анимаций
        for (drawings in for_images) {
            for (anims in drawings.animation.animations) {
                if (anims.playing) {
                    anims.current_frame++
                    if (anims.current_frame >= anims.count_of_frames) anims.current_frame = 0
                }
            }
        }
    }

    override fun pause() {
        AssetsK.SaS_RibbitKing.pause()
    }

    override fun resume() {
        AssetsK.SaS_RibbitKing.play()
    }

    override fun dispose() {
    }
}