package com.testsite.reddittop.ui

import com.testsite.reddittop.api.RedditApi
import com.testsite.reddittop.domain.model.RedditPost
import com.testsite.reddittop.ui.model.RedditPostUI
import com.testsite.reddittop.utils.roundToK
import com.testsite.reddittop.utils.timeAgo
import java.util.concurrent.TimeUnit

object UIMapper {
    fun RedditPost.toUI(): RedditPostUI = RedditPostUI(
        title = title,
        author = "u/$authorName",
        subreddit = "r/$subredditName",
        creationTimeDisplay = createdTimeUtcSeconds.let(TimeUnit.SECONDS::toMillis).timeAgo(),
        thumbnail = if (thumbnail.startsWith("http")) thumbnail else null,
        scoreCountDisplay = scoreCount.roundToK(),
        commentsCount = commentsCount,
        commentsCountDisplay = commentsCount.roundToK(),
        link = RedditApi.BASE_URL + permaLink
    )
}