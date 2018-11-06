package com.testsite.reddittop.utils.exceptions;

import com.testsite.reddittop.App;
import com.testsite.reddittop.R;

/**
 * Created by paulf
 */
public final class UnauthorizedException extends RuntimeException {
    @Override
    public String getMessage() {
        return App.getContext().getString(R.string.error_unauthorized);
    }
}
