package com.testsite.reddittop.ui.model

/**
 * Created by paulf
 */
data class RedditPostUI(
    val title: String,
    val author: String,
    val subreddit: String,
    val creationTimeDisplay: String,
    val thumbnail: String?,
    val scoreCountDisplay: String,
    val commentsCount: Long,
    val commentsCountDisplay: String,
    val link: String
)