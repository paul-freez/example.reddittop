package com.testsite.reddittop.data.posts.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.testsite.reddittop.data.posts.model.TimeFilter
import com.testsite.reddittop.data.posts.source.remote.FeedRemoteDataSource
import com.testsite.reddittop.domain.DomainMapper.toDomain
import com.testsite.reddittop.domain.posts.model.RedditPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FeedRepository @Inject constructor(private val remoteSourceFeedRemoteDataSourceFactory: FeedRemoteDataSource.FeedRemoteDataSourceFactory) {

    fun getTopPostsPager(size: Int = PAGE_SIZE_DEFAULT, filter: TimeFilter = TimeFilter.DAY): Flow<PagingData<RedditPost>> {
        return Pager(
            config = PagingConfig(pageSize = size),
            pagingSourceFactory = { remoteSourceFeedRemoteDataSourceFactory.create(filter) }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    companion object {
        private const val PAGE_SIZE_DEFAULT = 5
    }
}