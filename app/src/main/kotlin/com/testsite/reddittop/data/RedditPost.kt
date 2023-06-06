package com.testsite.reddittop.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.testsite.reddittop.api.RedditApi
import com.testsite.reddittop.utils.roundToK
import com.testsite.reddittop.utils.timeAgo
import java.util.*

/**
 * Created by paulf
 */
data class RedditPost(
    @SerializedName("title") @Expose val title: String,
    @SerializedName("author") @Expose private val authorName: String,
    @SerializedName("subreddit") @Expose private val subredditName: String,
    @SerializedName("created_utc") @Expose private val createdTimeUtc: Long,
    @SerializedName("thumbnail") @Expose private val thumbnail: String,
    @SerializedName("score") @Expose private val scoreCount: Long,
    @SerializedName("num_comments") @Expose private val commentsCount: Long,
    @SerializedName("permalink") @Expose private val permaLink: String
) {

    val author: String
        get() = "u/$authorName"

    val subreddit: String
        get() = "r/$subredditName"

    val creationTime: String
        get() = createdTimeUtc.timeAgo()

    val imageUrl: String?
        get() = if (thumbnail.startsWith("http")) thumbnail else null

    val score: String = scoreCount.roundToK()
    val comments: String = commentsCount.roundToK()

    val link: String
        get() = RedditApi.BASE_URL + permaLink

    override fun toString() =
        "$title\nFrom $subreddit by $author created $creationTime\nWith $scoreCount upvotes & $commentsCount comments"
}