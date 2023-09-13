package com.example.baseproject.data.repository.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.data.model.User
import com.example.baseproject.utils.Response

interface ProfileRepository {
    suspend fun updateProfile(name: String, profilePictureUri : Uri?) : Response<Boolean>
    fun getProfile() : MutableLiveData<Response<User>>

}