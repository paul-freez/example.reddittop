package com.testsite.reddittop.api

import com.testsite.reddittop.data.RedditListingResponse
import com.testsite.reddittop.data.TimeFilter
import retrofit2.http.*

interface RedditApi {

    @GET("/top.json")
    suspend fun getTopPosts(
        @Query("t") timeFilter: TimeFilter, @Query("after") lastElement: String,
        @Query("limit") size: Int
    ): RedditListingResponse

    companion object {
        const val BASE_URL = "https://www.reddit.com"
    }
}