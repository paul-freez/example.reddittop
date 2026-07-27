package com.testsite.reddittop.data.posts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RedditListingResponse(@SerializedName("data") @Expose val data: ResponseData)

data class ResponseData(
    @SerializedName("after") @Expose val afterKey: String,
    @SerializedName("before") @Expose val beforeKey: String,
    @SerializedName("children") @Expose val content: List<DataBody>
)

data class DataBody(@SerializedName("data") @Expose val post: RedditPostDTO)
