package com.example.baseproject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
//import com.example.core.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication @Inject constructor() : Application() {

    companion object {
        const val CHANNEL_ID = "music_channel_id"
    }

    override fun onCreate() {
        super.onCreate()
        createNotification()
    }
    private fun createNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName = getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            channel.setSound(null, null)
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}