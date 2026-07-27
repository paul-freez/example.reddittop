package com.testsite.reddittop.data.client.source

interface ClientDataSource<T> {
    suspend fun retrieveToken() : T
    suspend fun preserveToken(token : T)
}
