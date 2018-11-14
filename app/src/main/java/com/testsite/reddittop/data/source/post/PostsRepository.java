package com.testsite.reddittop.data.source.post;

import com.testsite.reddittop.utils.models.UIListing;
import com.testsite.reddittop.data.RedditPost;

import androidx.paging.PagedList;

/**
 * Created by paulf
 */
interface PostsRepository {
    UIListing<PagedList<RedditPost>> getTopPosts(int size);
}
