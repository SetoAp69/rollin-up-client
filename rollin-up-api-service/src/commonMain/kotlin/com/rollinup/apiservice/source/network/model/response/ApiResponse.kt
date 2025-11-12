package com.rollinup.apiservice.source.network.model.response

import io.ktor.http.HttpStatusCode

sealed class ApiResponse<out T>() {
    data class Success<out T>(val data: T, val statusCode: HttpStatusCode) :
        ApiResponse<T>()

    data class Error(val e: Exception) :
        ApiResponse<Nothing>()
}