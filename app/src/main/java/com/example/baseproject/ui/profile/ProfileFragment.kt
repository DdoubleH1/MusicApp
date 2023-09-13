package com.example.baseproject.ui.profile


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.container.MainActivity
import com.example.baseproject.databinding.FragmentProfileBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.service.MusicService
import com.example.baseproject.ui.playlist.PlaylistViewModel
import com.example.baseproject.utils.LanguageConfig.changeLanguage
import com.example.baseproject.utils.Response
import com.example.baseproject.utils.SharedPrefs
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    @Inject
    lateinit var appNavigation: AppNavigation

    private lateinit var sharedPreferences: SharedPrefs
    private val viewModel: ProfileViewModel by viewModels()
    private val playListViewModel: PlaylistViewModel by activityViewModels()
    override fun getVM(): ProfileViewModel = viewModel

    private var profileImageUri: String? = null

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewModel.profileResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Failure -> {}
                is Response.Loading -> {}
                is Response.Success -> {
                    binding.userName.text = response.data.name
                    Glide.with(requireContext())
                        .load(response.data.profilePictureUrl.toUri())
                        .into(binding.imgProfile)
                    profileImageUri = response.data.profilePictureUrl
                }
            }
        }
        if (sharedPreferences.locale == "en") {
            binding.btnLanguage.setBackgroundResource(R.drawable.ic_gb_flag)
        } else {
            binding.btnLanguage.setBackgroundResource(R.drawable.ic_vi_flag)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = SharedPrefs(context)
    }

    override fun bindingAction() {
        super.bindingAction()
        viewModel.logOutResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Failure -> {}
                is Response.Loading -> {}
                is Response.Success -> {
                    appNavigation.openProfileToStartScreen()
                }
            }
        }
    }
//com.example.baseproject.service.MusicService@28da2de
    override fun setOnClick() {
        super.setOnClick()
        binding.apply {
            btnLogout.setOnClickListener {
                    Log.e("TAG", "Logout: ${playListViewModel}")
                playListViewModel.musicService.value?.apply {
                    reset()
                    stopForeground(true)
                    songLiveData.postValue(null)
                }
                viewModel.get()
                viewModel.data.observe(viewLifecycleOwner) {
                    Log.e("TAG", "Logout: Successed")
                    viewModel.pushCrossRef(it)
                    viewModel.logOut()
                }
            }
            btnEditProfile.setOnClickListener {
                EditProfileDialog(binding.userName.text.toString(), profileImageUri).show(
                    childFragmentManager,
                    "EditProfileDialog"
                )
            }
            btnLanguage.setOnClickListener {
                if (sharedPreferences.locale == "en") {
                    sharedPreferences.locale = "vi"
                    changeLanguage(requireContext(), "vi")
                    binding.btnLanguage.setBackgroundResource(R.drawable.ic_vi_flag)
                } else {
                    sharedPreferences.locale = "en"
                    changeLanguage(requireContext(), "en")
                    binding.btnLanguage.setBackgroundResource(R.drawable.ic_gb_flag)
                }
                (activity as MainActivity).recreate()
            }
        }
    }
}