package com.testsite.reddittop.main;

import com.testsite.reddittop.App;
import com.testsite.reddittop.R;
import com.testsite.reddittop.UIListing;
import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.api.RedditApiFactory;
import com.testsite.reddittop.data.source.client.RedditClientRepository;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;
import com.testsite.reddittop.data.source.post.RedditPostsRepository;
import com.testsite.reddittop.utils.CustomTabsInstance;

import java.util.concurrent.Executors;

import androidx.arch.core.util.Function;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

/**
 * Created by paulf
 */
public class RedditViewModel extends ViewModel {

    private MutableLiveData<Long> auth = new MutableLiveData<>(); // Trigger to start authentication
    private MutableLiveData<Long> fetch = new MutableLiveData<>();  // Trigger to start fetching

    // Main repo results
    private MediatorLiveData<UIListing<PagedList<RedditPost>>> repoResult = new MediatorLiveData<>();
    // Main loader
    private MediatorLiveData<Boolean> loadingState = new MediatorLiveData<>();

    private LiveData<UIListing<OAuthToken>> authResult = Transformations.map(auth, new Function<Long, UIListing<OAuthToken>>() {
        @Override
        public UIListing<OAuthToken> apply(Long input) {
            return clientRepository.authenticate();
        }
    });

    private LiveData<OAuthToken> token = Transformations.switchMap(authResult, new Function<UIListing<OAuthToken>, LiveData<OAuthToken>>() {
        @Override
        public LiveData<OAuthToken> apply(UIListing<OAuthToken> input) {
            return input.getContent();
        }
    });

    private LiveData<PagedList<RedditPost>> posts = Transformations.switchMap(repoResult, new Function<UIListing<PagedList<RedditPost>>, LiveData<PagedList<RedditPost>>>() {
        @Override
        public LiveData<PagedList<RedditPost>> apply(UIListing<PagedList<RedditPost>> input) {
            return input.getContent();
        }
    });

    private RedditClientRepository clientRepository;

    private RedditPostsRepository postsRepository;

    private MutableLiveData<CustomTabsInstance.ChromTabsIntent<RedditPost>> calloutIntent = new MutableLiveData<>();

    public RedditViewModel() {
        // Setting up loaders
        // Loader for authorization
        LiveData<Boolean> loadingStateAuth = Transformations.switchMap(authResult, new Function<UIListing<OAuthToken>, LiveData<Boolean>>() {
            @Override
            public LiveData<Boolean> apply(UIListing<OAuthToken> input) {
                return input.getLoadState();
            }
        });
        loadingState.addSource(loadingStateAuth, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                loadingState.setValue(value);
            }
        });
        // Loader for posts
        LiveData<Boolean> loadingStatePosts = Transformations.switchMap(repoResult, new Function<UIListing<PagedList<RedditPost>>, LiveData<Boolean>>() {
            @Override
            public LiveData<Boolean> apply(UIListing<PagedList<RedditPost>> input) {
                return input.getLoadState();
            }
        });
        loadingState.addSource(loadingStatePosts, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                loadingState.setValue(value);
            }
        });

        // Setting up repo calls
        // Result for the 1st init
        LiveData<UIListing<PagedList<RedditPost>>> repoResultInit = Transformations.switchMap(token, new Function<OAuthToken, LiveData<UIListing<PagedList<RedditPost>>>>() {
            @Override
            public LiveData<UIListing<PagedList<RedditPost>>> apply(OAuthToken token) {
                postsRepository = RedditPostsRepository.getInstance(RedditApiFactory.create(RedditApi.OAUTH_URL, token),
                        Executors.newFixedThreadPool(5));
                LiveData<UIListing<PagedList<RedditPost>>> res = new MutableLiveData<>();
                ((MutableLiveData<UIListing<PagedList<RedditPost>>>) res).postValue(postsRepository.getTopPosts(5));
                return res;
            }
        });
        repoResult.addSource(repoResultInit, new Observer<UIListing<PagedList<RedditPost>>>() {
            @Override
            public void onChanged(UIListing<PagedList<RedditPost>> pagedListUIListing) {
                repoResult.setValue(pagedListUIListing);
            }
        });
        // Result from manual fetching
        LiveData<UIListing<PagedList<RedditPost>>> repoResultFetch = Transformations.map(fetch, new Function<Long, UIListing<PagedList<RedditPost>>>() {
            @Override
            public UIListing<PagedList<RedditPost>> apply(Long input) {
                return postsRepository.getTopPosts(5);
            }
        });
        repoResult.addSource(repoResultFetch, new Observer<UIListing<PagedList<RedditPost>>>() {
            @Override
            public void onChanged(UIListing<PagedList<RedditPost>> pagedListUIListing) {
                repoResult.setValue(pagedListUIListing);
            }
        });

        this.clientRepository = RedditClientRepository.getInstance(
                RedditApiFactory.create(RedditApi.BASE_URL, null));
    }

    public void start() {
        auth.setValue(System.currentTimeMillis());
    }

    public void loadPosts() {
        fetch.setValue(System.currentTimeMillis());
    }

    public LiveData<PagedList<RedditPost>> getPosts() {
        return posts;
    }

    public LiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    public void openPost(RedditPost post) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(App.getContext().getResources().getColor(R.color.colorPrimary))
                .setShowTitle(true)
                .build();

        calloutIntent.setValue(new CustomTabsInstance.ChromTabsIntent<>(customTabsIntent, post));
    }

    public LiveData<CustomTabsInstance.ChromTabsIntent<RedditPost>> getCalloutIntent() {
        return calloutIntent;
    }
}
