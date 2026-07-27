package com.testsite.reddittop.data.posts.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.testsite.reddittop.data.posts.model.TimeFilter
import com.testsite.reddittop.data.posts.model.RedditPostDTO
import com.testsite.reddittop.data.posts.source.remote.FeedRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedRepository @Inject constructor(private val remoteSource: FeedRemoteDataSource) {

    fun getTopPostsPager(filter: TimeFilter, size: Int): Flow<PagingData<RedditPostDTO>> {
        // TODO: Not sure how DYNAMIC this filter would actually be
        return Pager(
            config = PagingConfig(pageSize = size),
            pagingSourceFactory = { remoteSource.apply { feedTimeSort = filter } }).flow
    }
}