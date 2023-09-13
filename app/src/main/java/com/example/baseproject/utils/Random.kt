package com.example.baseproject.utils

import android.util.Log
import com.example.baseproject.data.model.PlaylistSongItem
import java.text.FieldPosition
import java.util.Collections

class Random {

    fun getRandomSongList(
        songList: List<PlaylistSongItem>
    ): List<PlaylistSongItem> {
        val randomList = songList.toMutableList()
        randomList.shuffle()
        return randomList
    }
}