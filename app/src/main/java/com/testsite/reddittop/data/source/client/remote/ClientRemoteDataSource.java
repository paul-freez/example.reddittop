package com.testsite.reddittop.data.source.client.remote;

import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.client.ClientDataSource;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;
import com.testsite.reddittop.utils.connectivity.ErrorHandler;

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
    private MutableLiveData<ErrorHandler> errorMessenger = new MutableLiveData<>();

    public ClientRemoteDataSource(RedditApi api) {
        this.api = api;
    }

    public LiveData<OAuthToken> getToken() {
        return token;
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
                errorMessenger.setValue(new ErrorHandler(t));
            }
        });
    }

    @Override
    public LiveData<ErrorHandler> getErrorMessenger() {
        return errorMessenger;
    }

    @Override
    public LiveData<Boolean> getLoaderHandler() {
        return loadingState;
    }
}
