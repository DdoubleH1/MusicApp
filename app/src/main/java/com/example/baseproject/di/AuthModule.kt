package com.example.baseproject.di

import com.example.baseproject.data.repository.auth.AuthRepository
import com.example.baseproject.data.repository.auth.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class AuthModule {
    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()
}