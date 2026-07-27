package com.testsite.reddittop.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.testsite.reddittop.TokenOuterClass
import com.testsite.reddittop.components.concurrency.AppDispatchers
import com.testsite.reddittop.data.client.source.local.datastore.TokenSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideTokenCorruptionHandler(): ReplaceFileCorruptionHandler<TokenOuterClass.Token> =
        ReplaceFileCorruptionHandler { TokenOuterClass.Token.getDefaultInstance() }

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context,
        coroutineScope: CoroutineScope,
        appDispatchers: AppDispatchers,
        tokenCorruptionHandler: ReplaceFileCorruptionHandler<TokenOuterClass.Token>,
    ): DataStore<TokenOuterClass.Token> = DataStoreFactory.create(
        serializer = TokenSerializer,
        corruptionHandler = tokenCorruptionHandler,
        scope = coroutineScope + appDispatchers.IO,
        produceFile = { context.dataStoreFile(FILE_TOKEN_PROTO) }
    )

    private const val FILE_TOKEN_PROTO = "token.pb"
}