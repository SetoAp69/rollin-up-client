package com.rollinup.apiservice.data.source.network.datasource.user

import com.rollinup.apiservice.data.source.network.apiservice.UserApiService
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.DeleteUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitVerificationOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserListResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserOptionsResponse
import com.rollinup.apiservice.data.source.network.model.response.user.ResetPasswordRequestResponse
import com.rollinup.apiservice.data.source.network.model.response.user.SubmitResetOtpResponse
import com.rollinup.apiservice.data.source.network.model.response.user.ValidateVerificationOtpResponse
import com.rollinup.apiservice.utils.JSON_TYPE
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserApiDataSource(
    private val httpClient: HttpClient,
) : UserApiService {
    val baseUrl = "/user"
    override suspend fun getUsersList(
        queryParams: GetUserQueryParams,
    ): ApiResponse<GetUserListResponse> {
        return try {
            val response = httpClient.get(baseUrl) {
                queryParams.toQueryMap().forEach { (key, value) ->
                    parameter(key, value)
                }
            }
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
            val response = httpClient.patch("$baseUrl/$id") {
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
        body: SubmitResetPasswordOTPBody,
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

    override suspend fun getUserOptions(): ApiResponse<GetUserOptionsResponse> {
        return try {
            val response = httpClient.get("$baseUrl/options") {
                contentType(JSON_TYPE)
            }
            val body = response.body<GetUserOptionsResponse>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun deleteUser(body: DeleteUserBody): ApiResponse<Unit> =
        try {
            val response = httpClient.delete(baseUrl) {
                contentType(JSON_TYPE)
                setBody(body)
            }
            ApiResponse.Success(data = Unit, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }

    override suspend fun checkEmailOrUsername(queryParams: Map<String, String?>): ApiResponse<Unit> =
        try {
            val response = httpClient.get("$baseUrl/check-email-username") {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            ApiResponse.Success(data = Unit, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }

    override suspend fun submitVerificationOtp(body: SubmitVerificationOTPBody): ApiResponse<ValidateVerificationOtpResponse> =
        try {
            val response = httpClient.post("$baseUrl/update-password-and-verification/validate") {
                setBody(body)
                contentType(ContentType.Application.Json)
            }
            val body = response.body<ValidateVerificationOtpResponse>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }

    override suspend fun updatePasswordAndVerification(body: UpdatePasswordAndVerificationBody): ApiResponse<Unit> =
        try {
            val response = httpClient.patch("$baseUrl/update-password-and-verification") {
                setBody(body)
                contentType(ContentType.Application.Json)
            }
            ApiResponse.Success(data = Unit, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }

    override suspend fun resetVerificationOtp(): ApiResponse<Unit> =
        try {
            val response = httpClient.get("$baseUrl/resend-verification-otp") { }
            ApiResponse.Success(data = Unit, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
}
