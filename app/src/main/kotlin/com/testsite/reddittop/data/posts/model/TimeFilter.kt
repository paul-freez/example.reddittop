package com.testsite.reddittop.data.posts.model

import com.google.gson.annotations.SerializedName

enum class TimeFilter {
    @SerializedName("all")
    ALL,

    @SerializedName("year")
    YEAR,

    @SerializedName("month")
    MONTH,

    @SerializedName("week")
    WEEK,

    @SerializedName("day")
    DAY,

    @SerializedName("hour")
    HOUR;
}