package com.testsite.reddittop.di

import com.google.gson.Gson
import com.testsite.reddittop.BuildConfig
import com.testsite.reddittop.data.client.RedditAuthApi
import com.testsite.reddittop.data.client.model.local.OAuthTokenLocal
import com.testsite.reddittop.data.client.source.ClientDataSource
import com.testsite.reddittop.data.posts.RedditApi
import com.testsite.reddittop.domain.DomainMapper.toDomain
import com.testsite.reddittop.utils.connectivity.ConnectivityInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideConnectivityInterceptor(): ConnectivityInterceptor = ConnectivityInterceptor()

    @Provides
    @UserAgentInterceptor
    @Singleton
    fun provideUserAgentInterceptor(): Interceptor = provideHeaderInterceptor(
        "User-Agent",
        "android:${BuildConfig.APPLICATION_ID}:v:${BuildConfig.VERSION_NAME} (by ${BuildConfig.USER_NAME})"
    )

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    @Auth
    fun provideRedditBaseUrl(): String = RedditApi.BASE_URL

    @Singleton
    @Provides
    @NonAuth
    fun provideRedditAuthUrl(): String = RedditAuthApi.OAUTH_URL

    @Provides
    @Auth
    @Singleton
    fun provideAuthTokenInterceptor(local: ClientDataSource<OAuthTokenLocal>): Interceptor =
        Interceptor { chain ->
            val token = runBlocking { local.retrieveToken().toDomain() }
            val authHeader = when {
                token.isExpired() -> Credentials.basic(BuildConfig.CLIENT_ID, "")
                else -> token.fullToken
            }
            chain.proceed(
                chain.request().newBuilder()
                    .header("Authorization", authHeader)
                    .build()
            )
        }

    @Provides
    @Singleton
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        connectivityInterceptor: ConnectivityInterceptor,
        @UserAgentInterceptor userAgentInterceptor: Interceptor,
        @Auth authInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(connectivityInterceptor)
        .addInterceptor(userAgentInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    @Provides
    @Singleton
    @Auth
    fun provideAuthRetrofit(
        @Auth url: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        // Do we really need complicated error handling? Can't we just rely on Exceptions?
//        .addCallAdapterFactory(new ErrorHandler.ErrorHandlingCallAdapterFactory())
        // TODO: This is probably redundant
//        .addConverterFactory(new EnumRetrofitConverterFactory())
        .build()

    @Provides
    @Singleton
    @NonAuth
    fun provideRetrofit(
        @NonAuth url: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        // Do we really need complicated error handling? Can't we just rely on Exceptions?
//        .addCallAdapterFactory(new ErrorHandler.ErrorHandlingCallAdapterFactory())
        // TODO: This is probably redundant
//        .addConverterFactory(new EnumRetrofitConverterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRedditApi(@Auth retrofit: Retrofit): RedditApi =
        retrofit.create(RedditApi::class.java)

    @Provides
    @Singleton
    fun provideRedditAuthApi(@NonAuth retrofit: Retrofit): RedditAuthApi =
        retrofit.create(RedditAuthApi::class.java)

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