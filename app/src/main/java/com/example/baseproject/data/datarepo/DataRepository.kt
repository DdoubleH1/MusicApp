package com.example.baseproject.data.datarepo

import android.content.Context
import com.example.baseproject.data.model.PlaylistSongItem

interface DataRepository {
    fun getSong(context: Context): List<PlaylistSongItem>
}