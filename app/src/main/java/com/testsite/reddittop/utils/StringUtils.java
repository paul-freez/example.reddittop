package com.testsite.reddittop.utils;

import java.text.DecimalFormat;

/**
 * Created by paulf
 */
public final class StringUtils {
    private StringUtils() {
    }

    public static String roundToK(int number) {
        if (number > 1000) {
            return new DecimalFormat("#.#").format(number / 1000f) + "k";
        } else {
            return String.valueOf(number);
        }
    }
}
