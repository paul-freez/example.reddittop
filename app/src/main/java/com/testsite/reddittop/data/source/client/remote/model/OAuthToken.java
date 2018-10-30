package com.testsite.reddittop.data.source.client.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

/**
 * Created by paulf
 */
public class OAuthToken {

    @SerializedName("access_token")
    @Expose
    private String token;

    @SerializedName("expires_in")
    @Expose
    private long expiresIn; // TODO: Make use of this field

    @SerializedName("token_type")
    @Expose
    private String type;

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "%s %s expires in %d minutes", type, token, TimeUnit.SECONDS.toMinutes(expiresIn));
    }
}
