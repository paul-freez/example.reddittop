package com.testsite.reddittop.data.client.source.local

import androidx.datastore.core.DataStore
import com.testsite.reddittop.TokenOuterClass
import com.testsite.reddittop.copy
import com.testsite.reddittop.data.client.model.local.OAuthTokenLocal
import com.testsite.reddittop.data.client.source.ClientDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ClientLocalDataSourceImpl @Inject constructor(private val dataStore: DataStore<TokenOuterClass.Token>) : ClientDataSource<OAuthTokenLocal> {
    override suspend fun retrieveToken(): OAuthTokenLocal {
        val token = dataStore.data.first()
        return OAuthTokenLocal(
            fullToken = token.token,
            expirationTime = token.timeout,
            type = token.type
        )
    }

    override suspend fun preserveToken(token: OAuthTokenLocal) {
        dataStore.updateData { dsToken ->
            dsToken.copy {
                this.token = token.fullToken
                this.timeout = token.expirationTime
                this.type = token.type
            }
        }
    }
}
