package com.example.baseproject.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


object PermissionsUtil {
    fun checkPermissions(context: Context?, vararg permissions: String?): Boolean {
        for (permission in permissions) {
            if (!checkPermission(context, permission)) {
                return false
            }
        }
        return true
    }

    private fun checkPermission(context: Context?, permission: String?): Boolean {
        return (ActivityCompat.checkSelfPermission(context!!, permission!!)
                == PackageManager.PERMISSION_GRANTED)
    }

    fun requestPermissions(
        activity: Activity?, requestCode: Int,
        vararg permissions: String?
    ) {
        ActivityCompat.requestPermissions(activity!!, permissions, requestCode)
    }
}