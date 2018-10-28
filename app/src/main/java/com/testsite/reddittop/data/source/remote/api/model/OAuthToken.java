package com.testsite.reddittop.data.source.remote.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
        return type + " " + token;
    }
}
