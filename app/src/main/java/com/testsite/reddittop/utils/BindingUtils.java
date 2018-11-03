package com.testsite.reddittop.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    @BindingAdapter("toggleRefresh")
    public static void toggleRefresh(SwipeRefreshLayout swl, boolean show) {
        // Since we set refreshing only manually, this call will just hide it
        if (swl.isRefreshing() && !show) {
            swl.setRefreshing(false);
        }
    }

    @BindingAdapter("toggleView")
    public static void toggleProgressBar(ContentLoadingProgressBar pb, boolean show) {
        if (show) {
            pb.show();
        } else {
            pb.hide();
        }
    }
}
