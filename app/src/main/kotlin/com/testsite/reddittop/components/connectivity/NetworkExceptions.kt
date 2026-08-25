package com.testsite.reddittop.components.connectivity

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface NetworkException

class NoConnectivityException : IOException(), NetworkException

class NetworkErrorException(val detailedException: IOException) : RuntimeException(), NetworkException {
    override val message: String
        get() = detailedException.message.orEmpty()

    override val cause: Throwable?
        get() = detailedException.cause
}

data class UnauthorizedException(val code: Int) : RuntimeException(), NetworkException

data class UnexpectedErrorException(
    val code: Int,
    private val detailedResponse: String?
) : RuntimeException(), NetworkException {
    override val message: String
        get() = detailedResponse.orEmpty()
}

data class ServerErrorException(val code: Int) : RuntimeException(), NetworkException

fun Response<*>.toNetworkException(): Throwable = when (val code = code()) {
    401, 403 -> UnauthorizedException(code)
    in 500..599 -> ServerErrorException(code)
    else -> UnexpectedErrorException(code = code, detailedResponse = message())
}

fun Throwable.toNetworkException(): Throwable = when (this) {
    is NetworkException -> this
    is IOException -> NetworkErrorException(this)
    is HttpException -> when (val code = code()) {
        401, 403 -> UnauthorizedException(code)
        in 500..599 -> ServerErrorException(code)
        else -> UnexpectedErrorException(code = code, detailedResponse = message())
    }

    else -> this
}
