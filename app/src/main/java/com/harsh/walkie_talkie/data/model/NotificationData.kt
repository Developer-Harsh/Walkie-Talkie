package com.harsh.walkie_talkie.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    val token: String? = null,
    val data: HashMap<String, String>? = null
)
