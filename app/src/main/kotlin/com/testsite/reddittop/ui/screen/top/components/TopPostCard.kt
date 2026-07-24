@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.testsite.reddittop.ui.screen.top.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.testsite.reddittop.R
import com.testsite.reddittop.ui.model.RedditPostUI
import com.testsite.reddittop.utils.RedditPostPreview

@Preview("Post preview")
@Composable
private fun PreviewTopPostCard(
    @PreviewParameter(RedditPostPreview::class) post: RedditPostUI
) {
    TopPostCard(post = post, onPostClicked = {})
}

@Composable
fun TopPostCard(post: RedditPostUI, onPostClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .padding(horizontal = 4.dp)
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onPostClicked,
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                AsyncImage(
                    model = post.thumbnail,
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.img_noimage),
                    error = painterResource(id = android.R.drawable.stat_notify_error),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(size = 10.dp)),
                )

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = post.title,
                        color = colorResource(id = R.color.textcolor_title),
                        fontSize = 20.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = post.subreddit,
                        color = colorResource(id = R.color.textcolor_primary),
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "by ${post.author}",
                            color = colorResource(id = R.color.textcolor_secondary),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, false)
                        )
                        Text(
                            text = post.creationTimeDisplay,
                            color = colorResource(id = R.color.textcolor_secondary),
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.scoreCountDisplay,
                    color = colorResource(id = R.color.colorPrimary),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = pluralStringResource(
                        R.plurals.post_comments,
                        post.commentsCount.toInt(),
                        post.commentsCountDisplay
                    ),
                    color = colorResource(id = R.color.textcolor_secondary),
                    fontSize = 14.sp,
                )
            }
        }
    }
}