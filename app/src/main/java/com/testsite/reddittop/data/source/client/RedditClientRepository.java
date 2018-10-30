package com.testsite.reddittop.data.source.client;

import com.testsite.reddittop.UIListing;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.client.remote.ClientRemoteDataSource;
import com.testsite.reddittop.data.source.client.remote.ClientRemoteDataSourceFactory;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

/**
 * Created by paulf
 */
public class RedditClientRepository implements ClientRepository {

    private static RedditClientRepository INSTANCE;

    private RedditApi api;

    private RedditClientRepository(RedditApi api) {
        this.api = api;
    }

    public static RedditClientRepository getInstance(RedditApi api) {
        if (INSTANCE == null) {
            INSTANCE = new RedditClientRepository(api);
        }

        return INSTANCE;
    }

    @Override
    public UIListing<OAuthToken> authenticate() {
        ClientRemoteDataSourceFactory sourceFactory = new ClientRemoteDataSourceFactory(api);
        sourceFactory.create().authenticate();

        LiveData<OAuthToken> tokenLiveData = Transformations.switchMap(sourceFactory.getSourceLiveData(), new Function<ClientRemoteDataSource, LiveData<OAuthToken>>() {
            @Override
            public LiveData<OAuthToken> apply(ClientRemoteDataSource input) {
                return input.getToken();
            }
        });

        LiveData<Boolean> loadingStateLiveData = Transformations.switchMap(sourceFactory.getSourceLiveData(), new Function<ClientRemoteDataSource, LiveData<Boolean>>() {
            @Override
            public LiveData<Boolean> apply(ClientRemoteDataSource input) {
                return input.getLoadingState();
            }
        });

        return new UIListing<>(tokenLiveData, loadingStateLiveData);
    }
}
