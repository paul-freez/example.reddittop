package com.testsite.reddittop.models;

import com.testsite.reddittop.utils.connectivity.ErrorHandler;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * View model that provides mechanisms for showing messages and loading states
 */
public abstract class StatusAwareViewModel extends ViewModel {

    // Main loader
    private final MediatorLiveData<Boolean> loadingState = new MediatorLiveData<>();
    // Main error handler
    private final MediatorLiveData<ErrorHandler> errorHandler = new MediatorLiveData<>();

    private boolean areLoadersReady = false;
    private boolean areHandlersReady = false;

    private void setupLoaders() {
        if (!areLoadersReady) {
            Observer<Boolean> simpleLoaderObserver = new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean value) {
                    loadingState.setValue(value);
                }
            };
            for (LiveData<Boolean> liveData : provideLoaders()) {
                loadingState.addSource(liveData, simpleLoaderObserver);
            }
            areLoadersReady = true;
        }
    }

    private void setupErrorMassaging() {
        if(!areHandlersReady) {
            Observer<ErrorHandler> simpleMessagingObserver = new Observer<ErrorHandler>() {
                @Override
                public void onChanged(ErrorHandler value) {
                    errorHandler.setValue(value);
                }
            };

            for (LiveData<ErrorHandler> liveData : provideErrorHandlers()) {
                errorHandler.addSource(liveData, simpleMessagingObserver);
            }
            areHandlersReady = true;
        }
    }

    protected abstract List<LiveData<Boolean>> provideLoaders();

    protected abstract List<LiveData<ErrorHandler>> provideErrorHandlers();

    public LiveData<Boolean> getLoadingState() {
        setupLoaders();

        return loadingState;
    }

    public LiveData<ErrorHandler> getErrorHandler() {
        setupErrorMassaging();

        return errorHandler;
    }
}
