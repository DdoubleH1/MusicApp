package com.example.baseproject.di

import com.example.baseproject.data.datarepo.DataRepository
import com.example.baseproject.data.datarepo.DataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppDataRepositoryModule {
    @Provides
    fun provideDataRepository(): DataRepository = DataRepositoryImpl()
}