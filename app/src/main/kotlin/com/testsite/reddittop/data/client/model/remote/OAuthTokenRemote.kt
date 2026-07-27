package com.testsite.reddittop.data.client.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OAuthTokenRemote(
    @SerialName("access_token")
    val token: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("token_type")
    val type: String
)
