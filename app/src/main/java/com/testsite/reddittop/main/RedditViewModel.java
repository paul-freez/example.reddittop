package com.testsite.reddittop.main;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import com.testsite.reddittop.App;
import com.testsite.reddittop.R;
import com.testsite.reddittop.utils.ChromeTabsIntent;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.api.RedditApiFactory;
import com.testsite.reddittop.data.source.client.RedditClientRepository;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;
import com.testsite.reddittop.data.source.post.RedditPostsRepository;
import com.testsite.reddittop.data.posts.model.RedditPostDTO;
import com.testsite.reddittop.models.StatusAwareViewModel;
import com.testsite.reddittop.utils.connectivity.ErrorHandler;
import com.testsite.reddittop.utils.models.UIListing;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import kotlin.jvm.functions.Function1;

/**
 * Created by paulf
 */
public class RedditViewModel extends StatusAwareViewModel {

    private final MutableLiveData<Long> auth = new MutableLiveData<>(); // Trigger to start authentication
    private final MutableLiveData<Long> fetch = new MutableLiveData<>();  // Trigger to start fetching

    // Main repo results
    private final MediatorLiveData<UIListing<PagedList<RedditPostDTO>>> repoResult = new MediatorLiveData<>();

    private final LiveData<UIListing<OAuthToken>> authResult = Transformations.map(auth, new Function1<Long, UIListing<OAuthToken>>() {
        @Override
        public UIListing<OAuthToken> invoke(Long input) {
            return clientRepository.authenticate();
        }
    });

    private final LiveData<OAuthToken> token = Transformations.switchMap(authResult, new Function1<UIListing<OAuthToken>, LiveData<OAuthToken>>() {
        @Override
        public LiveData<OAuthToken> invoke(UIListing<OAuthToken> input) {
            return input.getContent();
        }
    });

    private final LiveData<PagedList<RedditPostDTO>> posts = Transformations.switchMap(repoResult, new Function1<UIListing<PagedList<RedditPostDTO>>, LiveData<PagedList<RedditPostDTO>>>() {
        @Override
        public LiveData<PagedList<RedditPostDTO>> invoke(UIListing<PagedList<RedditPostDTO>> input) {
            return input.getContent();
        }
    });

    private final RedditClientRepository clientRepository;
    private final RedditPostsRepository postsRepository;

    private final MutableLiveData<ChromeTabsIntent<RedditPostDTO>> externalIntent = new MutableLiveData<>();

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
                Transformations.switchMap(authResult, new Function1<UIListing<OAuthToken>, LiveData<Boolean>>() {
                    @Override
                    public LiveData<Boolean> invoke(UIListing<OAuthToken> input) {
                        return input.getLoadStateHandler();
                    }
                }),
                // Loader for posts
                Transformations.switchMap(repoResult, new Function1<UIListing<PagedList<RedditPostDTO>>, LiveData<Boolean>>() {
                    @Override
                    public LiveData<Boolean> invoke(UIListing<PagedList<RedditPostDTO>> input) {
                        return input.getLoadStateHandler();
                    }
                }));
    }

    @Override
    protected List<LiveData<ErrorHandler>> provideErrorHandlers() {
        return Arrays.asList(
                //Loader for auth
                Transformations.switchMap(authResult, new Function1<UIListing<OAuthToken>, LiveData<ErrorHandler>>() {
                    @Override
                    public LiveData<ErrorHandler> invoke(UIListing<OAuthToken> input) {
                        return input.getErrorHandler();
                    }
                }),
                // Loader for posts
                Transformations.switchMap(repoResult, new Function1<UIListing<PagedList<RedditPostDTO>>, LiveData<ErrorHandler>>() {
                    @Override
                    public LiveData<ErrorHandler> invoke(UIListing<PagedList<RedditPostDTO>> input) {
                        return input.getErrorHandler();
                    }
                }));
    }

    private void setupRepoCalls() {
        Observer<UIListing<PagedList<RedditPostDTO>>> simpleRepoObserver = new Observer<UIListing<PagedList<RedditPostDTO>>>() {
            @Override
            public void onChanged(UIListing<PagedList<RedditPostDTO>> pagedListUIListing) {
                repoResult.setValue(pagedListUIListing);
            }
        };

        // Result from auth
        LiveData<UIListing<PagedList<RedditPostDTO>>> repoResultInit = Transformations.switchMap(token, new Function1<OAuthToken, LiveData<UIListing<PagedList<RedditPostDTO>>>>() {
            @Override
            public LiveData<UIListing<PagedList<RedditPostDTO>>> invoke(OAuthToken token) {
                LiveData<UIListing<PagedList<RedditPostDTO>>> res = new MutableLiveData<>();
                ((MutableLiveData<UIListing<PagedList<RedditPostDTO>>>) res).postValue(postsRepository.getTopPosts(5));
                return res;
            }
        });
        // Result from manual fetching
        LiveData<UIListing<PagedList<RedditPostDTO>>> repoResultFetch = Transformations.map(fetch, new Function1<Long, UIListing<PagedList<RedditPostDTO>>>() {
            @Override
            public UIListing<PagedList<RedditPostDTO>> invoke(Long input) {
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

    public LiveData<PagedList<RedditPostDTO>> getPosts() {
        return posts;
    }

    public void openPost(RedditPostDTO post) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(App.Companion.getAppContext().getResources().getColor(R.color.colorPrimary))
                .setShowTitle(true)
                .build();

        externalIntent.setValue(new ChromeTabsIntent<>(customTabsIntent, post));
    }

    public LiveData<ChromeTabsIntent<RedditPostDTO>> getExternalIntent() {
        return externalIntent;
    }
}