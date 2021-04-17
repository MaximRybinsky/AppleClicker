package com.example.primaryengine

import android.graphics.Color
import android.util.Log
import com.example.primaryengine.framework.*

class OpenWorldGame(game : GameK) : ScreenK(game) {
    // engine vars
    // autosafe vars
    private var stTime : Float = System.nanoTime() / 1000000000.0f
    private var currTime : Float = System.nanoTime() / 1000000000.0f
    private var timer = 10
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
    private var if_touch_arm1 = false
    private var if_touch_arm2 = false
    private var if_touch_stomach = false
    private var if_touch_leg1 = false
    private var if_touch_leg2 = false
    //gameplay vars
    //button touchs
    var ifdmgup = false
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
    private var mainWood = Wood(0, diff)
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

    init {
        // включается музыка
        AssetsK.SaS_RibbitKing.mediaPlayer.setVolume(SettingsK.volume, SettingsK.volume)
        AssetsK.SaS_RibbitKing.play()

        //main x70y250w600h660 second x630y710w106h116
        //main x70+(57-566)y250+(57-341)w32h32 second x630+(10-100)y710+(10-60)w7h7

        addFruits(mainWoodFruits, mainWood)
        mainWoodFruits.forEach {

        }
        mainWood.x = 70
        mainWood.y = 250
        mainWood.width = 600
        mainWood.height = 660
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
                // timer нужен для автосейвов, но пока что ничего не сохраняется, см SettingsK
                timer--
                if (timer == 0) {
                    SettingsK.write_data()
                    SettingsK.write_save(game.fileIO)
                    timer = 10
                }
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

                    //настраиваем координаты падающих фруктов
                    droppedFruits()
                    // считаем кадры в анимациях
                    prepareAnimation()
                    // сортирует массив for_image элементов Drawing_object, чтобы был эффект наложения картинок в правильной перспективе
                    prepareImages()
                    //проверяем здоровье main wood
                    checkHP()

                    //это для звука фруктов: каждые 0.3 секунд проигрывается звук хруста
                    // т.к. звуков несколько, они проигрываются наугад
                    if (hero.animation.animations[2].playing) {
                        if (currTime - stTime > 0.3) {
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

        // 3 функции отрисовки для 3 состояний окна: игра, настройки, скиллы
        drawMain(g)
        if (if_settings_open) drawSettings(g)
        if (if_skills_open) drawSkills(g)


        //debug_info
        if (SettingsK.if_draw_debug_info) {
            g.drawText("ups:" + fps.toString(),50f, 50f, 50f, Color.BLACK)
            g.drawText("fps:" + dfps.toString(), 50f, 150f, 50f, Color.BLACK)
        }
    }

    fun drawMain(g : GraphicsK) {
        //draw bg
        g.drawPixmap(AssetsK.background, 0,0,721,1281, 0, 0, 800, 1280)
        //draw second wood
        g.drawPixmap(secondWood.image, secondWood.x, secondWood.y, secondWood.width, secondWood.height, secondWood.srcX, secondWood.srcY, secondWood.srcWidth, secondWood.srcHeight, secondWood.alpha)
        //draw second wood fruits
        for (fruit in secondWoodFruits) {
            g.drawPixmap(fruit.image, fruit.x, fruit.y, fruit.width, fruit.height, fruit.srcX, fruit.srcY, fruit.srcWidth, fruit.srcHeight, fruit.alpha)
        }
        //draw main wood
        g.drawPixmap(mainWood.image, mainWood.x, mainWood.y, mainWood.width, mainWood.height, mainWood.srcX, mainWood.srcY, mainWood.srcWidth, mainWood.srcHeight, mainWood.alpha)
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
            g.drawPixmap(fruit.image, fruit.x, fruit.y, fruit.width, fruit.height, fruit.srcX, fruit.srcY, fruit.srcWidth, fruit.srcHeight, fruit.alpha)
        }
        //draw old wood
        g.drawPixmap(oldWood.image, oldWood.x, oldWood.y, oldWood.width, oldWood.height, oldWood.srcX, oldWood.srcY, oldWood.srcWidth, oldWood.srcHeight, oldWood.alpha)
        //draw old wood fruits
        for (fruit in dropWoodFruits) {
            g.drawPixmap(fruit.image, fruit.x, fruit.y, fruit.width, fruit.height, fruit.srcX, fruit.srcY, fruit.srcWidth, fruit.srcHeight, fruit.alpha)
        }
        for (fruit in navigateFruits) {
            g.drawPixmap(fruit.image, fruit.x, fruit.y, fruit.width, fruit.height, fruit.srcX, fruit.srcY, fruit.srcWidth, fruit.srcHeight, fruit.alpha)
        }
        for (fruit in endFruits) {
            g.drawPixmap(fruit.image2, fruit.x, fruit.y, fruit.width, fruit.height, fruit.srcX, fruit.srcY, fruit.srcWidth, fruit.srcHeight, fruit.alpha)
        }
        //draw button
        if (!if_settings_open) {
            g.drawPixmap(AssetsK.b_settings, 50, 1020, 177, 164)
        }else {
            g.drawPixmap(AssetsK.b_settings_off, 50, 1020, 177, 164)
        }
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
        g.drawText(hero.arm1.toString(), 60f, 313f, 50f, Color.YELLOW)
        g.drawText(hero.arm2.toString(), 488f, 313f, 50f, Color.YELLOW)
        g.drawText(hero.stomach.toString(), 275f, 515f, 50f, Color.YELLOW)
        g.drawText(hero.leg1.toString(), 60f, 680f, 50f, Color.YELLOW)
        g.drawText(hero.leg2.toString(), 488f, 680f, 50f, Color.YELLOW)
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
                }
                touch.TOUCH_UP -> {
                    // touch up on main wood
                    if (ifTouch) if (SupportK.inBounds(touch, mainWood.x, mainWood.y, mainWood.width, mainWood.height)) {
                        damage()
                    }
                    if (SupportK.inBounds(touch, 50, 1020, 177, 164)) {
                        if (if_touch_b_settings) {
                            if_settings_open = true
                        }
                    }
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
                    if_touch_close = false
                    if_touch_b_skills = false
                    if_touch_sound = false
                    if_touch_music = false
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
                        if (hero.kcal > 999) {
                            hero.kcal -= 1000
                            hero.arm1++
                            hero.damage++
                            if (Math.random()*2 > 1.0) {
                                AssetsK.pop1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.pop2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                    //arm2
                    if (SupportK.inBounds(touch, 480, 280, 185, 200)) {
                        if (hero.kcal > 999) {
                            hero.kcal -= 1000
                            hero.arm2++
                            hero.damage++
                            if (Math.random()*2 > 1.0) {
                                AssetsK.pop1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.pop2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                    //stomach
                    if (SupportK.inBounds(touch, 265, 481, 185, 200)) {
                        if (hero.kcal > 2999) {
                            hero.kcal -= 3000
                            hero.stomach++
                            hero.damage += 5
                            if (Math.random()*2 > 1.0) {
                                AssetsK.pop1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.pop2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                    //leg1
                    if (SupportK.inBounds(touch, 50, 646, 185, 200)) {
                        if (hero.kcal > 1499) {
                            hero.kcal -= 1500
                            hero.leg1++
                            hero.damage += 3
                            if (Math.random()*2 > 1.0) {
                                AssetsK.pop1.play(SettingsK.sound_volume)
                            }else {
                                AssetsK.pop2.play(SettingsK.sound_volume)
                            }
                        }
                    }
                    //leg2
                    if (SupportK.inBounds(touch, 480, 646, 185, 200)) {
                        if (hero.kcal > 1499) {
                            hero.kcal -= 1500
                            hero.leg2++
                            hero.damage += 3
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
        // С вероятностью 20% падает один фрукт(первый в списке)
        if (Math.random()*10 < 2) {
            var t = mainWoodFruits.first()
            t.ifDrop = true
            t.vectorX = (-5 + Math.random()*10).toFloat()/4
            t.vectorY = (3 + Math.random()*7).toFloat()/4
            dropWoodFruits.add(t)
            hero.kcal += t.exp
            mainWoodFruits.removeAt(0)
        }
        // Уменьшает хп
        mainWood.HP = mainWood.HP - hero.damage
        // проигрываем случайный звук
        if (Math.random() > 0.5) {
            AssetsK.shake1.play(SettingsK.sound_volume)
        }else {
            AssetsK.shake2.play(SettingsK.sound_volume)
        }
    }

    private fun checkHP() {
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
            var id = (1+Math.random()*2).toInt()
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
                oldWood.alpha -= 5
                mainWood.y -= 10
                mainWood.width += 4
                mainWood.height += 4
            }
        }
    }

    private fun addFruits(list : MutableList<Wood.Fruit>, wood : Wood) {
        // добавляет фрукты в список list
        val count = ((5+Math.random()*15)*diff).toInt()
        for (i in 0..count) {
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
            }
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
            }
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