package com.example.baseproject.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPrefs(var context: Context) {
    private var LOCALE_KEY = "locale"
    private var setback_locale = "en"
    private var database_name = "database"
    var editor: SharedPreferences.Editor =
        context.getSharedPreferences(database_name, Context.MODE_PRIVATE).edit()
    var preferences: SharedPreferences =
        context.getSharedPreferences(database_name, Context.MODE_PRIVATE)

    var locale: String
        get() = if (preferences.contains(LOCALE_KEY)) {
            preferences.getString(LOCALE_KEY, "")!!
        } else {
            setback_locale
        }
        set(lang) {
            editor.putString(LOCALE_KEY, lang)
            editor.apply()
        }
}