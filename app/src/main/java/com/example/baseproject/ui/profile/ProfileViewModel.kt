package com.example.baseproject.ui.profile

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.MusicDatabase
import com.example.baseproject.data.MusicRepository
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.model.User
import com.example.baseproject.data.relation.SongPlaylistCrossRef
import com.example.baseproject.data.repository.auth.AuthRepository
import com.example.baseproject.data.repository.playlist.PlaylistRepositoryFB
import com.example.baseproject.data.repository.profile.ProfileRepository
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import com.example.core.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    application: Application,
    private val playlistRepositoryFB: PlaylistRepositoryFB
) : BaseViewModel() {

    private var _logOutResponse = MutableLiveData<Response<Boolean>>()
    val logOutResponse: MutableLiveData<Response<Boolean>> get() = _logOutResponse
    private val repository: MusicRepository

    init {
        val musicDao = MusicDatabase.getDatabase(application).musicDao()
        repository = MusicRepository(musicDao)
    }

    fun logOut() {
        viewModelScope.launch {
            _logOutResponse.value = Response.Loading
            _logOutResponse.value = authRepository.firebaseLogout()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePlaylists()
        }
    }

    private var _profileResponse = profileRepository.getProfile()
    val profileResponse: LiveData<Response<User>> = _profileResponse

    private var _editResponse = MutableLiveData<Response<Boolean>>()
    val editResponse: MutableLiveData<Response<Boolean>> get() = _editResponse

    fun updateProfile(name: String, profilePictureUri: Uri?) {
        viewModelScope.launch {
            _editResponse.postValue(Response.Loading)
            _editResponse.postValue(profileRepository.updateProfile(name, profilePictureUri))
        }
    }

    private var _validator: MutableLiveData<Boolean> = MutableLiveData()
    val validator: LiveData<Boolean> get() = _validator

    private var _isValidName = false
    fun setValidState(
        isValidName: Boolean? = _isValidName
    ) {
        _isValidName = isValidName!!
        _validator.value = _isValidName
    }

    val data = MutableLiveData<List<SongPlaylistCrossRef>>()

    fun pushCrossRef(list: List<SongPlaylistCrossRef>) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepositoryFB.updateCrossRef(list)
        }
    }

    fun get() {
        viewModelScope.launch(Dispatchers.IO) {
            data.postValue(repository.getAllCrossRef())
        }
    }
}
