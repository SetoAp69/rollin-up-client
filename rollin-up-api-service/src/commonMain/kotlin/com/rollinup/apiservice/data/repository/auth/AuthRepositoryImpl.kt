package com.rollinup.apiservice.data.repository.auth

import com.rollinup.apiservice.data.mapper.AuthMapper
import com.rollinup.apiservice.data.source.network.apiservice.AuthApiService
import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.utils.Utils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Implementation of the [AuthRepository] interface.
 *
 * This class manages authentication-related operations such as logging in (via credentials or JWT),
 * updating user credentials, and clearing local session tokens.
 *
 * @property apiDataSource The API service for making auth-related network requests.
 * @property ioDispatcher The CoroutineDispatcher for performing IO operations.
 * @property mapper The mapper to transform authentication responses into domain entities.
 */
class AuthRepositoryImpl(
    private val apiDataSource: AuthApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val mapper: AuthMapper,
) : AuthRepository {

    /**
     * Authenticates a user using credentials.
     *
     * @param body The login request body containing username and password.
     * @return A Flow emitting a [Result] containing [LoginEntity] on success or [NetworkError] on failure.
     */
    override fun login(body: LoginBody): Flow<Result<LoginEntity, NetworkError>> =
        flow {
            val response = apiDataSource.login(body)
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> {
                    emit(Result.Success(mapper.mapLoginResponse(response.data.data)))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    /**
     * Authenticates a user using an existing JWT token (auto-login).
     *
     * @param token The existing JWT token.
     * @return A Flow emitting a [Result] containing [LoginEntity] on success or [NetworkError] on failure.
     */
    override fun loginJWT(token: String): Flow<Result<LoginEntity, NetworkError>> =
        flow {
            val response = apiDataSource.loginJWT(token)
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> emit(Result.Success(mapper.mapLoginResponse(response.data.data)))
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    /**
     * Updates the user's password and/or device verification details.
     *
     * @param id The user ID.
     * @param body The request body containing the new password or device info.
     * @param token The authorization token.
     * @return A Flow emitting [Result.Success] on completion or [NetworkError] on failure.
     */
    override fun updatePasswordAndDevice(
        id: String,
        body: UpdatePasswordAndVerificationBody,
        token: String,
    ): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = apiDataSource.updatePasswordAndDevice(id, token, body)
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> emit(Result.Success(Unit))
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    /**
     * Clears the client-side authentication token, effectively logging the user out.
     */
    override suspend fun clearClientToken() {
        apiDataSource.logout()
    }

}