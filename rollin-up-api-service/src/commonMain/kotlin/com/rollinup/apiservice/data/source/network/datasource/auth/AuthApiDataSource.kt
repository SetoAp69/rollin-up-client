package com.rollinup.apiservice.data.source.network.datasource.auth

import com.rollinup.apiservice.data.source.network.apiservice.AuthApiService
import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.auth.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Implementation of [AuthApiService] using Ktor [HttpClient].
 * Handles network operations related to authentication, such as login, logout,
 * and credential updates.
 *
 * @property httpClient The Ktor client instance used to make network requests.
 */
class AuthApiDataSource(
    private val httpClient: HttpClient,
) : AuthApiService {
    val baseUrl = "/auth"

    /**
     * Authenticates the user using an existing JWT token.
     * Typically used for auto-login or session validation.
     *
     * @param token The Bearer token string to validate.
     * @return [ApiResponse] containing [LoginResponse] if successful, or an error.
     */
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

    /**
     * Performs a standard login operation using user credentials.
     *
     * @param body The [LoginBody] containing username/email and password.
     * @return [ApiResponse] containing [LoginResponse] with tokens and user info.
     */
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

    /**
     * Updates the user's password and device verification status.
     * This request manually sets the Authorization header, overriding the default client configuration.
     *
     * @param id The unique identifier of the user.
     * @param token The specific token required for this verification action.
     * @param body The [UpdatePasswordAndVerificationBody] containing new credentials.
     * @return [ApiResponse] containing [Unit] on success.
     */
    override suspend fun updatePasswordAndDevice(
        id: String,
        token: String,
        body: UpdatePasswordAndVerificationBody,
    ): ApiResponse<Unit> = try {
        val response = httpClient.patch("/user/$id/update-password-and-device") {
            headers.remove("Authorization")
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        ApiResponse.Success(Unit, response.status)
    } catch (e: Exception) {
        ApiResponse.Error(e)
    }

    /**
     * Clears the locally stored authentication token from the Ktor client.
     * Effectively logs the user out of the client session.
     */
    override suspend fun logout() {
        httpClient.authProvider<BearerAuthProvider>()?.clearToken()
    }

}