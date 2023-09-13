package com.example.baseproject.container

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.MusicDatabase
import com.example.baseproject.data.MusicRepository
import com.example.baseproject.data.datarepo.DataRepository
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(data: DataRepository, application: Application) :
    BaseViewModel() {

    private val mData = data
    private val mApplication = application
    private val musicDao = MusicDatabase.getDatabase(mApplication).musicDao()
    private val repository = MusicRepository(musicDao)
    fun getSong() {
        val songData = mData.getSong(mApplication)
        for (items in songData) {
            addSong(items)
        }
    }

    fun addSong(item: PlaylistSongItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSong(item)
        }
    }
}