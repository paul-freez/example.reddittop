package com.testsite.reddittop;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by paulf
 */
public class App extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
