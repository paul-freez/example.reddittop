package com.testsite.reddittop.di

import com.testsite.reddittop.api.RedditAuthApi
import com.testsite.reddittop.data.source.ClientDataSource
import com.testsite.reddittop.data.source.local.ClientLocalDataSource
import com.testsite.reddittop.data.source.remote.ClientRemoteDataSource
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
    fun provideClientRemoteDataSource(api : RedditAuthApi) : ClientDataSource = ClientRemoteDataSource(api)

    @Provides
    @Singleton
    @Local
    fun provideClientLocalDataSource() : ClientDataSource = ClientLocalDataSource()

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Remote

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Local
}