package com.rollinup.apiservice

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode

data class HttpResponseData(
    val content: String = "",
    val status: HttpStatusCode = HttpStatusCode.OK,
    val contentType: ContentType = ContentType.Application.Json,
) {
    val sContentType
        get() = contentType.toString()
}