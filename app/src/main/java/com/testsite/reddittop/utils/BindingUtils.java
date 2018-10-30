package com.testsite.reddittop.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

/**
 * Created by paulf
 */
public final class BindingUtils {

    @BindingAdapter(value = {"url", "placeholder"}, requireAll = false)
    public static void loadImage(ImageView iv, String url, Drawable placeholder) {
        if (url == null) {
            iv.setImageDrawable(placeholder);
        } else {
            GlideApp.with(iv)
                    .load(url)
                    .fitCenter()
                    .placeholder(placeholder)
                    .error(android.R.drawable.stat_notify_error)
                    .into(iv);
        }
    }
}
