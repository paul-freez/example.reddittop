package com.testsite.reddittop.utils

import android.R
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide

@BindingAdapter(value = ["url", "placeholder"], requireAll = false)
fun loadImage(iv: ImageView, url: String?, placeholder: Drawable?) {
    if (url == null) {
        iv.setImageDrawable(placeholder)
    } else {
        Glide.with(iv)
            .load(url)
            .fitCenter()
            .placeholder(placeholder)
            .error(R.drawable.stat_notify_error)
            .into(iv)
    }
}

@BindingAdapter("toggleRefresh")
fun toggleRefresh(swl: SwipeRefreshLayout, show: Boolean) {
    // Since we set refreshing only manually, this call will just hide it
    if (swl.isRefreshing && !show) {
        swl.isRefreshing = false
    }
}

@BindingAdapter("toggleView")
fun toggleProgressBar(pb: ContentLoadingProgressBar, show: Boolean) {
    if (show) {
        pb.show()
    } else {
        pb.hide()
    }
}

@BindingAdapter("useDefaultDivider")
fun useDivider(rv: RecyclerView, useStandartDivider: Boolean) {
    val layoutManager = rv.layoutManager
    if (useStandartDivider && layoutManager != null) {
        if (layoutManager is LinearLayoutManager) {
            rv.addItemDecoration(
                DividerItemDecoration(
                    rv.context,
                    layoutManager.orientation
                )
            )
        }
    }
}