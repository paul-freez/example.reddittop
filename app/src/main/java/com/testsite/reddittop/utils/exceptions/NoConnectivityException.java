package com.testsite.reddittop.utils.exceptions;

import com.testsite.reddittop.App;
import com.testsite.reddittop.R;

import java.io.IOException;

/**
 * Created by paulf
 */
public class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return App.getContext().getString(R.string.error_noconnection);
    }
}
