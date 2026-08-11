package com.testsite.reddittop.core

import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Helper/wrapper class for CoroutineExceptionHandler for message and behavior customization
 */
class ExceptionHandler @Inject constructor() {
    operator fun invoke(handle: (Throwable) -> Unit): CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable -> handle(updateException(throwable)) }

    fun updateException(throwable: Throwable): Throwable {
        val errMsg = when (throwable) {
            is HttpException -> throwable.detailedMessage()

            else -> null
        }

        return Throwable(message = errMsg ?: throwable.message, cause = throwable)
    }

    private fun HttpException.detailedMessage(): String? = when (code()) {
        401, 403 -> "Not authorized"
        else -> null
    }
}