package com.testsite.reddittop.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

data class OAuthToken(
    @SerializedName("access_token") @Expose
    private val token: String,
    @SerializedName("expires_in") @Expose
    private val expiresIn: Long,
    @SerializedName("token_type") @Expose
    private val type: String
) {

    // Response time and creation time shouldn't be much different
    private val creationTime: Long = System.currentTimeMillis()
    private val expirationTime: Long = creationTime + TimeUnit.SECONDS.toMillis(expiresIn)

    val fullToken: String
        get() = "$type $token"

    val isExpired: Boolean
        get() = expirationTime <= System.currentTimeMillis()

    override fun toString(): String {
        return "$fullToken expires in ${TimeUnit.SECONDS.toMinutes(expiresIn)}"
    }

    companion object {
        val NONE = OAuthToken("none", -1, "none")
    }
}
