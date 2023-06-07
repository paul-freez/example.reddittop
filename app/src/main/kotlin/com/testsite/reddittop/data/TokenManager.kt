package com.testsite.reddittop.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.testsite.reddittop.TokenOuterClass.Token
import com.testsite.reddittop.copy
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * Manage Token here.
 * Using ProtoBuf we're able to save and retrieve token data and transform it back and forth to default OAuthToken class
 */
class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.tokenDataStore: DataStore<Token> by dataStore(
        fileName = "token.pb",
        serializer = TokenSerializer,
        corruptionHandler = ReplaceFileCorruptionHandler { Token.getDefaultInstance() }
    )

    val token: Flow<OAuthToken> = context.tokenDataStore.data.map { token ->
        OAuthToken(
            token = token.token,
            expiresIn = token.timeout,
            type = token.type,
            creationTime = token.creationTime
        )
    }

    suspend fun updateToken(oAuthToken: OAuthToken) {
        context.tokenDataStore.updateData { t ->
            t.copy {
                token = oAuthToken.token
                timeout = oAuthToken.expiresIn
                type = oAuthToken.type
                creationTime = oAuthToken.creationTime
            }
        }
    }

    private object TokenSerializer : Serializer<Token> {
        override val defaultValue: Token
            get() = Token.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): Token {
            try {
                return Token.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(t: Token, output: OutputStream) = t.writeTo(output)
    }
}