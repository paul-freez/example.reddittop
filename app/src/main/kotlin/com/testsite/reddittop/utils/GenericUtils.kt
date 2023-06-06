package com.testsite.reddittop.utils

import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Long.roundToK() : String {
    return if (this > 1000) {
        DecimalFormat("#.#").format((this / 1000f).toDouble()) + "k"
    } else {
        this.toString()
    }
}

fun Long.timeAgo() : String {
    val ago: Long = System.currentTimeMillis() - this * 1000

    val days = TimeUnit.MILLISECONDS.toDays(ago).toInt()
    val hours = TimeUnit.MILLISECONDS.toHours(ago).toInt()
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ago).toInt()

    return when{
        days > 0 -> String.format(Locale.US, "%d day(s) ago", days)
        hours > 0 -> String.format(Locale.US, "%d hour(s) ago", hours)
        minutes > 0 -> String.format(Locale.US, "%d minute(s) ago", minutes)
        else -> "just now"
    }
}