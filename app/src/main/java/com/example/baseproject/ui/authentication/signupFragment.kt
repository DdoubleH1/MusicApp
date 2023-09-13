package com.example.baseproject.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentSignupBinding
import com.example.baseproject.extension.validate
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.utils.Response
import com.example.core.base.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class signupFragment :
    BaseFragment<FragmentSignupBinding, SignupViewModel>(R.layout.fragment_signup) {

    @Inject
    lateinit var appNavigation: AppNavigation


    private val viewModel: SignupViewModel by viewModels()
    override fun getVM() = viewModel

    override fun setOnClick() {
        super.setOnClick()
        binding.apply {
            btnSignUp.setOnClickListener {
                val email = editTextTextEmailAddress.text.toString().trim()
                val password = editTextTextPassword.text.toString().trim()
                val userName = editTextTextUserName.text.toString().trim()
                viewModel.signUp(email, password, userName)
            }

            binding.btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun bindingAction() {
        super.bindingAction()
        viewModel.apply {
            signUpResponse.observe(viewLifecycleOwner){response ->
                when(response){
                    is Response.Loading -> {}
                    is Response.Success ->{
                        getString(R.string.string_sign_in_successfully).toast(requireContext())
                        appNavigation.openSignUptoHome()
                    }
                    is Response.Failure -> {
                        when(response.e){
                            is FirebaseAuthUserCollisionException -> {
                                getString(R.string.email_already_exists).toast(requireContext())
                            }
                            is FirebaseNetworkException -> {
                                getString(R.string.no_internet_connection).toast(requireContext())
                            }
                            else -> {
                                getString(R.string.sign_up_failed).toast(requireContext())
                            }
                        }
                    }
                }
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.apply {
            signUpResponse.observe(viewLifecycleOwner){response ->
                when(response){
                    is Response.Loading -> {
                       binding.editTextTextPassword.isEnabled = false
                        binding.editTextTextEmailAddress.isEnabled = false
                        binding.editTextTextUserName.isEnabled = false
                        binding.ProgressBar.visibility = View.VISIBLE
                    }
                    is Response.Failure -> {
                        binding.editTextTextPassword.isEnabled = true
                        binding.editTextTextEmailAddress.isEnabled = true
                        binding.editTextTextUserName.isEnabled = true
                        binding.ProgressBar.visibility = View.GONE
                    }
                    is Response.Success -> {
                        binding.editTextTextPassword.isEnabled = true
                        binding.editTextTextEmailAddress.isEnabled = true
                        binding.editTextTextUserName.isEnabled = true
                        binding.ProgressBar.visibility = View.GONE
                    }
                }

            }
        }

        binding.apply {
            editTextTextEmailAddress.validate { email ->
                if (email.isEmpty()) {
                    editTextTextEmailAddress.error = getString(R.string.do_not_leave_this_field_blank)
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextTextEmailAddress.error = getString(R.string.invalid_email_address)
                } else {
                    editTextTextEmailAddress.error = null
                    viewModel.setValidState(isValidEmail = true)
                }
            }
            editTextTextPassword.validate { password ->
                if (password.isEmpty()) {
                    editTextTextPassword.error = getString(R.string.do_not_leave_this_field_blank)
                } else if (password.length < 8) {
                    editTextTextPassword.error = getString(R.string.password_must_be_at_least_8_characters)
                } else {
                    editTextTextPassword.error = null
                    viewModel.setValidState(isValidPassword = true)
                }
            }
            editTextTextUserName.validate { username ->
                if (username.isEmpty()) {
                    editTextTextUserName.error = getString(R.string.do_not_leave_this_field_blank)
                }  else {
                    editTextTextUserName.error = null
                    viewModel.setValidState(isValidUserName = true)
                }
            }


        }
        viewModel.validator.observe(viewLifecycleOwner) { validator ->
            binding.btnSignUp.isEnabled = validator
            binding.btnSignUp.background =
                if (validator) {
                    AppCompatResources.getDrawable(requireContext(), R.drawable.btn_enable_background)
                } else {
                    AppCompatResources.getDrawable(requireContext(), R.drawable.btn_disable_background)
                }
        }
    }
}