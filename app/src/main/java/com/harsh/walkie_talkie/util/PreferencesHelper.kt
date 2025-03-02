package com.harsh.walkie_talkie.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson


object PreferencesHelper {
    private const val PREFS_NAME = "walkie_prefs"
    private const val KEY_SETTLED = "settled_app"
    const val KEY_PROFILE = "profile"

    fun isKeyExist(context: Context, key: String): Boolean {
        val map: Map<String, *> = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).all
        if (map.containsKey(key)) {
            return true
        } else {
            Log.e(
                "EasyPrefs",
                "Not found any key which belongs to string you have provided $key"
            )
            return false
        }
    }

    fun <T> setProfile(context: Context, `object`: T) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PROFILE, Gson().toJson(`object`)).apply()
    }

    fun <T> getProfile(context: Context, classType: Class<T>?): T? {
        if (isKeyExist(context, KEY_PROFILE)) {
            val objectString: String = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_PROFILE, "").toString()
            return Gson().fromJson(objectString, classType)
        }
        return null
    }

    fun <T> setObject(context: Context, key: String, `object`: T) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(key, Gson().toJson(`object`)).apply()
    }

    fun <T> getObject(context: Context, key: String, classType: Class<T>?): T? {
        if (isKeyExist(context, key)) {
            val objectString: String = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(key, "").toString()
            return Gson().fromJson(objectString, classType)
        }
        return null
    }

    fun setSettled(context: Context, permission: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SETTLED, permission).apply()
    }

    fun getSettled(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SETTLED, false)
    }
}
