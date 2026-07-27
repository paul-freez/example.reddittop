package com.testsite.reddittop.data.posts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.testsite.reddittop.data.posts.RedditApi
import com.testsite.reddittop.utils.roundToK
import com.testsite.reddittop.utils.timeAgo
import java.util.concurrent.TimeUnit

/**
 * Created by paulf
 */
data class RedditPostDTO(
    @SerializedName("title") @Expose val title: String,
    @SerializedName("author") @Expose val authorName: String,
    @SerializedName("subreddit") @Expose val subredditName: String,
    @SerializedName("created_utc") @Expose val createdTimeUtcSeconds: Long,
    @SerializedName("thumbnail") @Expose val thumbnail: String,
    @SerializedName("score") @Expose val scoreCount: Long,
    @SerializedName("num_comments") @Expose val commentsCount: Long,
    @SerializedName("permalink") @Expose val permaLink: String
) {

    val author: String
        get() = "u/$authorName"

    val subreddit: String
        get() = "r/$subredditName"

    val creationTime: String
        get() = createdTimeUtcSeconds.let(TimeUnit.SECONDS::toMillis).timeAgo()

    val imageUrl: String?
        get() = if (thumbnail.startsWith("http")) thumbnail else null

    val score: String = scoreCount.roundToK()
    val commentsDisplay: String = commentsCount.roundToK()

    val link: String
        get() = RedditApi.BASE_URL + permaLink

    override fun toString() =
        "$title\nFrom $subreddit by $author created $creationTime\nWith $scoreCount upvotes & $commentsCount comments"
}