package com.harsh.walkie_talkie.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("uid") val uid: String,
    @SerialName("token") val token: String
)

