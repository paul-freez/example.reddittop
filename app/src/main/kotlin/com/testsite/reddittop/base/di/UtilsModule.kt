package com.testsite.reddittop.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import saschpe.android.customtabs.CustomTabsHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Singleton
    @Provides
    fun provideCustomTabsHelper() = CustomTabsHelper()
}