package com.example.primaryengine

import com.example.primaryengine.framework.InputK

class SupportK {
    companion object {
        fun inBounds(event : InputK.TouchEvent, x : Int, y : Int, width : Int, height : Int) : Boolean {
            return (event.x > x && event.x < x+width-1 && event.y > y && event.y < y+height-1)
        }
        fun inBounds(x1 : Int, y1 : Int,x : Int, y : Int, width : Int, height : Int) : Boolean {
            return (x1 > x && x1 < x+width-1 && y1 > y && y1 < y+height-1)
        }
        fun inBoundsBetween(x0 : Int, y0 : Int, x1 : Int, y1 : Int, x2 : Int, y2 : Int) : Boolean {
            return (x0 > x1 && x0 < x2 && y0 > y1 && y0 < y2)
        }
    }
}