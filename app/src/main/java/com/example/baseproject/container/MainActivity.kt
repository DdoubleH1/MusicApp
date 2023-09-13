package com.example.baseproject.container

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.baseproject.R
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.service.MusicService
import com.example.baseproject.ui.play.PlayFragmentDialog
import com.example.baseproject.ui.play.PlayViewModel
import com.example.baseproject.utils.LanguageConfig.changeLanguage
import com.example.baseproject.utils.PermissionsUtil
import com.example.baseproject.utils.PermissionsUtil.requestPermissions
import com.example.baseproject.utils.SharedPrefs
import com.example.core.base.BaseActivity
import com.example.core.pref.RxPreferences
import com.example.core.utils.NetworkConnectionManager
import com.example.core.utils.toast
import com.example.setting.ui.home.DemoDialogListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), DemoDialogListener {

    @Inject
    lateinit var appNavigation: AppNavigation

    @Inject
    lateinit var networkConnectionManager: NetworkConnectionManager

    @Inject
    lateinit var rxPreferences: RxPreferences

    private var sharedPreferences: SharedPrefs? = null

    private val viewModel: MainViewModel by viewModels()
    private val playViewModel: PlayViewModel by viewModels()
    override fun getVM() = viewModel
    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        appNavigation.bind(navHostFragment.navController)

        networkConnectionManager.isNetworkConnectedFlow
            .onEach {
                if (it) {
                    Log.d("ahihi", "onCreate: Network connected")
                } else {
                    Log.d("ahihi", "onCreate: Network disconnected")
                }
            }
            .launchIn(lifecycleScope)

        if (intent != null && intent.getBundleExtra("notification_bundle")?.getBoolean(MusicService.NEED_OPEN_DIALOG) == true) {
            PlayFragmentDialog().show(supportFragmentManager, "PlayFragmentDialog")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        val needOpenDialog = intent?.getBundleExtra("notification_bundle")?.getBoolean(MusicService.NEED_OPEN_DIALOG)
        Log.e("HoangDH", "onNewIntent: $needOpenDialog")
        if(needOpenDialog != null && needOpenDialog){
                if(playViewModel.dimissDialog.value == null || playViewModel.dimissDialog.value == true){
                    playViewModel.switchDismissDialog(false)
                    PlayFragmentDialog().show(supportFragmentManager, "PlayFragmentDialog")
                }
            }

        super.onNewIntent(intent)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            var i = 0
            val len = permissions.size
            while (i < len) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getSong()
                    return
                }
                i++
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = SharedPrefs(newBase!!)
        val languageCode: String = sharedPreferences!!.locale
        val context: Context = changeLanguage(newBase, languageCode)
        super.attachBaseContext(context)
    }

    override fun onStart() {
        super.onStart()
        networkConnectionManager.startListenNetworkState()
    }

    override fun onStop() {
        super.onStop()
        networkConnectionManager.stopListenNetworkState()
    }

    override fun onClickOk() {
        "Ok Activity".toast(this)
    }

    override fun onClickCancel() {
        "Cancel Activity".toast(this)
    }

}