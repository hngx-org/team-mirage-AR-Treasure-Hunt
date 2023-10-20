package com.shegs.artreasurehunt.data.network.request_and_response_models

sealed class NetworkResult<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Loading<T> : NetworkResult<T>()

    class Success<T>(data: T?) : NetworkResult<T>(data = data)

    class Error<T>(throwable: Throwable?) : NetworkResult<T>(throwable = throwable)

}