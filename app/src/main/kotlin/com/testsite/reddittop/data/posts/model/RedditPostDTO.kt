package com.testsite.reddittop.data.posts.model

import com.testsite.reddittop.data.posts.RedditApi
import com.testsite.reddittop.utils.roundToK
import com.testsite.reddittop.utils.timeAgo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.concurrent.TimeUnit

/**
 * Created by paulf
 */
@Serializable
data class RedditPostDTO(
    @SerialName("title") val title: String,
    @SerialName("author") val authorName: String,
    @SerialName("subreddit") val subredditName: String,
    // For some reason, API returns time with .0 at the end???
    @SerialName("created_utc") private val _createdTimeUtcSeconds: Double,
    @SerialName("thumbnail") val thumbnail: String,
    @SerialName("score") val scoreCount: Long,
    @SerialName("num_comments") val commentsCount: Long,
    @SerialName("permalink") val permaLink: String
) {

    val createdTimeUtcSeconds : Long = _createdTimeUtcSeconds.toLong()

    val author: String
        get() = "u/$authorName"

    val subreddit: String
        get() = "r/$subredditName"

    val creationTime: String
        get() = createdTimeUtcSeconds.let(TimeUnit.SECONDS::toMillis).timeAgo()

    val imageUrl: String?
        get() = if (thumbnail.startsWith("http")) thumbnail else null

    @Transient
    val score: String = scoreCount.roundToK()
    @Transient
    val commentsDisplay: String = commentsCount.roundToK()

    val link: String
        get() = RedditApi.BASE_URL + permaLink

    override fun toString() =
        "$title\nFrom $subreddit by $author created $creationTime\nWith $scoreCount upvotes & $commentsCount comments"
}