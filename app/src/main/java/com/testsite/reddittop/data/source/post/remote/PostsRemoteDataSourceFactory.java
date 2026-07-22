package com.testsite.reddittop.data.source.post.remote;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.testsite.reddittop.data.source.ReportingDataSourceFactory;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.model.RedditPostDTO;

/**
 * Created by paulf
 * <p>
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI.
 */
@Deprecated
public class PostsRemoteDataSourceFactory extends DataSource.Factory<String, RedditPostDTO>
        implements ReportingDataSourceFactory<PageKeyedPostsRemoteDataSource> {

    private final RedditApi api;

    private final MutableLiveData<PageKeyedPostsRemoteDataSource> sourceLiveData = new MutableLiveData<>();

    public PostsRemoteDataSourceFactory(RedditApi api) {
        this.api = api;
    }

    @NonNull
    @Override
    public DataSource<String, RedditPostDTO> create() {
        PageKeyedPostsRemoteDataSource source = new PageKeyedPostsRemoteDataSource(api);
        sourceLiveData.postValue(source);
        return source;
    }

    @Override
    public LiveData<PageKeyedPostsRemoteDataSource> getSourceLiveData() {
        return sourceLiveData;
    }
}
