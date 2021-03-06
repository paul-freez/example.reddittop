package com.testsite.reddittop.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.testsite.reddittop.data.source.api.RedditApi;
import com.testsite.reddittop.utils.StringUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by paulf
 */
@SuppressWarnings("unused")
public class RedditPost {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("author")
    @Expose
    private String authorName;

    @SerializedName("subreddit")
    @Expose
    private String subredditName;

    @SerializedName("created_utc")
    @Expose
    private long createdTimeUtc;

    private String createdAgo;

    @SuppressWarnings("FieldCanBeLocal")
    @SerializedName("thumbnail")
    @Expose
    private @NonNull
    String thumbnail = "";

    @SerializedName("score")
    @Expose
    private int scoreCount;

    @SerializedName("num_comments")
    @Expose
    private int commentsCount;

    @SerializedName("permalink")
    @Expose
    private String permaLink;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return "u/" + authorName;
    }

    public String getSubreddit() {
        return "r/" + subredditName;
    }

    public String getCreationTime() {
        if (createdAgo == null) {
            long ago = System.currentTimeMillis() - createdTimeUtc * 1000;

            int days = (int) TimeUnit.MILLISECONDS.toDays(ago);
            int hours = (int) TimeUnit.MILLISECONDS.toHours(ago);
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(ago);
            if (days > 0) {  // Days
                createdAgo = String.format(Locale.US, "%d day(s) ago", days);
            } else if (hours > 0) {
                createdAgo = String.format(Locale.US, "%d hour(s) ago", hours);
            } else if (minutes > 0) {
                createdAgo = String.format(Locale.US, "%d minute(s) ago", minutes);
            } else {
                createdAgo = "just now";
            }
        }
        return createdAgo;
    }

    @Nullable
    public String getThumbnail() {
        return thumbnail.startsWith("http") ? thumbnail : null;
    }

    public String getScore() {
        return StringUtils.roundToK(scoreCount);
    }

    public String getCommentsCount() {
        return StringUtils.roundToK(commentsCount);
    }

    public String getLink() {
        return RedditApi.BASE_URL + permaLink;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "%s\n%s created by %s %s\n%s with %s comments",
                getTitle(), getSubreddit(), getAuthor(), getCreationTime(), getScore(), getCommentsCount());
    }
}
