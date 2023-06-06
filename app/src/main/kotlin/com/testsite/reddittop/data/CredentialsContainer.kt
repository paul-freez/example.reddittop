package com.testsite.reddittop.data

import com.testsite.reddittop.api.RedditAuthApi
import okhttp3.Credentials

class CredentialsContainer(private val token: OAuthToken? = null) {

    fun get() = token?.fullToken ?: Credentials.basic(RedditAuthApi.CLIENT_ID, "");
}