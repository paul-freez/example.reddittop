package com.testsite.reddittop.data.source.local

import com.testsite.reddittop.data.OAuthToken
import com.testsite.reddittop.data.TokenManager
import com.testsite.reddittop.data.source.ClientDataSource
import kotlinx.coroutines.flow.first

class ClientLocalDataSource(private val manager: TokenManager) : ClientDataSource {
    override suspend fun retrieveToken(): OAuthToken {
        return manager.token.first()
    }

    override suspend fun preserveToken(token: OAuthToken) {
        manager.updateToken(token)
    }
}