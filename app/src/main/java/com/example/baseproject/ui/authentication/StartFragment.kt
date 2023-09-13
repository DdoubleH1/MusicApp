package com.example.baseproject.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentStartBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment :
    BaseFragment<FragmentStartBinding, StartFragmentViewModel>(R.layout.fragment_start) {

    companion object {
        fun newInstance() = StartFragment()
    }

    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: StartFragmentViewModel by viewModels()
    override fun getVM() = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        if (viewModel.isLogin) {
            appNavigation.openStartToHome()
        }
//        appNavigation.openStartToHome()
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.apply {
            loginTextView.setOnClickListener {
                appNavigation.openStartToLogin()
            }
            freeSignUpButton.setOnClickListener {
                appNavigation.openStartToSignUp()
            }
        }
    }
}