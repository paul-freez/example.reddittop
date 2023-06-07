package com.testsite.reddittop.di

import com.google.gson.Gson
import com.testsite.reddittop.BuildConfig
import com.testsite.reddittop.api.RedditApi
import com.testsite.reddittop.api.RedditAuthApi
import com.testsite.reddittop.data.CredentialsContainer
import com.testsite.reddittop.data.TokenManager
import com.testsite.reddittop.utils.connectivity.ConnectivityInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {

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
    @Singleton
    fun provideCredentials(tokenManager: TokenManager): CredentialsContainer = CredentialsContainer(tokenManager)

    @Provides
    @Auth
    @Singleton
    fun provideAuthTokenInterceptor(@Auth token: CredentialsContainer): Interceptor =
        provideHeaderInterceptor("Authorization", token.get())

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

    companion object {

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

}