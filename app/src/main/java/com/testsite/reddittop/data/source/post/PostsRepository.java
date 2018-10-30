package com.testsite.reddittop.data.source.post;

import com.testsite.reddittop.UIListing;
import com.testsite.reddittop.data.RedditPost;

import androidx.paging.PagedList;

/**
 * Created by paulf
 */
public interface PostsRepository {
    UIListing<PagedList<RedditPost>> getTopPosts(int size);
}
