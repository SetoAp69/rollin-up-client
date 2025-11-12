package com.rollinup.apiservice.source.network.datasource.user

import com.rollinup.apiservice.source.network.apiservice.UserApiService
import com.rollinup.apiservice.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.source.network.model.request.user.SubmitOTPBody
import com.rollinup.apiservice.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.source.network.model.response.ApiResponse
import com.rollinup.apiservice.source.network.model.response.user.GetUserByIdResponse
import com.rollinup.apiservice.source.network.model.response.user.GetUserListResponse
import com.rollinup.apiservice.source.network.model.response.user.ResetPasswordRequestResponse
import com.rollinup.apiservice.source.network.model.response.user.SubmitResetOtpResponse
import com.rollinup.apiservice.utils.JSON_TYPE
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.contentType

class UserApiDataSource(
    private val httpClient: HttpClient,
) : UserApiService {
    val baseUrl = "/user"
    override suspend fun getUsersList(
        queryParams: GetUserQueryParams,
    ): ApiResponse<GetUserListResponse> {
        return try {
            val response = httpClient.get(baseUrl)
            val body = response.body<GetUserListResponse>()

            ApiResponse.Success(data = body, statusCode = response.status)

        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun getUserById(
        userId: String,
    ): ApiResponse<GetUserByIdResponse> {
        return try {
            val response = httpClient.get("$baseUrl/$userId")
            val body = response.body<GetUserByIdResponse>()

            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun registerUser(
        body: CreateEditUserBody,
    ): ApiResponse<Unit> {
        return try {
            val response = httpClient.post(baseUrl) {
                contentType(JSON_TYPE)
                setBody(body.toHashMap())
            }
            val body = response.body<Unit>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun editUser(
        id: String,
        body: CreateEditUserBody,
    ): ApiResponse<Unit> {
        return try {
            val response = httpClient.put("$baseUrl/$id") {
                contentType(JSON_TYPE)
                setBody(body.toHashMap())
            }
            val body = response.body<Unit>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun createResetPasswordRequest(
        body: CreateResetPasswordRequestBody,
    ): ApiResponse<ResetPasswordRequestResponse> {
        return try {
            val response = httpClient.post("$baseUrl/reset-password/request") {
                contentType(JSON_TYPE)
                setBody(body)
            }
            val body = response.body<ResetPasswordRequestResponse>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun submitResetOtp(
        body: SubmitOTPBody,
    ): ApiResponse<SubmitResetOtpResponse> {
        return try {
            val response = httpClient.post("$baseUrl/reset-password/validate") {
                contentType(JSON_TYPE)
                setBody(body)
            }
            val body = response.body<SubmitResetOtpResponse>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun submitResetPassword(
        body: SubmitResetPasswordBody,
    ): ApiResponse<Unit> {
        return try {
            val response = httpClient.put("$baseUrl/reset-password") {
                contentType(JSON_TYPE)
                setBody(body)
            }
            val body = response.body<Unit>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

}
