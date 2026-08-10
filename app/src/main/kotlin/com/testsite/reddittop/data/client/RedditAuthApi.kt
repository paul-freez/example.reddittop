package com.testsite.reddittop.data.client

import com.testsite.reddittop.data.client.model.remote.OAuthTokenRemote
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RedditAuthApi {

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    suspend fun getOAuthToken(
        @Field("grant_type", encoded = true) type: String = GRANT_CLIENT,
        @Field("device_id") deviceId: String
    ): OAuthTokenRemote

    companion object {
        const val GRANT_CLIENT = "https://oauth.reddit.com/grants/installed_client"

        const val BASE_URL = "https://reddit.com"
    }
}