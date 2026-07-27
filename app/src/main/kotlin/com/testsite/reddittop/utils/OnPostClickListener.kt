package com.testsite.reddittop.utils

import com.testsite.reddittop.data.posts.model.RedditPostDTO

fun interface OnPostClickListener {
    fun onPostClicked(post: RedditPostDTO)
}