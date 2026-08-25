package com.testsite.reddittop.di

import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.testsite.reddittop.BuildConfig
import com.testsite.reddittop.components.connectivity.ConnectivityInterceptor
import com.testsite.reddittop.components.connectivity.ErrorHandlingCallAdapterFactory
import com.testsite.reddittop.data.client.RedditAuthApi
import com.testsite.reddittop.data.client.model.local.OAuthTokenLocal
import com.testsite.reddittop.data.client.source.ClientDataSource
import com.testsite.reddittop.data.posts.RedditApi
import com.testsite.reddittop.domain.DomainMapper.toDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideConnectivityInterceptor(connectivityManager: ConnectivityManager): ConnectivityInterceptor =
        ConnectivityInterceptor(connectivityManager)

    @Provides
    @UserAgentInterceptor
    @Singleton
    fun provideUserAgentInterceptor(): Interceptor = provideHeaderInterceptor(
        "User-Agent",
        "android:${BuildConfig.APPLICATION_ID}:v:${BuildConfig.VERSION_NAME} (by ${BuildConfig.USER_NAME})"
    )

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        prettyPrint = true
    }

    @Provides
    @Singleton
    fun provideKotlinxSerializationConverterFactory(json: Json): Converter.Factory {
        val contentType = "application/json".toMediaType()
        return json.asConverterFactory(contentType)
    }

    @Provides
    @Auth
    @Singleton
    fun provideAuthTokenInterceptor(local: ClientDataSource<OAuthTokenLocal>): Interceptor {
        val token = runBlocking { local.retrieveToken().toDomain() }
        val authHeader = when {
            token.isExpired() -> Credentials.basic(BuildConfig.CLIENT_ID, "")
            else -> token.fullToken
        }
        return provideHeaderInterceptor("Authorization", authHeader)
    }

    @Provides
    @Singleton
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        connectivityInterceptor: ConnectivityInterceptor,
        @UserAgentInterceptor userAgentInterceptor: Interceptor,
        @Auth authInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(connectivityInterceptor)
        .addInterceptor(userAgentInterceptor)
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRedditApi(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): RedditApi =
        Retrofit.Builder()
            .baseUrl(RedditApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ErrorHandlingCallAdapterFactory())
            .build()
            .create(RedditApi::class.java)

    @Provides
    @Singleton
    fun provideRedditAuthApi(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): RedditAuthApi =
        Retrofit.Builder()
            .baseUrl(RedditAuthApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ErrorHandlingCallAdapterFactory())
            .build().create(RedditAuthApi::class.java)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserAgentInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NonAuth

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Auth

    private fun provideHeaderInterceptor(headerName: String, headerValue: String) =
        Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header(
                        headerName,
                        headerValue
                    )
                    .build()
            )
        }
}