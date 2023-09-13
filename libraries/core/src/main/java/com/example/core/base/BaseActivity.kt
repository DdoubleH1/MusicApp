package com.example.core.base

import android.app.LocaleManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.internal.ContextUtils
import java.lang.ref.WeakReference

abstract class BaseActivity<BD : ViewDataBinding, VM : BaseViewModel> :
    BaseActivityNotRequireViewModel<BD>() {

    private lateinit var viewModel: VM

    abstract fun getVM(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()
    }
}