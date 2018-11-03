package com.testsite.reddittop.data.source.client;

import com.testsite.reddittop.UIListing;
import com.testsite.reddittop.data.source.BaseRepository;
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
public class RedditClientRepository extends BaseRepository<ClientRemoteDataSource, ClientRemoteDataSourceFactory> implements ClientRepository {

    private static RedditClientRepository INSTANCE;

    private RedditClientRepository(RedditApi api) {
        super(api);
    }

    @Override
    protected ClientRemoteDataSourceFactory createSourceFactory() {
        return  new ClientRemoteDataSourceFactory(getApi());
    }

    public static RedditClientRepository getInstance(RedditApi api) {
        if (INSTANCE == null) {
            INSTANCE = new RedditClientRepository(api);
        }

        return INSTANCE;
    }

    @Override
    public UIListing<OAuthToken> authenticate() {
        getSourceFactory().create().authenticate();

        LiveData<OAuthToken> tokenLiveData = Transformations.switchMap(getSourceFactory().getSourceLiveData(), new Function<ClientRemoteDataSource, LiveData<OAuthToken>>() {
            @Override
            public LiveData<OAuthToken> apply(ClientRemoteDataSource input) {
                return input.getToken();
            }
        });

        return new UIListing<>(tokenLiveData, getLoaderHandler(), getMessengerHandler());
    }
}
