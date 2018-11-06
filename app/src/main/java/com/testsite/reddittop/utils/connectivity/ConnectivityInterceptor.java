package com.testsite.reddittop.utils.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.testsite.reddittop.App;
import com.testsite.reddittop.utils.exceptions.NoConnectivityException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by paulf
 */
public class ConnectivityInterceptor implements Interceptor {

    private final NetworkConnectivityManager connectivityManager;

    public ConnectivityInterceptor() {
        this.connectivityManager = new NetworkConnectivityManager();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!connectivityManager.isConnected()) {
            throw new NoConnectivityException();
        } else {
            return chain.proceed(chain.request());
        }
    }

    private static final class NetworkConnectivityManager {

        private final ConnectivityManager cm;

        NetworkConnectivityManager() {
            cm = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        boolean isConnected() {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
    }

}
