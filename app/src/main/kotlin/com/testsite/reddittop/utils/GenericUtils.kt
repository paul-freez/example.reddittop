package com.testsite.reddittop.utils

import androidx.core.text.HtmlCompat
import java.text.DecimalFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Long.roundToK(): String {
    return if (this > 1000) {
        DecimalFormat("#.#").format((this / 1000f).toDouble()) + "k"
    } else {
        this.toString()
    }
}

fun Long.timeAgo(): String {
    val ago: Long = System.currentTimeMillis() - this

    val days = TimeUnit.MILLISECONDS.toDays(ago).toInt()
    val hours = TimeUnit.MILLISECONDS.toHours(ago).toInt()
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ago).toInt()

    return when {
        days > 0 -> String.format(Locale.US, "%d day(s) ago", days)
        hours > 0 -> String.format(Locale.US, "%d hour(s) ago", hours)
        minutes > 0 -> String.format(Locale.US, "%d minute(s) ago", minutes)
        else -> "just now"
    }
}

fun String.fixRedditImagePreview(): String? {
    return if (startsWith("http")) {
        HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    } else null
}