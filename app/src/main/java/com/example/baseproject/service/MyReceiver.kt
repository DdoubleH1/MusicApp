package com.example.baseproject.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("HoangDH", "onReceive")
        val action = intent?.action
        val serviceIntent = Intent(context, MusicService::class.java)
        serviceIntent.action = action
        context!!.startService(serviceIntent)
    }
}