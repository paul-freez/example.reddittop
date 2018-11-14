package com.testsite.reddittop.data.source.post;

import com.testsite.reddittop.utils.models.UIListing;
import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.data.source.BaseRepository;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.post.remote.PageKeyedPostsRemoteDataSource;
import com.testsite.reddittop.data.source.post.remote.PostsRemoteDataSourceFactory;

import java.util.concurrent.Executor;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * Created by paulf
 */
public class RedditPostsRepository extends BaseRepository<PageKeyedPostsRemoteDataSource, PostsRemoteDataSourceFactory>
        implements PostsRepository {

    private static RedditPostsRepository INSTANCE;

    private final Executor networkExecutor;

    public static RedditPostsRepository getInstance(RedditApi api, Executor networkExecutor) {
        if (INSTANCE == null) {
            INSTANCE = new RedditPostsRepository(api, networkExecutor);
        }

        return INSTANCE;
    }

    private RedditPostsRepository(RedditApi api, Executor networkExecutor) {
        super(api);

        this.networkExecutor = networkExecutor;
    }

    @Override
    public UIListing<PagedList<RedditPost>> getTopPosts(int size) {
        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setInitialLoadSizeHint(size + 1)
                        .setPageSize(size).build();
        LiveData<PagedList<RedditPost>> pagedListLiveData = new LivePagedListBuilder<>(getSourceFactory(), pagedListConfig)
                .setFetchExecutor(networkExecutor)
                .build();

        return new UIListing<>(pagedListLiveData, getLoaderHandler(), getMessengerHandler());
    }

    @Override
    protected PostsRemoteDataSourceFactory createSourceFactory() {
        return new PostsRemoteDataSourceFactory(getApi());
    }
}
