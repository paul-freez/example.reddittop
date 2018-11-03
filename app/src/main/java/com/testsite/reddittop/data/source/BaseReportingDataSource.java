package com.testsite.reddittop.data.source;

import androidx.lifecycle.LiveData;

/**
 * A basic class to provide reports on loading state and errors
 */
public interface BaseReportingDataSource {
    LiveData<String> getErrorMessenger();
    LiveData<Boolean> getLoaderHandler();
}
