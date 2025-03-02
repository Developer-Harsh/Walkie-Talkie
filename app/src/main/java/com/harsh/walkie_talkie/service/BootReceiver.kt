package com.harsh.walkie_talkie.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.harsh.walkie_talkie.ui.component.startFloatingService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            context.startFloatingService()
        }
    }
}
