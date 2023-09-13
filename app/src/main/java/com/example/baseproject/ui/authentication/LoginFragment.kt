package com.example.baseproject.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLoginBinding
import com.example.baseproject.extension.validate
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.utils.Response
import com.example.core.base.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    @Inject
    lateinit var appNavigation: AppNavigation


    private val viewModel: LoginViewModel by viewModels()
    override fun getVM(): LoginViewModel = viewModel



    override fun setOnClick() {
        super.setOnClick()
        binding.btnSignIn.setOnClickListener {
            viewModel.signIn(
                binding.editTextTextEmailAddress.text.toString(),
                binding.editTextTextPassword.text.toString()
            )
        }


        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.apply {
            signInResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                        binding.editTextTextPassword.isEnabled = false
                        binding.editTextTextEmailAddress.isEnabled = false
                        binding.ProgressBar.visibility = View.VISIBLE
                    }

                    is Response.Failure -> {
                        binding.editTextTextPassword.isEnabled = true
                        binding.editTextTextEmailAddress.isEnabled = true
                        binding.ProgressBar.visibility = View.GONE
                    }

                    is Response.Success -> {
                        binding.editTextTextPassword.isEnabled = true
                        binding.editTextTextEmailAddress.isEnabled = true
                        binding.ProgressBar.visibility = View.GONE
                    }
                }
                validator.observe(viewLifecycleOwner) { validator ->
                    binding.btnSignIn.isEnabled = validator
                }
            }
        }

        binding.apply {
            editTextTextEmailAddress.validate { email ->
                if (email.isEmpty()) {
                    editTextTextEmailAddress.error =
                        getString(R.string.do_not_leave_this_field_blank)
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextTextEmailAddress.error = getString(R.string.invalid_email_address)
                } else {
                    viewModel.setValidState(isValidEmail = true)
                }
            }
            editTextTextPassword.validate { password ->
                if (password.isEmpty()) {
                    editTextTextPassword.error = getString(R.string.do_not_leave_this_field_blank)
                } else if (password.length < 8) {
                    editTextTextPassword.error =
                        getString(R.string.password_must_be_at_least_8_characters)
                } else {
                    editTextTextPassword.error = null
                    viewModel.setValidState(isValidPassword = true)
                }
            }
        }
        viewModel.validator.observe(viewLifecycleOwner) { validator ->
            binding.btnSignIn.isEnabled = validator
            binding.btnSignIn.background =
                if (validator) {
                    AppCompatResources.getDrawable(requireContext(), R.drawable.btn_enable_background)
                } else {
                    AppCompatResources.getDrawable(requireContext(), R.drawable.btn_disable_background)
                }
        }

        if (viewModel.isLogin) {
            appNavigation.openLogintoHomeScreen()
        }

    }

    override fun bindingAction() {
        super.bindingAction()
        viewModel.signInResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Success -> {
                    getString(R.string.login_successfully).toast(requireContext())
                    appNavigation.openLogintoHomeScreen()
                }

                is Response.Failure -> {
                    when (result.e) {
                        is FirebaseAuthInvalidUserException -> {
                            getString(R.string.email_is_not_registered).toast(requireContext())
                        }

                        is IllegalArgumentException -> {
                            getString(R.string.email_or_password_is_empty).toast(requireContext())
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            getString(R.string.password_is_incorrect).toast(requireContext())
                        }

                        is FirebaseNetworkException -> {
                            getString(R.string.no_internet_connection).toast(requireContext())
                        }

                        else -> {
                            getString(R.string.login_failed).toast(requireContext())
                        }
                    }
                }
            }
        }
    }

}