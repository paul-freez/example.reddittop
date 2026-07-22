package com.testsite.reddittop.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.testsite.reddittop.domain.model.DummyRedditPost
import com.testsite.reddittop.ui.UIMapper.toUI
import com.testsite.reddittop.ui.model.RedditPostUI
import java.time.LocalDateTime
import java.time.ZoneOffset

class RedditPostPreview : PreviewParameterProvider<RedditPostUI> {
    override val values: Sequence<RedditPostUI>
        get() = sequenceOf(
            DummyRedditPost.post,
            DummyRedditPost.post.copy(
                title = "Shoot!",
                authorName = "SomeVeryLongUserNameThatCanBeFoundInTheDepthOfWorldWideWeb",
                subredditName = "UnconventionallyLongSubNameThatMightNotFit",
                createdTimeUtcSeconds = LocalDateTime.now().minusDays(3)
                    .toEpochSecond(ZoneOffset.UTC),
                scoreCount = -5,
                commentsCount = 0,
                permaLink = "a"
            ),
            DummyRedditPost.post.copy(
                createdTimeUtcSeconds = LocalDateTime.now().minusHours(3)
                    .toEpochSecond(ZoneOffset.UTC),
                scoreCount = 4,
                commentsCount = 100500,
                permaLink = "b"
            )
        ).map { it.toUI() }

}