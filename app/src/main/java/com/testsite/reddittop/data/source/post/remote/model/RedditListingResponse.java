package com.testsite.reddittop.data.source.post.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.testsite.reddittop.data.RedditPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulf
 */
public class RedditListingResponse {

    @SerializedName("data")
    @Expose
    private ResponseData data;

    public static class ResponseData {
        @SerializedName("after")
        @Expose
        private String afterKey;
        @SerializedName("before")
        @Expose
        private String beforeKey;

        @SerializedName("children")
        @Expose
        private List<DataBody> content;

        public static class DataBody {
            @SerializedName("data")
            @Expose
            private RedditPost post;

            public RedditPost getPost() {
                return post;
            }
        }

        public String getAfterKey() {
            return afterKey;
        }

        public String getBeforeKey() {
            return beforeKey;
        }

        public List<RedditPost> getContent() {
            List<RedditPost> posts = new ArrayList<>();
            if (content != null) {
                for (DataBody dataBody : content) {
                    posts.add(dataBody.getPost());
                }
            }

            return posts;
        }
    }

    public ResponseData getData() {
        return data;
    }
}
