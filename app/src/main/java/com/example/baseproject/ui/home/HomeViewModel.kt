package com.example.baseproject.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.MusicDatabase
import com.example.baseproject.data.MusicRepository
import com.example.baseproject.data.datarepo.DataRepository
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.baseproject.data.relation.SongPlaylistCrossRef
import com.example.baseproject.data.repository.playlist.PlaylistRepositoryFB
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    playlistRepositoryFB: PlaylistRepositoryFB,
    private val data: DataRepository,
) : BaseViewModel() {
    private val repository: MusicRepository

    init {
        val musicDao = MusicDatabase.getDatabase(application).musicDao()
        repository = MusicRepository(musicDao)
    }

    var playlistsList = playlistRepositoryFB.getPlaylist()
    val crossRefData = playlistRepositoryFB.getCrossRef()

    fun setup(list: List<LibraryItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            list.forEach {
                repository.addPlaylist(it)
            }
        }
    }

    fun setupCrossRef(list: List<SongPlaylistCrossRef>) {
        list.forEach {
            viewModelScope.launch {
                repository.addSongPlaylistCrossRef(it)
            }
        }
    }

    fun getSong() {
        val songData = data.getSong(application)
        for (items in songData) {
            Log.d("HoangDH", "getSong: ${items.songTitle}")
            addSong(items)
        }
    }

    private fun addSong(item: PlaylistSongItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSong(item)
        }
    }
}
