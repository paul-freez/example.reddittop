package com.testsite.reddittop.data

import com.testsite.reddittop.BuildConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Credentials

class CredentialsContainer(private val tokenManager: TokenManager) {

    private var token: OAuthToken? = null

    init {
        // Since our token should be observed all the time, GlobalScope should be fine
        GlobalScope.launch {
            tokenManager.token.collect {
                token = it
            }
        }
    }

    fun get() = when {
        token == null ||
                token!!.isExpired -> Credentials.basic(BuildConfig.CLIENT_ID, "")

        else -> token!!.fullToken
    }
}