package com.testsite.reddittop.data.source.client.remote;

import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.client.ClientDataSource;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;

import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by paulf
 */
public class ClientRemoteDataSource implements ClientDataSource {

    private RedditApi api;

    private MutableLiveData<OAuthToken> token = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingState = new MutableLiveData<>();

    public ClientRemoteDataSource(RedditApi api) {
        this.api = api;
    }

    public LiveData<OAuthToken> getToken() {
        return token;
    }

    public LiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    @Override
    public void authenticate() {
        // TODO: Check toke expire before fetching new

        loadingState.setValue(true);

        api.getTokenCredentials(RedditApi.GRANT_CLIENT, UUID.randomUUID().toString()).enqueue(new Callback<OAuthToken>() {
            @Override
            public void onResponse(Call<OAuthToken> call, retrofit2.Response<OAuthToken> response) {
                loadingState.setValue(false);

                if (response.isSuccessful()) {
                    token.setValue(response.body());

                } else {
                    onFailure(call, new Exception("Authorisation failed!"));
                }
            }

            @Override
            public void onFailure(Call<OAuthToken> call, Throwable t) {
                loadingState.setValue(false);
                // TODO: Handle failure
            }
        });
    }
}
