package com.example.baseproject.data.relation

import androidx.room.Entity

@Entity(primaryKeys = ["songId", "playlistId"])
data class SongPlaylistCrossRef(
    val songId: Int = 0,
    val playlistId: String = ""
)