package com.testsite.reddittop.data.source;

import com.testsite.reddittop.data.Post;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by paulf
 */
public interface PostsDataSource {

    interface LoadPostsCallback{
        void onPostsLoaded(List<Post> posts);

        void onDataNotAvailable();
    }

    interface OAuthCallback{
        void onAuthApproved();

        void onFailure();
    }

    void authenticate(@NonNull OAuthCallback callback);

    void getPosts(int page, int quantity, @NonNull LoadPostsCallback callback);

    void refreshPosts();
}
