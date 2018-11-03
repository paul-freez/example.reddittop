package com.testsite.reddittop.data.source;

import androidx.lifecycle.LiveData;

/**
 * Created by paulf
 */
public interface ReportingDataSourceFactory<T extends BaseReportingDataSource> {
    LiveData<T> getSourceLiveData();
}
