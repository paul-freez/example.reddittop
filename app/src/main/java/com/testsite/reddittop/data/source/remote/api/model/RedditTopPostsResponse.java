package com.testsite.reddittop.data.source.remote.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.testsite.reddittop.data.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulf
 */
public class RedditTopPostsResponse {

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
            private Post post;

            public Post getPost() {
                return post;
            }
        }

        public String getAfterKey() {
            return afterKey;
        }

        public String getBeforeKey() {
            return beforeKey;
        }

        public List<Post> getContent() {
            List<Post> posts = new ArrayList<>();
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
