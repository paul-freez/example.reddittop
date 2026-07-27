package com.testsite.reddittop.data.client.source.remote

import com.testsite.reddittop.data.client.RedditAuthApi
import com.testsite.reddittop.data.client.model.remote.OAuthTokenRemote
import com.testsite.reddittop.data.client.source.ClientDataSource
import java.util.UUID
import javax.inject.Inject

class ClientRemoteDataSourceImpl @Inject constructor(private val api: RedditAuthApi) :
    ClientDataSource<OAuthTokenRemote> {
    override suspend fun retrieveToken(): OAuthTokenRemote =
        api.getOAuthToken(deviceId = UUID.randomUUID().toString())

    override suspend fun preserveToken(token: OAuthTokenRemote) {
    }
}
