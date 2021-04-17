package com.example.primaryengine

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Typeface
import android.util.Log
import com.example.primaryengine.framework.GameK
import com.example.primaryengine.framework.GraphicsK
import com.example.primaryengine.framework.ScreenK

class LoadingScreenK(game : GameK): ScreenK(game) {

    override fun update(deltaTime: Float) {
        var g : GraphicsK = game.graphics
        // ЗАГРУЖАЕМ ФАЙЛЫ В СИСТЕМУ
        AssetsK.oneEmptyPixel = g.newPixmap("oneEmptyPixel.png", -1)
        AssetsK.hero = g.newPixmap("hero.png", 1)
        AssetsK.hero_sprite = g.newPixmap("hero_sprite.png",3)
        AssetsK.mainSprite = g.newPixmap("mainSprite.png", 2)
        AssetsK.apple = g.newPixmap("FruitApple.png", 4)
        AssetsK.apple_stub = g.newPixmap("FruitAppleStub.png", 5)
        AssetsK.pear = g.newPixmap("FruitPear.png", 6)
        AssetsK.pear_stub = g.newPixmap("FruitPearStub.png", 7)
        AssetsK.tree = g.newPixmap("Tree.png", 8)
        AssetsK.b_close = g.newPixmap("ButtonClose.png", 9)
        AssetsK.b_close_off = g.newPixmap("ButtonClose-off.png", 10)
        AssetsK.b_music = g.newPixmap("ButtonMusic.png", 11)
        AssetsK.b_music_off = g.newPixmap("ButtonMusic-off.png", 12)
        AssetsK.b_settings = g.newPixmap("ButtonSettings.png", 13)
        AssetsK.b_settings_off = g.newPixmap("ButtonSettings-off.png", 14)
        AssetsK.b_sound = g.newPixmap("ButtonSound.png", 15)
        AssetsK.b_sound_off = g.newPixmap("ButtonSound-off.png", 16)
        AssetsK.settings_window = g.newPixmap("SettingsWindow.png", 17)
        AssetsK.background = g.newPixmap("Background.png", 18)
        AssetsK.bg_cloud_1 = g.newPixmap("BackGroundCloud-1.png", 19)
        AssetsK.bg_cloud_2 = g.newPixmap("BackGroundCloud-2.png", 20)
        AssetsK.bg_cloud_3 = g.newPixmap("BackGroundCloud-3.png", 21)
        AssetsK.bg_sun = g.newPixmap("BackGroundSun.png", 22)
        AssetsK.i_arm = g.newPixmap("IconArm.png", 23)
        AssetsK.i_cloth = g.newPixmap("IconCloths.png", 24)
        AssetsK.i_hat = g.newPixmap("IconHat.png", 25)
        AssetsK.i_leg = g.newPixmap("IconLeg.png", 26)
        AssetsK.i_stomach = g.newPixmap("IconStomach.png", 27)
        AssetsK.skills = g.newPixmap("Skills.png", 28)
        AssetsK.b_language = g.newPixmap("ButtonLanguage.png", 29)
        AssetsK.redBull = g.newPixmap("Redbull.png", 30)
        AssetsK.redBull_off = g.newPixmap("Redbull-off.png", 31)
        AssetsK.x2 = g.newPixmap("Bonus(X2).png", 32)
        AssetsK.goethe = Typeface.createFromAsset(g.assets, "GoetheBold.ttf"); g.paint.setTypeface(AssetsK.goethe)
        AssetsK.hrum = game.audio.newSound("hrum.ogg")
        AssetsK.crack1 = game.audio.newSound("CrackSound-1.ogg")
        AssetsK.crack2 = game.audio.newSound("CrackSound-2.ogg")
        AssetsK.pop1 = game.audio.newSound("PopSound-1.ogg")
        AssetsK.pop2 = game.audio.newSound("PopSound-2.ogg")
        AssetsK.shake1 = game.audio.newSound("ShakeSound-1.ogg")
        AssetsK.shake2 = game.audio.newSound("ShakeSound-2.ogg")
        AssetsK.soda = game.audio.newSound("SodaCanSound.ogg")
        AssetsK.SaS_RibbitKing = game.audio.newMusic("[Loop Music] Sand and Slopes - Ribbit King.ogg")
        SettingsK.load_save(game.fileIO)
        game.screenn = OpenWorldGame(game)
    }

    override fun present(deltaTime: Float) { }

    override fun pause() {  }

    override fun resume() { }

    override fun dispose() {  }

}