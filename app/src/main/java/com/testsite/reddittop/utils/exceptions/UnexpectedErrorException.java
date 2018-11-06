package com.testsite.reddittop.utils.exceptions;

import com.testsite.reddittop.App;
import com.testsite.reddittop.R;

/**
 * Created by paulf
 */
public final class UnexpectedErrorException extends RuntimeException {

    private String detailedResponse;

    public UnexpectedErrorException(String detailedResponse) {
        this.detailedResponse = detailedResponse;
    }

    public String getDetailedResponse() {
        return detailedResponse;
    }

    @Override
    public String getMessage() {
        return App.getContext().getString(R.string.error_unexpected);
    }
}
