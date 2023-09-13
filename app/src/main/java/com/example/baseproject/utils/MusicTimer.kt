package com.example.baseproject.utils

class MusicTimer {
    fun setTimer(duration : Int): String {
        var timer = ""
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60
        timer += "$minutes:"
        if (seconds < 10) {
            timer += "0"
        }
        timer += seconds
        return timer
    }

}