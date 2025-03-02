package com.harsh.walkie_talkie.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val message: String? = "",
    val error: String? = ""
)

@Serializable
data class DataResponse<T>(
    val success: T? = null,
    val error: String? = ""
)