package com.testsite.reddittop.data.posts

import com.testsite.reddittop.data.posts.model.RedditListingResponse
import com.testsite.reddittop.data.posts.model.TimeFilter
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {

    @GET("/top.json")
    suspend fun getTopPosts(
        @Query("t") timeFilter: TimeFilter,
        @Query("after") lastElement: String?,
        @Query("limit") size: Int
    ): RedditListingResponse

    companion object {
        const val BASE_URL = "https://oauth.reddit.com"
    }
}