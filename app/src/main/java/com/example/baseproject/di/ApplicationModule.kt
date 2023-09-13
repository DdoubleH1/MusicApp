package com.example.baseproject.di

import android.app.Application
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.navigation.AppNavigatorImpl
import com.example.core.model.network.navigationComponent.BaseNavigator
import com.example.setting.DemoNavigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class ApplicationModule {
    @Binds
    @ActivityScoped
    abstract fun provideApplication(application: Application): Application
}