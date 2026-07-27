package com.testsite.reddittop.data.client.mapper

import com.testsite.reddittop.data.client.model.local.OAuthTokenLocal
import com.testsite.reddittop.data.client.model.remote.OAuthTokenRemote
import java.util.concurrent.TimeUnit

fun OAuthTokenRemote.toLocal(): OAuthTokenLocal {
    val creationTime = System.currentTimeMillis()
    return OAuthTokenLocal(
        fullToken = "$type $token",
        expirationTime = creationTime + TimeUnit.SECONDS.toMillis(expiresIn),
        type = type
    )
}
