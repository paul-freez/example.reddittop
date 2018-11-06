package com.testsite.reddittop.data.source.post.remote;

import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.data.source.BaseReportingDataSource;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.post.remote.model.RedditListingResponse;
import com.testsite.reddittop.utils.connectivity.ErrorHandler;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by paulf
 */
public class PageKeyedPostsRemoteDataSource extends PageKeyedDataSource<String, RedditPost> implements BaseReportingDataSource {

    private final int MAX = 50;

    private final RedditApi api;

    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    private final MutableLiveData<ErrorHandler> errorMessenger = new MutableLiveData<>();

    private int fetchedItemsCount = 0;  // Tracking for the successfully fetched items

    public PageKeyedPostsRemoteDataSource(RedditApi api) {
        this.api = api;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, RedditPost> callback) {
        loadPosts(new LoadParamsAdapter<>(params), callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull final LoadCallback<String, RedditPost> callback) {
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull final LoadCallback<String, RedditPost> callback) {
        loadPosts(params, new LoadCallbackAdapter<>(callback));
    }

    @Override
    public LiveData<ErrorHandler> getErrorMessenger() {
        return errorMessenger;
    }

    @Override
    public LiveData<Boolean> getLoaderHandler() {
        return loadingState;
    }

    private void loadPosts(@NonNull LoadParams<String> params, final LoadInitialCallback<String, RedditPost> callback) {
        loadingState.postValue(true);

        api.getNextTopPosts(RedditApi.TimeFilter.DAY, params.key, Math.min(params.requestedLoadSize, MAX - fetchedItemsCount))
                .enqueue(new Callback<RedditListingResponse>() {
                    @Override
                    public void onResponse(Call<RedditListingResponse> call, Response<RedditListingResponse> response) {
                        loadingState.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            RedditListingResponse.ResponseData responseData = response.body().getData();
                            fetchedItemsCount += responseData.getContent().size();
                            callback.onResult(responseData.getContent(), responseData.getBeforeKey(), fetchedItemsCount >= MAX ? null : responseData.getAfterKey());
                        } else {
                            onFailure(call, new Exception("Error code: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<RedditListingResponse> call, Throwable t) {
                        loadingState.postValue(false);
                        errorMessenger.postValue(new ErrorHandler(t));
                    }
                });
    }

    private static class LoadCallbackAdapter<K, T> extends LoadInitialCallback<K, T> {

        private LoadCallback<K, T> callback;

        LoadCallbackAdapter(LoadCallback<K, T> callback) {
            this.callback = callback;
        }

        @Override
        public void onResult(@NonNull List<T> data, int position, int totalCount, @Nullable K previousPageKey, @Nullable K nextPageKey) {
        }

        @Override
        public void onResult(@NonNull List<T> data, @Nullable K previousPageKey, @Nullable K nextPageKey) {
            if (callback != null) {
                callback.onResult(data, nextPageKey);
            }
        }
    }

    private static class LoadParamsAdapter<K> extends LoadParams<String> {
        LoadParamsAdapter(LoadInitialParams<K> initialParams) {
            super("", initialParams.requestedLoadSize);
        }
    }
}
