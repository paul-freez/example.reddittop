package com.testsite.reddittop.ui

import com.testsite.reddittop.core.Constants
import com.testsite.reddittop.domain.posts.model.RedditPost
import com.testsite.reddittop.ui.model.RedditPostUI
import com.testsite.reddittop.utils.fixRedditImagePreview
import com.testsite.reddittop.utils.roundToK
import com.testsite.reddittop.utils.timeAgo
import java.util.concurrent.TimeUnit

object UiMapper {
    fun RedditPost.toUI(): RedditPostUI = RedditPostUI(
        title = title,
        author = "u/$authorName",
        subreddit = "r/$subredditName",
        creationTimeDisplay = createdTimeUtcSeconds.let(TimeUnit.SECONDS::toMillis).timeAgo(),
        thumbnail = thumbnail.fixRedditImagePreview(),
        scoreCountDisplay = scoreCount.roundToK(),
        commentsCount = commentsCount,
        commentsCountDisplay = commentsCount.roundToK(),
        link = Constants.REDDIT_EXT + permaLink
    )
}