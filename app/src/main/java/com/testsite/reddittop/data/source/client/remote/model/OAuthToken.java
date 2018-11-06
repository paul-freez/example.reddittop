package com.testsite.reddittop.data.source.client.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

/**
 * Created by paulf
 */
@SuppressWarnings("unused")
public class OAuthToken {

    @SerializedName("access_token")
    @Expose
    private String token;

    @SerializedName("expires_in")
    @Expose
    private long expiresIn;

    @SerializedName("token_type")
    @Expose
    private String type;

    private final long creationTime;

    public OAuthToken() {
        // Response time and creation time shouldn't be much different
        creationTime = System.currentTimeMillis();
    }

    /**
     * Returns token representation.
     * <p/><i>You can also use {@link #toString()} instead</i>
     */
    public String getToken() {
        return type + " " + token;
    }

    public boolean isExpired() {
        return creationTime + TimeUnit.SECONDS.toMillis(expiresIn) - System.currentTimeMillis() <= 0 ;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "%s %s expires in %d minutes", type, token, TimeUnit.SECONDS.toMinutes(expiresIn));
    }
}
