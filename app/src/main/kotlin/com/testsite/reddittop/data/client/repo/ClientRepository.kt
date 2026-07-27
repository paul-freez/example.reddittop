package com.testsite.reddittop.data.client.repo

import com.testsite.reddittop.data.client.mapper.toLocal
import com.testsite.reddittop.data.client.model.local.OAuthTokenLocal
import com.testsite.reddittop.data.client.model.remote.OAuthTokenRemote
import com.testsite.reddittop.data.client.source.ClientDataSource
import com.testsite.reddittop.domain.DomainMapper.toDomain
import com.testsite.reddittop.domain.client.model.OAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClientRepository @Inject constructor(
    private val remoteSource: ClientDataSource<OAuthTokenRemote>,
    private val localDataSource: ClientDataSource<OAuthTokenLocal>
) {

    fun authenticate(): Flow<OAuthToken> = flow {
        val token = localDataSource.retrieveToken().toDomain().takeUnless(OAuthToken::isExpired)
            ?: retrieveNewToken().toDomain()

        emit(token)
    }

    private suspend fun retrieveNewToken(): OAuthTokenLocal = remoteSource.retrieveToken().toLocal()
        .also { token -> localDataSource.preserveToken(token) }
}
