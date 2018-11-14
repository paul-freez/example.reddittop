package com.testsite.reddittop.main;

import com.testsite.reddittop.App;
import com.testsite.reddittop.R;
import com.testsite.reddittop.utils.models.UIListing;
import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.api.RedditApiFactory;
import com.testsite.reddittop.data.source.client.RedditClientRepository;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;
import com.testsite.reddittop.data.source.post.RedditPostsRepository;
import com.testsite.reddittop.models.StatusAwareViewModel;
import com.testsite.reddittop.utils.CustomTabsInstance;
import com.testsite.reddittop.utils.connectivity.ErrorHandler;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.arch.core.util.Function;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

/**
 * Created by paulf
 */
public class RedditViewModel extends StatusAwareViewModel {

    private final MutableLiveData<Long> auth = new MutableLiveData<>(); // Trigger to start authentication
    private final MutableLiveData<Long> fetch = new MutableLiveData<>();  // Trigger to start fetching

    // Main repo results
    private final MediatorLiveData<UIListing<PagedList<RedditPost>>> repoResult = new MediatorLiveData<>();

    private final LiveData<UIListing<OAuthToken>> authResult = Transformations.map(auth, new Function<Long, UIListing<OAuthToken>>() {
        @Override
        public UIListing<OAuthToken> apply(Long input) {
            return clientRepository.authenticate();
        }
    });

    private final LiveData<OAuthToken> token = Transformations.switchMap(authResult, new Function<UIListing<OAuthToken>, LiveData<OAuthToken>>() {
        @Override
        public LiveData<OAuthToken> apply(UIListing<OAuthToken> input) {
            return input.getContent();
        }
    });

    private final LiveData<PagedList<RedditPost>> posts = Transformations.switchMap(repoResult, new Function<UIListing<PagedList<RedditPost>>, LiveData<PagedList<RedditPost>>>() {
        @Override
        public LiveData<PagedList<RedditPost>> apply(UIListing<PagedList<RedditPost>> input) {
            return input.getContent();
        }
    });

    private final RedditClientRepository clientRepository;
    private final RedditPostsRepository postsRepository;

    private final MutableLiveData<CustomTabsInstance.ChromTabsIntent<RedditPost>> externalIntent = new MutableLiveData<>();

    public RedditViewModel() {
        setupRepoCalls();

        this.clientRepository = RedditClientRepository.getInstance(
                RedditApiFactory.create(RedditApi.BASE_URL, new MutableLiveData<OAuthToken>()));    // We don't need actual token here
        postsRepository = RedditPostsRepository.getInstance(RedditApiFactory.create(RedditApi.OAUTH_URL, token),
                Executors.newFixedThreadPool(5));
    }

    @Override
    protected List<LiveData<Boolean>> provideLoaders() {
        return Arrays.asList(
                //Loader for auth
                Transformations.switchMap(authResult, new Function<UIListing<OAuthToken>, LiveData<Boolean>>() {
                    @Override
                    public LiveData<Boolean> apply(UIListing<OAuthToken> input) {
                        return input.getLoadStateHandler();
                    }
                }),
                // Loader for posts
                Transformations.switchMap(repoResult, new Function<UIListing<PagedList<RedditPost>>, LiveData<Boolean>>() {
                    @Override
                    public LiveData<Boolean> apply(UIListing<PagedList<RedditPost>> input) {
                        return input.getLoadStateHandler();
                    }
                }));
    }

    @Override
    protected List<LiveData<ErrorHandler>> provideErrorHandlers() {
        return Arrays.asList(
                //Loader for auth
                Transformations.switchMap(authResult, new Function<UIListing<OAuthToken>, LiveData<ErrorHandler>>() {
                    @Override
                    public LiveData<ErrorHandler> apply(UIListing<OAuthToken> input) {
                        return input.getErrorHandler();
                    }
                }),
                // Loader for posts
                Transformations.switchMap(repoResult, new Function<UIListing<PagedList<RedditPost>>, LiveData<ErrorHandler>>() {
                    @Override
                    public LiveData<ErrorHandler> apply(UIListing<PagedList<RedditPost>> input) {
                        return input.getErrorHandler();
                    }
                }));
    }

    private void setupRepoCalls() {
        Observer<UIListing<PagedList<RedditPost>>> simpleRepoObserver = new Observer<UIListing<PagedList<RedditPost>>>() {
            @Override
            public void onChanged(UIListing<PagedList<RedditPost>> pagedListUIListing) {
                repoResult.setValue(pagedListUIListing);
            }
        };

        // Result from auth
        LiveData<UIListing<PagedList<RedditPost>>> repoResultInit = Transformations.switchMap(token, new Function<OAuthToken, LiveData<UIListing<PagedList<RedditPost>>>>() {
            @Override
            public LiveData<UIListing<PagedList<RedditPost>>> apply(OAuthToken token) {
                LiveData<UIListing<PagedList<RedditPost>>> res = new MutableLiveData<>();
                ((MutableLiveData<UIListing<PagedList<RedditPost>>>) res).postValue(postsRepository.getTopPosts(5));
                return res;
            }
        });
        // Result from manual fetching
        LiveData<UIListing<PagedList<RedditPost>>> repoResultFetch = Transformations.map(fetch, new Function<Long, UIListing<PagedList<RedditPost>>>() {
            @Override
            public UIListing<PagedList<RedditPost>> apply(Long input) {
                return postsRepository.getTopPosts(5);
            }
        });

        repoResult.addSource(repoResultInit, simpleRepoObserver);
        repoResult.addSource(repoResultFetch, simpleRepoObserver);
    }

    public void start() {
        // Fetch new data only when no or expired token available - otherwise everything is set-up already
        if (token.getValue() == null || token.getValue().isExpired()) {
            authorize();
        }
    }

    public void authorize() {
        auth.setValue(System.currentTimeMillis());
    }

    public void loadPosts() {
        fetch.setValue(System.currentTimeMillis());
    }

    public LiveData<PagedList<RedditPost>> getPosts() {
        return posts;
    }

    public void openPost(RedditPost post) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(App.getContext().getResources().getColor(R.color.colorPrimary))
                .setShowTitle(true)
                .build();

        externalIntent.setValue(new CustomTabsInstance.ChromTabsIntent<>(customTabsIntent, post));
    }

    public LiveData<CustomTabsInstance.ChromTabsIntent<RedditPost>> getExternalIntent() {
        return externalIntent;
    }
}