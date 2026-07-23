package com.testsite.reddittop.data.source.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testsite.reddittop.BuildConfig;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;
import com.testsite.reddittop.utils.connectivity.ConnectivityInterceptor;
import com.testsite.reddittop.utils.connectivity.EnumRetrofitConverterFactory;
import com.testsite.reddittop.utils.connectivity.ErrorHandler;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by paulf
 */
@Deprecated
public class RedditApiFactory {

    public static RedditApi create(String url, final @NonNull LiveData<OAuthToken> token) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor())
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request requestWithUserAgent = originalRequest.newBuilder()
                                // Updating User-Agent according to Reddit API rules
                                .header("User-Agent", String.format(Locale.US, "android:%s:v:%s (by %s)",
                                        BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME, BuildConfig.USER_NAME))
                                // Authorization
                                .header("Authorization", (token.getValue() == null ?
                                        Credentials.basic(BuildConfig.CLIENT_ID, "")
                                        : token.getValue().getToken()))
                                .build();
                        return chain.proceed(requestWithUserAgent);
                    }
                }).build();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(new ErrorHandler.ErrorHandlingCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new EnumRetrofitConverterFactory())
                .build();

        return retrofit.create(RedditApi.class);
    }
}
