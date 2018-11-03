package com.testsite.reddittop;

import androidx.lifecycle.LiveData;

/**
 * Created by paulf
 *
 * Data class that is necessary for a UI to show a listing and interact with the rest of the system
 */
public class UIListing<T> extends LiveData<T> {

    private LiveData<T> pagedList;

    private LiveData<Boolean> loadState;

    private LiveData<String> errorMessage;

    public UIListing(LiveData<T> pagedList, LiveData<Boolean> loadState, LiveData<String> errorMessage) {
        this.pagedList = pagedList;
        this.loadState = loadState;
        this.errorMessage = errorMessage;
    }

    public LiveData<T> getContent() {
        return pagedList;
    }

    public LiveData<Boolean> getLoadState() {
        return loadState;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
