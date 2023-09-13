package com.example.baseproject.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.model.PlaylistSongItem

data class SongWithPlaylists(
    @Embedded val song: PlaylistSongItem,
    @Relation(
        parentColumn = "songId",
        entityColumn = "playlistId",
        associateBy = Junction(SongPlaylistCrossRef::class)
    )
    val playlists: List<LibraryItem>
)