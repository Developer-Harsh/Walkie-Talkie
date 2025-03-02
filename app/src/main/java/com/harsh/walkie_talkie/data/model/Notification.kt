package com.harsh.walkie_talkie.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val message: NotificationData? = null
)
