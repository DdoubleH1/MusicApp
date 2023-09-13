package com.example.baseproject.data.model

data class WaitList(
    var currentPlaylist: String,
    var history: ArrayDeque<PlaylistSongItem>,
    var waitList: ArrayDeque<PlaylistSongItem>
){
    constructor(): this("", ArrayDeque<PlaylistSongItem>(), ArrayDeque<PlaylistSongItem>())
}