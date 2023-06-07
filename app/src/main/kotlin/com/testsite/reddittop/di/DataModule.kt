package com.testsite.reddittop.di

import com.testsite.reddittop.api.RedditApi
import com.testsite.reddittop.api.RedditAuthApi
import com.testsite.reddittop.data.TokenManager
import com.testsite.reddittop.data.source.ClientDataSource
import com.testsite.reddittop.data.source.ClientRepository
import com.testsite.reddittop.data.source.FeedRepository
import com.testsite.reddittop.data.source.local.ClientLocalDataSource
import com.testsite.reddittop.data.source.remote.ClientRemoteDataSource
import com.testsite.reddittop.data.source.remote.FeedRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    @Remote
    fun provideClientRemoteDataSource(api: RedditAuthApi): ClientDataSource =
        ClientRemoteDataSource(api)

    @Provides
    @Singleton
    @Local
    fun provideClientLocalDataSource(tokenManager: TokenManager): ClientDataSource = ClientLocalDataSource(tokenManager)

    @Provides
    @Singleton
    fun provideClientRepo(
        @Remote remote: ClientDataSource,
        @Local local: ClientDataSource
    ): ClientRepository = ClientRepository(remote, local)

    @Provides
    @Singleton
    fun provideFeedRemoteDataSource(api: RedditApi): FeedRemoteDataSource =
        FeedRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideFeedRepo(remote : FeedRemoteDataSource) : FeedRepository = FeedRepository(remote)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Remote

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Local
}