package com.example.baseproject.ui.authentication

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.MusicDatabase
import com.example.baseproject.data.MusicRepository
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.relation.SongPlaylistCrossRef
import com.example.baseproject.data.repository.auth.AuthRepository
import com.example.baseproject.data.repository.playlist.PlaylistRepositoryFB
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) :
    BaseViewModel() {
    private var _signInResponse = MutableLiveData<Response<Boolean>>()
    val signInResponse: MutableLiveData<Response<Boolean>> get() = _signInResponse

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInResponse.value = Response.Loading
            _signInResponse.value = authRepository.firebaseLogin(email, password)
        }
    }

    private var _validator = MutableLiveData<Boolean>()
    val validator: MutableLiveData<Boolean> get() = _validator

    private var _isValidEmail = false
    private var _isValidPassword = false

    fun setValidState(
        isValidEmail: Boolean? = _isValidEmail,
        isValidPassword: Boolean? = _isValidPassword
    ) {
        _isValidEmail = isValidEmail!!
        _isValidPassword = isValidPassword!!
        _validator.value = _isValidEmail && _isValidPassword
    }

    val isLogin: Boolean
        get() = authRepository.isLogin()
}