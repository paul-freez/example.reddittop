package com.testsite.reddittop.data.source.client.remote;

import com.testsite.reddittop.data.source.ReportingDataSourceFactory;
import com.testsite.reddittop.data.source.api.RedditApi;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by paulf
 * <p>
 *  * A simple data source factory which also provides a way to observe the last created data source.
 *  * This allows us to channel its network request status etc back to the UI.
 */
public class ClientRemoteDataSourceFactory implements ReportingDataSourceFactory<ClientRemoteDataSource> {

    private final RedditApi api;

    private final MutableLiveData<ClientRemoteDataSource> sourceLiveData = new MutableLiveData<>();

    public ClientRemoteDataSourceFactory(RedditApi api) {
        this.api = api;
    }

    @Override
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
