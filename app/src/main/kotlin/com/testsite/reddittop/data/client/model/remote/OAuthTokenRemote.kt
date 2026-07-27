package com.testsite.reddittop.data.client.model.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OAuthTokenRemote(
    @SerializedName("access_token") @Expose
    val token: String,
    @SerializedName("expires_in") @Expose
    val expiresIn: Long,
    @SerializedName("token_type") @Expose
    val type: String
)
