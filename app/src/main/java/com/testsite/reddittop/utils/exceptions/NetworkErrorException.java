package com.testsite.reddittop.utils.exceptions;

import com.testsite.reddittop.App;
import com.testsite.reddittop.R;

import java.io.IOException;

/**
 * Created by paulf
 */
public final class NetworkErrorException extends RuntimeException {

    private IOException detailedException;

    public NetworkErrorException(IOException detailedException) {
        this.detailedException = detailedException;
    }

    public IOException getDetailedException() {
        return detailedException;
    }

    @Override
    public String getMessage() {
        return App.getContext().getString(R.string.error_network);
    }
}
