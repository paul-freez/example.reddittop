package com.testsite.reddittop.api

import com.testsite.reddittop.data.OAuthToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RedditAuthApi {

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    suspend fun getOAuthToken(
        @Field("grant_type", encoded = true) type: String = GRANT_CLIENT,
        @Field("device_id") deviceId: String
    ): OAuthToken

    companion object {
        const val GRANT_CLIENT = "https://oauth.reddit.com/grants/installed_client"

        const val OAUTH_URL = "https://oauth.reddit.com"
    }
}