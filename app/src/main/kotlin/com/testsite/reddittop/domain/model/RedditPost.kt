package com.testsite.reddittop.domain.model

/**
 * Created by paulf
 */
data class RedditPost(
    val title: String,
    val authorName: String,
    val subredditName: String,
    val createdTimeUtcSeconds: Long,
    val thumbnail: String,
    val scoreCount: Long,
    val commentsCount: Long,
    val permaLink: String
)