package com.example.baseproject.navigation

import android.os.Bundle
import com.example.core.model.network.navigationComponent.BaseNavigator

interface AppNavigation : BaseNavigator {

    fun openSplashToStartScreen(bundle: Bundle? = null)
    fun openLogintoHomeScreen(bundle: Bundle? = null)
    fun openStartToSignUp(bundle: Bundle? = null)
    fun openStartToLogin(bundle: Bundle? = null)
    fun openSignUptoHome(bundle: Bundle? = null)
    fun openProfileToStartScreen(bundle: Bundle? = null)
    fun openStartToHome(bundle: Bundle? = null)

}