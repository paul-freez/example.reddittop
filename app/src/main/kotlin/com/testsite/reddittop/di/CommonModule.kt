package com.testsite.reddittop.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import saschpe.android.customtabs.CustomTabsHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Singleton
    @Provides
    fun provideCustomTabsHelper() = CustomTabsHelper()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
}