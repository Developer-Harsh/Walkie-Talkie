package com.harsh.walkie_talkie.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver : DefaultLifecycleObserver {

    companion object {
        var isAppInForeground: Boolean = false
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        isAppInForeground = true
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        isAppInForeground = true
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        isAppInForeground = false
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        isAppInForeground = true
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        isAppInForeground = false
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        isAppInForeground = false
    }
}