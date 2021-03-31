package com.example.primaryengine

class Hero : Drawing_object() {
    var damage = 1
    var woodsCounter = 0 // счетчик съеденных деревьев
    var kcal = 0 // Опыт

    var arm1 = 1
    var arm2 = 1
    var stomach = 1
    var leg1 = 1
    var leg2 = 1
    var cloth = 1
    var hat = 1


    init {
        image = AssetsK.hero_sprite
        y = 928
        x = 170
        width = 438
        height = 353
        srcX=1312
        srcY=0
        srcWidth=438
        srcHeight=353

        animation.addAnimation(AssetsK.hero_sprite)// удивлён
        animation.animations[0].addFrame(876,0,438,353,1000)
        animation.addAnimation(AssetsK.hero_sprite)//открыт рот
        animation.animations[1].addFrame(438,0,438,353,1000)
        animation.addAnimation(AssetsK.hero_sprite)// жуёт
        animation.animations[2].addFrame(0,0,438,353,20)
        animation.animations[2].addFrame(876,0,438,353,20)
    }
}