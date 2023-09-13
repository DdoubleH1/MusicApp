package com.example.core.pref

import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface RxPreferences : BasePreferences {

    fun getToken(): Flow<String?>

    suspend fun setUserToken(userToken: String)

    fun getLanguage(): Flow<String?>

    suspend fun setLanguage(language: String)

    fun getEmail(): Flow<String?>

    suspend fun setEmail(email: String)

    fun logout()

}