package com.example.baseproject.ui.home.hometab

import androidx.lifecycle.LiveData
import com.example.baseproject.data.model.User
import com.example.baseproject.data.repository.auth.AuthRepository
import com.example.baseproject.data.repository.profile.ProfileRepository
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeTabViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository)
: BaseViewModel()
{
    private var _profileResponse = profileRepository.getProfile()
    val profileResponse: LiveData<Response<User>> = _profileResponse

}