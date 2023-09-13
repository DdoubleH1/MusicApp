package com.example.baseproject.data.repository.auth

import com.example.baseproject.utils.Response

interface AuthRepository {

    suspend fun firebaseSignUp(email: String, password: String, userName: String): Response<Boolean>
    suspend fun firebaseLogin(email: String, password: String): Response<Boolean>


    suspend fun firebaseLogout(): Response<Boolean>
    fun isLogin(): Boolean
}