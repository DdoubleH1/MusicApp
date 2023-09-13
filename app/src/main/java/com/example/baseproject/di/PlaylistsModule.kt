package com.example.baseproject.di

import com.example.baseproject.data.repository.playlist.PlaylistRepositoryFB
import com.example.baseproject.data.repository.playlist.PlaylistRepositoryFBImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class PlaylistsModule {
    @Provides
    fun providePlaylistRepository(): PlaylistRepositoryFB = PlaylistRepositoryFBImpl()
}