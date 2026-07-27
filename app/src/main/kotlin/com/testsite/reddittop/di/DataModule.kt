package com.testsite.reddittop.di

import com.testsite.reddittop.data.client.model.local.OAuthTokenLocal
import com.testsite.reddittop.data.client.model.remote.OAuthTokenRemote
import com.testsite.reddittop.data.client.source.ClientDataSource
import com.testsite.reddittop.data.client.source.local.ClientLocalDataSourceImpl
import com.testsite.reddittop.data.client.source.remote.ClientRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindClientRemoteDataSource(impl: ClientRemoteDataSourceImpl): ClientDataSource<OAuthTokenRemote>

    @Binds
    @Singleton
    abstract fun bindClientLocalDataSource(impl: ClientLocalDataSourceImpl): ClientDataSource<OAuthTokenLocal>
}