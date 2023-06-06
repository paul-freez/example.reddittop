package com.testsite.reddittop.data.source.local

import com.testsite.reddittop.data.OAuthToken
import com.testsite.reddittop.data.source.ClientDataSource

class ClientLocalDataSource : ClientDataSource {
    override suspend fun authenticate(): OAuthToken {
        return OAuthToken.NONE // TODO: Implement proper caching
    }
}