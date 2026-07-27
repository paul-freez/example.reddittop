package com.testsite.reddittop.data.posts.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TimeFilter {
    @SerialName("all")
    ALL,

    @SerialName("year")
    YEAR,

    @SerialName("month")
    MONTH,

    @SerialName("week")
    WEEK,

    @SerialName("day")
    DAY,

    @SerialName("hour")
    HOUR;
}
