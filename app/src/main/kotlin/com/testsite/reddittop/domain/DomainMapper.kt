package com.testsite.reddittop.domain

import com.testsite.reddittop.data.model.RedditPostDTO
import com.testsite.reddittop.domain.model.RedditPost

object DomainMapper {
    fun RedditPostDTO.toDomain(): RedditPost = RedditPost(
        title = title,
        authorName = authorName,
        subredditName = subredditName,
        createdTimeUtcSeconds = createdTimeUtcSeconds,
        thumbnail = thumbnail,
        scoreCount = scoreCount,
        commentsCount = commentsCount,
        permaLink = permaLink,
    )
}