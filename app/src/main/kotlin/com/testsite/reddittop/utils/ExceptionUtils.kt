package com.testsite.reddittop.utils

import android.content.res.Resources
import com.testsite.reddittop.R
import com.testsite.reddittop.components.connectivity.NetworkErrorException
import com.testsite.reddittop.components.connectivity.NoConnectivityException
import com.testsite.reddittop.components.connectivity.ServerErrorException
import com.testsite.reddittop.components.connectivity.UnauthorizedException
import com.testsite.reddittop.components.connectivity.UnexpectedErrorException

fun Throwable.toUserMessage(resources: Resources): String {
    with(resources) {
        return when (this@toUserMessage) {
            is NoConnectivityException -> getString(R.string.error_noconnection)
            is UnauthorizedException -> getString(R.string.error_unauthorized)
            is UnexpectedErrorException -> getString(R.string.error_unexpected, this@toUserMessage.message)
            is NetworkErrorException -> getString(R.string.error_network, this@toUserMessage.message)
            is ServerErrorException -> getString(R.string.error_servererror)

            else -> message ?: getString(R.string.error)
        }
    }
}