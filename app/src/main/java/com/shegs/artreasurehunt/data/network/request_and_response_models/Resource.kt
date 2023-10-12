package com.shegs.artreasurehunt.data.network.request_and_response_models

sealed class Resource<out T> {

    data class Success<out T>(val data: T?) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()

    object Loading : Resource<Nothing>()

}
