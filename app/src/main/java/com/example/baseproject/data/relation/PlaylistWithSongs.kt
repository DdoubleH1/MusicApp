package com.example.baseproject.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.model.PlaylistSongItem

data class PlaylistWithSongs(
    @Embedded val playlist: LibraryItem,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(SongPlaylistCrossRef::class)
    )
    val songs: List<PlaylistSongItem>
)