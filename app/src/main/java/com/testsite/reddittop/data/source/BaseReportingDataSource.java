package com.testsite.reddittop.data.source;

import com.testsite.reddittop.utils.connectivity.ErrorHandler;

import androidx.lifecycle.LiveData;

/**
 * A basic class to provide reports on loading state and errors
 */
public interface BaseReportingDataSource {
    LiveData<ErrorHandler> getErrorMessenger();
    LiveData<Boolean> getLoaderHandler();
}
