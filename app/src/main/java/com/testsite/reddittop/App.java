package com.testsite.reddittop;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by paulf
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
