package com.testsite.reddittop.domain.model

import java.time.LocalDateTime
import java.time.ZoneOffset

object DummyRedditPost {
    val post = RedditPost(
        title = "This will be the title of the very-very cool post in the top of the day list!",
        authorName = "UserName",
        subredditName = "TopSub",
        createdTimeUtcSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
        thumbnail = "",
        scoreCount = 100201,
        commentsCount = 937,
        permaLink = "google.com"
    )
}