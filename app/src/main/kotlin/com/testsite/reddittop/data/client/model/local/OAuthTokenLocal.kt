package com.testsite.reddittop.data.client.model.local

data class OAuthTokenLocal(
    val fullToken: String,
    val expirationTime: Long,
    val type: String
)
