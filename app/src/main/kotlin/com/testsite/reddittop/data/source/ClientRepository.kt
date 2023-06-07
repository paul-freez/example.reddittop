package com.testsite.reddittop.data.source

import com.testsite.reddittop.data.OAuthToken
import com.testsite.reddittop.di.DataModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClientRepository @Inject constructor(
    @DataModule.Remote private val remoteSource: ClientDataSource,
    @DataModule.Local private val localDataSource: ClientDataSource
) {

    fun authenticate(): Flow<OAuthToken> = flow {
        val token = localDataSource.retrieveToken()
        emit(
            if (token.isExpired) {
                retrieveNewToken()
            } else {
                token
            }
        )
    }

    private suspend fun retrieveNewToken() : OAuthToken = remoteSource.retrieveToken().also { token -> localDataSource.preserveToken(token) }
}