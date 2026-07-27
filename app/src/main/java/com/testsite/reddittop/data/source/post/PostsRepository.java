package com.testsite.reddittop.data.source.post;

import androidx.paging.PagedList;

import com.testsite.reddittop.data.posts.model.RedditPostDTO;
import com.testsite.reddittop.utils.models.UIListing;

/**
 * Created by paulf
 */
@Deprecated
interface PostsRepository {
    UIListing<PagedList<RedditPostDTO>> getTopPosts(int size);
}
