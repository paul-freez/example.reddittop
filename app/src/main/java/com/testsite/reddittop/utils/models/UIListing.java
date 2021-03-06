package com.testsite.reddittop.utils.models;

import com.testsite.reddittop.utils.connectivity.ErrorHandler;

import androidx.lifecycle.LiveData;

/**
 * Created by paulf
 *
 * Data class that is necessary for a UI to show a listing and interact with the rest of the system
 */
public class UIListing<T> extends LiveData<T> {

    private final LiveData<T> pagedList;

    private final LiveData<Boolean> loadState;

    private final LiveData<ErrorHandler> errorMessage;

    public UIListing(LiveData<T> pagedList, LiveData<Boolean> loadState, LiveData<ErrorHandler> errorMessage) {
        this.pagedList = pagedList;
        this.loadState = loadState;
        this.errorMessage = errorMessage;
    }

    public LiveData<T> getContent() {
        return pagedList;
    }

    public LiveData<Boolean> getLoadStateHandler() {
        return loadState;
    }

    public LiveData<ErrorHandler> getErrorHandler() {
        return errorMessage;
    }
}
