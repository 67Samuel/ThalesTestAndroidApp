package com.example.thalestestandroidapp.data.network

import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.NetworkError.GeneralNetworkError
import com.example.thalestestandroidapp.domain.util.NetworkError.RemoteNetworkError
import com.example.thalestestandroidapp.domain.util.Result
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

fun Exception.toNetworkErrorResult(): Result.Error<NetworkError> {
    /**
     * Make sure that the Exceptions are in order of specificity
     */
    return when(this) {
        is SocketTimeoutException -> Result.Error(RemoteNetworkError.REQUEST_TIMEOUT)
        is JsonEncodingException -> Result.Error(RemoteNetworkError.SERIALIZATION)
        is IOException -> Result.Error(RemoteNetworkError.NO_INTERNET)
        is CancellationException,
        is ClosedReceiveChannelException -> Result.Error(GeneralNetworkError.COROUTINE_CANCELLATION)
        is HttpException -> when(this.code()) { // Retrofit Exception
            408 -> Result.Error(RemoteNetworkError.REQUEST_TIMEOUT)
            429 -> Result.Error(RemoteNetworkError.TOO_MANY_REQUESTS)
            else -> Result.Error(this.getGeneralNetworkError())
        }
        else -> Result.Error(GeneralNetworkError.UNKNOWN)
    }
}

private fun HttpException.getGeneralNetworkError(): NetworkError {
    return when (this.code()) {
        in 400 until 500 -> RemoteNetworkError.CLIENT_ERROR
        in 500 until 600 -> RemoteNetworkError.SERVER_ERROR
        else -> GeneralNetworkError.UNKNOWN
    }
}