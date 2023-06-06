package com.testsite.reddittop.data.source

import com.testsite.reddittop.data.OAuthToken

interface ClientDataSource {
    suspend fun authenticate() : OAuthToken
}