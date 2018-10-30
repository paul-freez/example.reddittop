package com.testsite.reddittop.data.source.api;

import com.google.gson.annotations.SerializedName;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;
import com.testsite.reddittop.data.source.post.remote.model.RedditListingResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by paulf
 */
public interface RedditApi {

    String CLIENT_ID = "your_client_id  ";

    String GRANT_CLIENT = "https://oauth.reddit.com/grants/installed_client";

    String BASE_URL = "https://www.reddit.com";
    String OAUTH_URL = "https://oauth.reddit.com";

    String TOKEN = "/api/v1/access_token";

    String POSTS_TOP = "/top.json";

    @FormUrlEncoded
    @POST(TOKEN)
    Call<OAuthToken> getTokenCredentials(@Field(value = "grant_type", encoded = true) String type, @Field("device_id") String deviceId);

    @GET(POSTS_TOP)
    Call<RedditListingResponse> getNextTopPosts(@Query("t")TimeFilter time,
                                                @Query("after") String lastElement,
                                                @Query("limit") int size);

    @GET(POSTS_TOP)
    Call<RedditListingResponse> getPreviousTopPosts(@Query("t")TimeFilter time,
                                                    @Query("before") String lastElement,
                                                    @Query("limit") int size);

    enum TimeFilter {
        @SerializedName("all")
        ALL,
        @SerializedName("year")
        YEAR,
        @SerializedName("month")
        MONTH,
        @SerializedName("week")
        WEEK,
        @SerializedName("day")
        DAY,
        @SerializedName("hour")
        HOUR
    }
}
