package com.testsite.reddittop.utils

import com.testsite.reddittop.data.model.RedditPostDTO

fun interface OnPostClickListener {
    fun onPostClicked(post: RedditPostDTO)
}