package com.testsite.reddittop.data.source;

import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.utils.connectivity.ErrorHandler;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

/**
 * Created by paulf
 */
public abstract class BaseRepository<D extends BaseReportingDataSource, T extends ReportingDataSourceFactory<D>> {

    private final RedditApi api;

    private T sourceFactory;

    protected BaseRepository(RedditApi api) {
        this.api = api;
    }

    protected RedditApi getApi() {
        return api;
    }

    protected T getSourceFactory() {
        if (sourceFactory == null) {
            sourceFactory = createSourceFactory();
        }

        return sourceFactory;
    }

    protected abstract T createSourceFactory();

    protected LiveData<Boolean> getLoaderHandler() {
        return Transformations.switchMap(sourceFactory.getSourceLiveData(), new Function<D, LiveData<Boolean>>() {
            @Override
            public LiveData<Boolean> apply(D input) {
                return input.getLoaderHandler();
            }
        });
    }

    protected LiveData<ErrorHandler> getMessengerHandler() {
        return Transformations.switchMap(sourceFactory.getSourceLiveData(), new Function<D, LiveData<ErrorHandler>>() {
            @Override
            public LiveData<ErrorHandler> apply(D input) {
                return input.getErrorMessenger();
            }
        });
    }

}
