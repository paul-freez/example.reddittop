package com.testsite.reddittop.components.connectivity

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor

class ErrorHandlingCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
        return object : CallAdapter<Any, Call<Any>> {
            override fun responseType(): Type = responseType
            override fun adapt(call: Call<Any>): Call<Any> = ErrorHandlingCall(call, retrofit.callbackExecutor())
        }
    }

    private class ErrorHandlingCall<T>(
        private val delegate: Call<T>,
        private val executor: Executor?
    ) : Call<T> by delegate {

        override fun enqueue(callback: Callback<T>) {
            delegate.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    executor.execute {
                        if (response.isSuccessful) {
                            callback.onResponse(this@ErrorHandlingCall, response)
                        } else {
                            callback.onFailure(this@ErrorHandlingCall, response.toNetworkException())
                        }
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    executor.execute {
                        callback.onFailure(this@ErrorHandlingCall, t.toNetworkException())
                    }
                }
            })
        }

        override fun clone(): Call<T> = ErrorHandlingCall(delegate.clone(), executor)

        private fun Executor?.execute(action: () -> Unit) {
            this?.execute(action) ?: action()
        }
    }
}
