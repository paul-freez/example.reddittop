package com.testsite.reddittop.data.posts.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testsite.reddittop.data.posts.RedditApi
import com.testsite.reddittop.data.posts.model.RedditPostDTO
import com.testsite.reddittop.data.posts.model.TimeFilter
import timber.log.Timber
import javax.inject.Inject

class FeedRemoteDataSource @Inject constructor(
    private val api: RedditApi,
    var feedTimeSort: TimeFilter = TimeFilter.DAY
) :
    PagingSource<String, RedditPostDTO>() {
    override fun getRefreshKey(state: PagingState<String, RedditPostDTO>): String? {
        // TODO: Look into responses and prev/next keys. Maybe we can update this thing to add proper +- positions
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey
                ?: state.closestPageToPosition(anchor)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditPostDTO> {
        val index = params.key ?: ""
        try {
            val response = api.getTopPosts(feedTimeSort, index, params.loadSize)
            with(response.data) {
                return LoadResult.Page(content.map { it.post }, beforeKey, afterKey)
            }

        } catch (e: Exception) {
            Timber.e(e)
            return LoadResult.Error(e)
        }
    }
}