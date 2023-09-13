package com.example.baseproject.ui.play

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.MusicDatabase
import com.example.baseproject.data.MusicRepository
import com.example.baseproject.data.model.LibraryItem
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    application: Application
) : BaseViewModel() {
    private val repository: MusicRepository

    init {
        val musicDao = MusicDatabase.getDatabase(application).musicDao()
        repository = MusicRepository(musicDao)
    }

    private val _dimissDialog = MutableLiveData<Boolean>()
    val dimissDialog: LiveData<Boolean> = _dimissDialog

    fun switchDismissDialog(dismissState : Boolean) {
        _dimissDialog.postValue(dismissState)
    }
}