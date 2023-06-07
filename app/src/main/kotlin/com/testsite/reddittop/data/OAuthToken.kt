package com.testsite.reddittop.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

data class OAuthToken(
    @SerializedName("access_token") @Expose
    val token: String,
    @SerializedName("expires_in") @Expose
    val expiresIn: Long,
    @SerializedName("token_type") @Expose
    val type: String,
    val creationTime: Long = System.currentTimeMillis()
) {

    // Response time and creation time shouldn't be much different
    private val expirationTime: Long = creationTime + TimeUnit.SECONDS.toMillis(expiresIn)

    val fullToken: String
        get() = "$type $token"

    val isExpired: Boolean
        get() = expirationTime <= System.currentTimeMillis()

    override fun toString(): String {
        return "$fullToken expires in ${TimeUnit.SECONDS.toMinutes(expiresIn)}"
    }
}
