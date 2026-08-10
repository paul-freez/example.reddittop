package com.testsite.reddittop.domain.client.model

import java.util.concurrent.TimeUnit

data class OAuthToken(
    val fullToken: String,
    val expirationTime: Long,
    val type: String
) {
    fun isExpired(): Boolean = expirationTime <= System.currentTimeMillis()

    override fun toString(): String {
        val remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(expirationTime - System.currentTimeMillis())
        return "this token expires in $remainingMinutes minutes"
    }
}
