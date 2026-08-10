package com.testsite.reddittop.data.posts.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testsite.reddittop.data.posts.RedditApi
import com.testsite.reddittop.data.posts.model.RedditPostDTO
import com.testsite.reddittop.data.posts.model.TimeFilter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber

class FeedRemoteDataSource @AssistedInject constructor(
    private val api: RedditApi,
    @Assisted private val feedTimeSort: TimeFilter
) : PagingSource<String, RedditPostDTO>() {

    @AssistedFactory
    interface FeedRemoteDataSourceFactory {
        fun create(feedTimeSort: TimeFilter): FeedRemoteDataSource
    }

    override fun getRefreshKey(state: PagingState<String, RedditPostDTO>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditPostDTO> {
        val index = params.key
        try {
            val response = api.getTopPosts(feedTimeSort, index, params.loadSize)
            with(response.data) {
                return LoadResult.Page(
                    data = content.map { it.post },
                    prevKey = beforeKey,
                    nextKey = afterKey
                )
            }

        } catch (e: Exception) {
            Timber.e(e)
            return LoadResult.Error(e)
        }
    }
}