package com.rollinup.apiservice.source.network.datasource.auth

import com.rollinup.apiservice.source.network.apiservice.AuthApiService
import com.rollinup.apiservice.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.source.network.model.response.ApiResponse
import com.rollinup.apiservice.source.network.model.response.auth.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiDataSource(
    private val httpClient: HttpClient,
) : AuthApiService {
    val baseUrl = "/auth"
    override suspend fun loginJWT(token: String): ApiResponse<LoginResponse> {
        return try {
            val response = httpClient.get("$baseUrl/login") {
                bearerAuth(token)
            }
            val body = response.body<LoginResponse>()

            ApiResponse.Success(body, response.status)

        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun login(body: LoginBody): ApiResponse<LoginResponse> {
        return try {
            val response = httpClient.post("$baseUrl/login") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }

            val body = response.body<LoginResponse>()

            ApiResponse.Success(body, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }
}


