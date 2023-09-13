package com.example.baseproject.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.R
import com.example.core.base.BaseViewModel
import com.example.core.utils.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel :
    BaseViewModel() {

    val actionSPlash = SingleLiveEvent<SplashActionState>()
    val splashTitle = MutableLiveData(R.string.splash)

    init {
        viewModelScope.launch {
            delay(1000)
            actionSPlash.value = SplashActionState.Finish
        }
    }
}

sealed class SplashActionState {
    object Finish : SplashActionState()
}