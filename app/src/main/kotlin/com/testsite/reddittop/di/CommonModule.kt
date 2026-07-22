package com.testsite.reddittop.di

import android.content.Context
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.testsite.reddittop.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideCustomTabIntent(@ApplicationContext context: Context): CustomTabsIntent =
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(context.getColor(R.color.colorPrimary))
                    .build()
            )
            .build()
}