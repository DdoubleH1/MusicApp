package com.example.baseproject.ui.authentication

import com.example.baseproject.data.repository.auth.AuthRepository
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartFragmentViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel(){
    val isLogin: Boolean
        get() = authRepository.isLogin()
}
