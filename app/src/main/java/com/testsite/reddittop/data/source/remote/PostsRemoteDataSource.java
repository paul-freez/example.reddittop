package com.testsite.reddittop.data.source.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testsite.reddittop.BuildConfig;
import com.testsite.reddittop.data.source.PostsDataSource;
import com.testsite.reddittop.data.source.remote.api.RedditApi;
import com.testsite.reddittop.data.source.remote.api.model.OAuthToken;
import com.testsite.reddittop.data.source.remote.api.model.RedditTopPostsResponse;
import com.testsite.reddittop.utils.EnumRetrofitConverterFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.NonNull;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by paulf
 */
public class PostsRemoteDataSource implements PostsDataSource {

    private RedditApi api;

    private OAuthToken token;

    public PostsRemoteDataSource() {
        initAPI(true);
    }

    /**
     * Init Reddit API using basic url or OAuth for future API requests
     */
    private void initAPI(boolean basic) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request requestWithUserAgent = originalRequest.newBuilder()
                                // Updating User-Agent according to Reddit API rules
                                .header("User-Agent", String.format(Locale.US, "android:%s:v:%s (by AnonymousUser)",
                                        BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME))
                                // Authorization
                                .header("Authorization", (token == null ?
                                        Credentials.basic(RedditApi.CLIENT_ID, "")
                                        : token.toString()))
                                .build();
                        return chain.proceed(requestWithUserAgent);
                    }
                }).build();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(basic ? RedditApi.BASE_URL : RedditApi.OAUTH_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new EnumRetrofitConverterFactory())
                .build();
        api = retrofit.create(RedditApi.class);
    }


    @Override
    public void authenticate(@NonNull final OAuthCallback callback) {
        if (token == null) {
            api.getTokenCredentials(RedditApi.GRANT_CLIENT, UUID.randomUUID().toString()).enqueue(new Callback<OAuthToken>() {
                @Override
                public void onResponse(Call<OAuthToken> call, retrofit2.Response<OAuthToken> response) {
                    if (response.isSuccessful()) {
                        token = response.body();
                        // Switching to OAuth requests
                        initAPI(false);

                        callback.onAuthApproved();
                    } else {
                        onFailure(call, new Exception("Authorisation failed!"));
                    }
                }

                @Override
                public void onFailure(Call<OAuthToken> call, Throwable t) {
                    // TODO: Handle failure
                }
            });
        } else {
            callback.onAuthApproved();
        }
    }

    @Override
    public void getPosts(int page, int quantity, @NonNull LoadPostsCallback callback) {
        api.getNextTopPosts(RedditApi.TimeFilter.DAY, "", 0, true, quantity)
                .enqueue(new Callback<RedditTopPostsResponse>() {
                    @Override
                    public void onResponse(Call<RedditTopPostsResponse> call, retrofit2.Response<RedditTopPostsResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onPostsLoaded(response.body().getData().getContent());
                        } else {
                            onFailure(call, new Exception("Request failed: " + response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<RedditTopPostsResponse> call, Throwable t) {
                        // TODO:
                    }
                });
    }

    @Override
    public void refreshPosts() {
        // Repository already handles this logic
    }
}
