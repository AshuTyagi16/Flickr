package com.sasuke.flickr.util

import com.sasuke.flickr.data.exception.NoConnectivityException
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.net.UnknownHostException
import com.sasuke.flickr.data.model.Error
import javax.net.ssl.SSLHandshakeException

abstract class ApiCallback<T> : retrofit2.Callback<T> {

    final override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            response.body()?.let { body ->
                success(body)
            } ?: run {
                createError(response)
            }
        } else {
            createError(response)
        }
    }

    final override fun onFailure(call: Call<T>, t: Throwable) {
        Timber.e(t.cause)
        var error = Error()
        when (t) {
            is NoConnectivityException -> {
                error = Error(t.message!!)
                failure(error)
            }
            is SSLHandshakeException -> {
                error = Error("Internet not working properly")
                failure(error)
            }
            is UnknownHostException -> {
                error = Error("Internet not working properly")
                failure(error)
            }
            else -> failure(error)
        }
    }

    private fun createError(response: Response<T>) {
        response.errorBody()?.let { errorBody ->
            try {
                val error = Gson().fromJson<Error>(errorBody.string(), Error::class.java)
                failure(error)
            } catch (e: Exception) {
                failure(Error("${e.localizedMessage}"))
            }
        } ?: run { failure(Error()) }
    }

    abstract fun success(response: T)

    abstract fun failure(error: Error)
}
