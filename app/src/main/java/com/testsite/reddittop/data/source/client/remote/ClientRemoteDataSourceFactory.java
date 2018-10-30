package com.testsite.reddittop.data.source.client.remote;

import com.testsite.reddittop.data.source.api.RedditApi;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by paulf
 */
public class ClientRemoteDataSourceFactory {

    private RedditApi api;

    private MutableLiveData<ClientRemoteDataSource> sourceLiveData = new MutableLiveData<>();

    public ClientRemoteDataSourceFactory(RedditApi api) {
        this.api = api;
    }

    public LiveData<ClientRemoteDataSource> getSourceLiveData() {
        return sourceLiveData;
    }

    @NonNull
    public ClientRemoteDataSource create() {
        ClientRemoteDataSource source = new ClientRemoteDataSource(api);
        sourceLiveData.postValue(source);
        return source;
    }
}
