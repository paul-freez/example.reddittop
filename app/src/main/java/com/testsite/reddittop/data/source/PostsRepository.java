package com.testsite.reddittop.data.source;

import com.testsite.reddittop.data.Post;
import com.testsite.reddittop.data.source.remote.PostsRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by paulf
 */
public class PostsRepository implements PostsDataSource {

    private static PostsRepository INSTANCE;

    private PostsRemoteDataSource remoteDataSource;

    private List<Post> cachedPosts;

    private PostsRepository(PostsRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;

        cachedPosts = new ArrayList<>();
    }

    public static PostsRepository getInstance(PostsRemoteDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PostsRepository(remoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void authenticate(@NonNull OAuthCallback callback) {
        remoteDataSource.authenticate(callback);
    }

    @Override
    public void getPosts(int page, int quantity, @NonNull LoadPostsCallback callback) {
        if (!cachedPosts.isEmpty()) {
            // TODO: Update to pagination
            callback.onPostsLoaded(new ArrayList<>(cachedPosts));
        }

        remoteDataSource.getPosts(page, quantity, callback);
    }

    @Override
    public void refreshPosts() {
        cachedPosts.clear();
    }
}
