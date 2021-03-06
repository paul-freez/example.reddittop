package com.testsite.reddittop.data.source.post.remote;

import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.data.source.ReportingDataSourceFactory;
import com.testsite.reddittop.data.source.api.RedditApi;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

/**
 * Created by paulf
 * <p>
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI.
 */
public class PostsRemoteDataSourceFactory extends DataSource.Factory<String, RedditPost>
        implements ReportingDataSourceFactory<PageKeyedPostsRemoteDataSource> {

    private final RedditApi api;

    private final MutableLiveData<PageKeyedPostsRemoteDataSource> sourceLiveData = new MutableLiveData<>();

    public PostsRemoteDataSourceFactory(RedditApi api) {
        this.api = api;
    }

    @NonNull
    @Override
    public DataSource<String, RedditPost> create() {
        PageKeyedPostsRemoteDataSource source = new PageKeyedPostsRemoteDataSource(api);
        sourceLiveData.postValue(source);
        return source;
    }

    @Override
    public LiveData<PageKeyedPostsRemoteDataSource> getSourceLiveData() {
        return sourceLiveData;
    }
}
