package com.example.baseproject.ui.library

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.MusicDatabase
import com.example.baseproject.data.MusicRepository
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.repository.playlist.PlaylistRepositoryFB
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    application: Application,
    private val playlistRepositoryFB: PlaylistRepositoryFB
) : BaseViewModel() {


    private val repository: MusicRepository
    var playlistList: MutableLiveData<Response<List<LibraryItem>>>? = null

    init {
        val musicDao = MusicDatabase.getDatabase(application).musicDao()
        repository = MusicRepository(musicDao)
    }

    fun get() {
        playlistList = playlistRepositoryFB.getPlaylist()
    }

    fun setup(list: List<LibraryItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            list.forEach {
                repository.addPlaylist(it)
            }
        }
    }

    fun addPlaylist(item: LibraryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepositoryFB.addPlaylist(item)
        }
    }

    private val _newPlaylist = MutableLiveData("")
    val newPlaylist: LiveData<String> = _newPlaylist

    fun creatPl(title: String) {
        _newPlaylist.value = title
    }

    fun set() {
        _newPlaylist.value = ""
    }
}