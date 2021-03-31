package com.example.primaryengine

import com.example.primaryengine.framework.AndroidGameK
import com.example.primaryengine.framework.ScreenK

class MainActivity : AndroidGameK() {
    // Главная активность, с которой всё начинается
    override fun getStartScreen(): ScreenK = LoadingScreenK(this)
}
