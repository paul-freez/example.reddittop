package com.testsite.reddittop.data.client.source.local.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.testsite.reddittop.TokenOuterClass
import java.io.InputStream
import java.io.OutputStream

object TokenSerializer : Serializer<TokenOuterClass.Token> {
    override val defaultValue: TokenOuterClass.Token
        get() = TokenOuterClass.Token.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TokenOuterClass.Token {
        try {
            return TokenOuterClass.Token.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: TokenOuterClass.Token, output: OutputStream) = t.writeTo(output)
}
