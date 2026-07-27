package com.testsite.reddittop.domain

import com.testsite.reddittop.data.client.model.local.OAuthTokenLocal
import com.testsite.reddittop.data.posts.model.RedditPostDTO
import com.testsite.reddittop.domain.client.model.OAuthToken
import com.testsite.reddittop.domain.posts.model.RedditPost

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

    fun OAuthTokenLocal.toDomain(): OAuthToken = OAuthToken(
        fullToken = fullToken,
        expirationTime = expirationTime,
        type = type
    )
}
