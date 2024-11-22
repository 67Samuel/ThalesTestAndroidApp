package com.example.thalestestandroidapp.domain.util

sealed interface NetworkError: Error {
    enum class RemoteNetworkError: NetworkError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER_ERROR,
        CLIENT_ERROR,
        SERIALIZATION
    }

    enum class GeneralNetworkError: NetworkError {
        UNKNOWN,
        COROUTINE_CANCELLATION
    }
}