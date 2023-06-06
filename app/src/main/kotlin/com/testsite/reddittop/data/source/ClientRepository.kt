package com.testsite.reddittop.data.source

import com.testsite.reddittop.data.OAuthToken
import com.testsite.reddittop.di.DataModule
import javax.inject.Inject

class ClientRepository @Inject constructor(
    @DataModule.Remote private val remoteSource: ClientDataSource,
    @DataModule.Local private val localDataSource: ClientDataSource
) : ClientDataSource {

    override suspend fun authenticate(): OAuthToken {
        val token = localDataSource.authenticate()
        // TODO: Provide better caching
        return if (token.isExpired) {
            remoteSource.authenticate()
        } else {
            token
        }
    }
}