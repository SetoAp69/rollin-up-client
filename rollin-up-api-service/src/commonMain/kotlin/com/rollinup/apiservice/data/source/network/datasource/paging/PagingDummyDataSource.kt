package com.rollinup.apiservice.data.source.network.datasource.paging

import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.data.source.network.apiservice.PagingDummyApi
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.paging.GetPagingDummyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PagingDummyDataSource(
    private val httpClient: HttpClient,
) : PagingDummyApi {
    override suspend fun getPagingDummy(page: Int, size: Int): ApiResponse<GetPagingDummyResponse> {
        return try {
            val response = httpClient.get(
                urlString = "https://dummyjson.com/product?select=title,price"
            ) {
                contentType(ContentType.Application.Json)
                parameter("limit", size)
                parameter("skip", (page - 1) * size)
            }


            val body = response.body<GetPagingDummyResponse>()
            L.w { "$response" }
            L.wtf { "$body" }
            ApiResponse.Success(body, response.status)
        } catch (e: Exception) {
            L.wtf { "$e" }
            ApiResponse.Error(e)
        }
    }
}