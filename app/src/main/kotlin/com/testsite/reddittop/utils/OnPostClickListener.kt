package com.testsite.reddittop.utils

import com.testsite.reddittop.data.RedditPost

fun interface OnPostClickListener {
    fun onPostClicked(post: RedditPost)
}