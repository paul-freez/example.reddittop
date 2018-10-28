package com.testsite.reddittop.main;

import com.testsite.reddittop.data.Post;
import com.testsite.reddittop.data.source.PostsDataSource;
import com.testsite.reddittop.data.source.PostsRepository;
import com.testsite.reddittop.data.source.remote.PostsRemoteDataSource;

import java.util.List;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

/**
 * Created by paulf
 */
public class TopPostsViewModel extends ViewModel {

    private MutableLiveData<List<Post>> postList;

    private ObservableBoolean dataLoading = new ObservableBoolean(false);

    // TODO: Re-make access to repository using LiveData
    private PostsRepository postsRepository;

    public TopPostsViewModel() {
        this.postsRepository = PostsRepository.getInstance(new PostsRemoteDataSource());
    }

    public void start() {
        postsRepository.authenticate(new PostsDataSource.OAuthCallback() {
            @Override
            public void onAuthApproved() {
                loadPosts(true);
            }

            @Override
            public void onFailure() {
                // TODO: Handle error
            }
        });
    }

    public LiveData<List<Post>> getPostList() {
        if (postList == null) {
            postList = new MutableLiveData<>();
        }
        return postList;
    }

    public void loadPosts(boolean forceUpdate) {
        if (forceUpdate) {
            postsRepository.refreshPosts();
        }
        dataLoading.set(true);

        // TODO: Update to dynamic pagination
        postsRepository.getPosts(0, 2, new PostsDataSource.LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                dataLoading.set(false);
                for (Post post : posts) {
                    Timber.d(post.toString());
                }

                postList.setValue(posts);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO: Handle errors
            }
        });
    }
}
