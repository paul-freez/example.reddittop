package com.testsite.reddittop.data.posts.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RedditListingResponse(@SerialName("data") val data: ResponseData)

@Serializable
data class ResponseData(
    @SerialName("after") val afterKey: String,
    @SerialName("before") val beforeKey: String?,
    @SerialName("children") val content: List<DataBody>
)

@Serializable
data class DataBody(@SerialName("data") val post: RedditPostDTO)
