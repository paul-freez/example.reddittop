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

    public UIListing(LiveData<T> pagedList, LiveData<Boolean> loadState) {
        this.pagedList = pagedList;
        this.loadState = loadState;
    }

    public LiveData<T> getContent() {
        return pagedList;
    }

    public LiveData<Boolean> getLoadState() {
        return loadState;
    }
}
