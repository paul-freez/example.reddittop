package com.testsite.reddittop.data.source.client;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.testsite.reddittop.data.source.BaseRepository;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.client.remote.ClientRemoteDataSource;
import com.testsite.reddittop.data.source.client.remote.ClientRemoteDataSourceFactory;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;
import com.testsite.reddittop.utils.models.UIListing;

import kotlin.jvm.functions.Function1;

/**
 * Created by paulf
 */
@Deprecated
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

        LiveData<OAuthToken> tokenLiveData = Transformations.switchMap(getSourceFactory().getSourceLiveData(), new Function1<ClientRemoteDataSource, LiveData<OAuthToken>>() {
            @Override
            public LiveData<OAuthToken> invoke(ClientRemoteDataSource input) {
                return input.getToken();
            }
        });

        return new UIListing<>(tokenLiveData, getLoaderHandler(), getMessengerHandler());
    }
}
