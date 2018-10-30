package com.testsite.reddittop.data.source.post.remote;

import com.testsite.reddittop.data.RedditPost;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.data.source.post.remote.model.RedditListingResponse;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by paulf
 */
public class PageKeyedPostsRemoteDataSource extends PageKeyedDataSource<String, RedditPost> {

    private RedditApi api;

    private MutableLiveData<Boolean> loadingState;

    public PageKeyedPostsRemoteDataSource(RedditApi api) {
        this.api = api;
        loadingState = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, RedditPost> callback) {
        loadingState.postValue(true);

        api.getNextTopPosts(RedditApi.TimeFilter.DAY, "", params.requestedLoadSize)
                .enqueue(new Callback<RedditListingResponse>() {
                    @Override
                    public void onResponse(Call<RedditListingResponse> call, Response<RedditListingResponse> response) {
                        loadingState.postValue(false);

                        if (response.isSuccessful()) {
                            RedditListingResponse.ResponseData responseData = response.body().getData();
                            callback.onResult(responseData.getContent(), responseData.getBeforeKey(), responseData.getAfterKey());
                        } else {
                            onFailure(call, new Exception("Error code: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<RedditListingResponse> call, Throwable t) {
                        loadingState.postValue(false);
                        // TODO: Handle
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull final LoadCallback<String, RedditPost> callback) {
        loadingState.postValue(true);

        api.getPreviousTopPosts(RedditApi.TimeFilter.DAY, params.key, params.requestedLoadSize)
                .enqueue(new Callback<RedditListingResponse>() {
                    @Override
                    public void onResponse(Call<RedditListingResponse> call, Response<RedditListingResponse> response) {
                        loadingState.postValue(false);

                        if (response.isSuccessful()) {
                            RedditListingResponse.ResponseData responseData = response.body().getData();
                            callback.onResult(responseData.getContent(), responseData.getBeforeKey());
                        } else {
                            onFailure(call, new Exception("Error code: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<RedditListingResponse> call, Throwable t) {
                        loadingState.postValue(false);
                        // TODO: Handle
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull final LoadCallback<String, RedditPost> callback) {
        loadingState.postValue(true);

        api.getNextTopPosts(RedditApi.TimeFilter.DAY, params.key, params.requestedLoadSize)
                .enqueue(new Callback<RedditListingResponse>() {
                    @Override
                    public void onResponse(Call<RedditListingResponse> call, Response<RedditListingResponse> response) {
                        loadingState.postValue(false);

                        if (response.isSuccessful()) {
                            RedditListingResponse.ResponseData responseData = response.body().getData();
                            // TODO: Limit to 50
                            callback.onResult(responseData.getContent(), responseData.getAfterKey());
                        } else {
                            onFailure(call, new Exception("Error code: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<RedditListingResponse> call, Throwable t) {
                        loadingState.postValue(false);
                        // TODO: Handle
                    }
                });
    }
}
