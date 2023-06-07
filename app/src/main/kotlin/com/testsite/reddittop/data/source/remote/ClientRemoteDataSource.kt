package com.testsite.reddittop.data.source.remote

import com.testsite.reddittop.api.RedditAuthApi
import com.testsite.reddittop.data.OAuthToken
import com.testsite.reddittop.data.source.ClientDataSource
import java.util.UUID

class ClientRemoteDataSource constructor(private val api: RedditAuthApi) :
    ClientDataSource {
    override suspend fun retrieveToken(): OAuthToken =
        api.getOAuthToken(deviceId = UUID.randomUUID().toString())

    override suspend fun preserveToken(token: OAuthToken) {
    }
}