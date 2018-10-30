package com.testsite.reddittop.data.source.post;

import com.testsite.reddittop.UIListing;
import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.post.remote.PageKeyedPostsRemoteDataSource;
import com.testsite.reddittop.data.source.post.remote.PostsRemoteDataSourceFactory;

import java.util.concurrent.Executor;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * Created by paulf
 */
public class RedditPostsRepository implements PostsRepository {

    private static RedditPostsRepository INSTANCE;

    private RedditApi api;

    private Executor networkExecutor;

    public static RedditPostsRepository getInstance(RedditApi api, Executor networkExecutor) {
        if (INSTANCE == null) {
            INSTANCE = new RedditPostsRepository(api, networkExecutor);
        }

        return INSTANCE;
    }

    private RedditPostsRepository(RedditApi api, Executor networkExecutor) {
        this.api = api;
        this.networkExecutor = networkExecutor;
    }

    @Override
    public UIListing<PagedList<RedditPost>> getTopPosts(int size) {
        PostsRemoteDataSourceFactory sourceFactory = new PostsRemoteDataSourceFactory(api);

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
//                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(size + 1)
                        .setPageSize(size).build();
        LiveData<PagedList<RedditPost>> pagedListLiveData = new LivePagedListBuilder<>(sourceFactory, pagedListConfig)
                .setFetchExecutor(networkExecutor)
                .build();

        LiveData<Boolean> loadingState = Transformations.switchMap(sourceFactory.getSourceLiveData(), new Function<PageKeyedPostsRemoteDataSource, LiveData<Boolean>>() {
            @Override
            public LiveData<Boolean> apply(PageKeyedPostsRemoteDataSource input) {
                return input.getLoadingState();
            }
        });

        return new UIListing<>(pagedListLiveData, loadingState);
    }
}
