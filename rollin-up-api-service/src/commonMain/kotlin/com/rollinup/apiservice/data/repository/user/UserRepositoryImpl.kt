package com.rollinup.apiservice.data.repository.user

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rollinup.apiservice.data.mapper.UserMapper
import com.rollinup.apiservice.data.repository.user.pagingsource.GetUserPagingSource
import com.rollinup.apiservice.data.source.network.apiservice.UserApiService
import com.rollinup.apiservice.data.source.network.model.request.user.CheckEmailOrUsernameQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.DeleteUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitVerificationOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.model.user.UserOptionEntity
import com.rollinup.apiservice.utils.Utils
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Implementation of the [UserRepository] interface.
 *
 * This repository handles the data operations for User management, including authentication flows,
 * user CRUD operations, and pagination.
 *
 * @property userApiService The network service for making user-related API calls.
 * @property mapper The mapper for transforming DTOs to Domain entities.
 * @property ioDispatcher The dispatcher for running IO operations.
 */
class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val mapper: UserMapper,
    private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {

    /**
     * Retrieves a list of users based on query parameters.
     *
     * @param queryParams Parameters to filter the user list.
     * @return A Flow emitting a Result containing a list of [UserEntity] or a [NetworkError].
     */
    override fun getUserList(queryParams: GetUserQueryParams): Flow<Result<List<UserEntity>, NetworkError>> =
        flow {
            val response = userApiService.getUsersList(
                queryParams = queryParams
            )
            when (response) {
                is ApiResponse.Success -> {
                    if (response.statusCode.value == 200) {
                        emit(Result.Success(mapper.mapGetUserList(response.data.data.data)))
                    } else {
                        emit(Result.Error(NetworkError.RESPONSE_ERROR))
                    }
                }

                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }
            }
        }.catch { emit(Utils.handleApiError(it as Exception)) }.flowOn(ioDispatcher)

    /**
     * Retrieves a paginated list of users using Paging 3.
     *
     * @param queryParams Parameters to filter the user list.
     * @return A Flow emitting [PagingData] of [UserEntity].
     */
    override fun getUserPaging(queryParams: GetUserQueryParams): Flow<PagingData<UserEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
            ),
        ) {
            GetUserPagingSource(
                dataSource = userApiService,
                query = queryParams,
                mapper = mapper
            )
        }.flow

    /**
     * Retrieves details for a specific user by ID.
     *
     * @param id The unique identifier of the user.
     * @return A Flow emitting a Result containing [UserDetailEntity] or a [NetworkError].
     */
    override fun getUserById(id: String): Flow<Result<UserDetailEntity, NetworkError>> =
        flow {
            val response = userApiService.getUserById(id)
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    if (response.statusCode == HttpStatusCode.OK) {
                        emit(Result.Success(mapper.mapGetUserById(response.data.data)))
                    } else {
                        emit(Result.Error(NetworkError.RESPONSE_ERROR))
                    }
                }
            }

        }.catch { emit(Utils.handleApiError(it as Exception)) }.flowOn(ioDispatcher)

    /**
     * Registers a new user.
     *
     * @param body The body containing user details for registration.
     * @return A Flow emitting a Result indicating success or failure.
     */
    override fun registerUser(body: CreateEditUserBody): Flow<Result<Unit, NetworkError>> =
        flow {

            val response = userApiService.registerUser(body)
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    if (response.statusCode == HttpStatusCode.Created) {
                        emit(Result.Success(Unit))
                    } else {
                        emit(Result.Error(NetworkError.RESPONSE_ERROR))
                    }
                }
            }

        }.catch { emit(Utils.handleApiError(it as Exception)) }.flowOn(ioDispatcher)

    /**
     * Updates an existing user's information.
     *
     * @param id The unique identifier of the user.
     * @param body The body containing updated user details.
     * @return A Flow emitting a Result indicating success or failure.
     */
    override fun editUser(
        id: String,
        body: CreateEditUserBody,
    ): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = userApiService.editUser(id, body)
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    if (response.statusCode == HttpStatusCode.Accepted) {
                        emit(Result.Success(Unit))
                    } else {
                        emit(Result.Error(NetworkError.RESPONSE_ERROR))
                    }
                }
            }

        }.catch { emit(Utils.handleApiError(it as Exception)) }.flowOn(ioDispatcher)

    /**
     * Initiates a password reset request.
     *
     * @param body The body containing the user's identifier (email/username).
     * @return A Flow emitting a Result containing the user's email on success.
     */
    override fun createResetPasswordRequest(body: CreateResetPasswordRequestBody): Flow<Result<String, NetworkError>> =
        flow {

            val response = userApiService.createResetPasswordRequest(body)
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    if (response.statusCode == HttpStatusCode.OK) {
                        emit(Result.Success(response.data.data.email))
                    } else {
                        emit(Result.Error(NetworkError.RESPONSE_ERROR))
                    }
                }
            }

        }.catch { emit(Utils.handleApiError(it as Exception)) }.flowOn(ioDispatcher)

    /**
     * Submits an OTP for password reset verification.
     *
     * @param body The body containing the OTP.
     * @return A Flow emitting a Result containing the reset token on success.
     */
    override fun submitResetOtp(body: SubmitResetPasswordOTPBody): Flow<Result<String, NetworkError>> =
        flow {

            val response = userApiService.submitResetOtp(body)
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    if (response.statusCode == HttpStatusCode.OK) {
                        emit(Result.Success(response.data.data.resetToken))
                    } else {
                        emit(Result.Error(NetworkError.RESPONSE_ERROR))
                    }
                }
            }

        }.catch { emit(Utils.handleApiError(it as Exception)) }.flowOn(ioDispatcher)

    /**
     * Completes the password reset process with the new password.
     *
     * @param body The body containing the new password and reset token.
     * @return A Flow emitting a Result indicating success or failure.
     */
    override fun submitResetPassword(body: SubmitResetPasswordBody): Flow<Result<Unit, NetworkError>> =
        flow {

            val response = userApiService.submitResetPassword(body)
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    if (response.statusCode == HttpStatusCode.OK) {
                        emit(Result.Success(Unit))
                    } else {
                        emit(Result.Error(NetworkError.RESPONSE_ERROR))
                    }
                }
            }

        }.catch { emit(Utils.handleApiError(it as Exception)) }.flowOn(ioDispatcher)

    /**
     * Retrieves user options (metadata).
     *
     * @return A Flow emitting a Result containing [UserOptionEntity].
     */
    override fun getOptions(): Flow<Result<UserOptionEntity, NetworkError>> =
        flow {
            val response = userApiService.getUserOptions()
            when (response) {
                is ApiResponse.Error -> {
                    println(response.e.toString())

                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    if (response.statusCode == HttpStatusCode.OK) {
                        emit(Result.Success(mapper.mapUserOptions(response.data.data)))
                    } else {
                        emit(Result.Error(NetworkError.RESPONSE_ERROR))
                    }
                }
            }
        }.catch {
            println(it.toString())
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    /**
     * Deletes a user.
     *
     * @param body The body containing user ID(s) to delete.
     * @return A Flow emitting a Result indicating success or failure.
     */
    override fun deleteUser(body: DeleteUserBody): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = userApiService.deleteUser(body)

            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    emit(Result.Success(Unit))
                }
            }
        }.catch {
            println(it.toString())
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    /**
     * Checks if an email or username is already in use.
     *
     * @param queryParams The email or username to check.
     * @return A Flow emitting a Result indicating availability (Success) or unavailable/error.
     */
    override fun checkEmailOrUsername(queryParams: CheckEmailOrUsernameQueryParams): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = userApiService.checkEmailOrUsername(queryParams.toQueryMap())

            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    emit(Result.Success(Unit))
                }
            }
        }.catch {
            println(it.toString())
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    /**
     * Submits an OTP for account verification.
     *
     * @param body The body containing the verification OTP.
     * @return A Flow emitting a Result containing the verified OTP string on success.
     */
    override fun submitVerificationOtp(body: SubmitVerificationOTPBody): Flow<Result<String, NetworkError>> =
        flow {
            val response = userApiService.submitVerificationOtp(body)

            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    emit(Result.Success(response.data.data.otp))
                }
            }
        }.catch {
            println(it.toString())
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    /**
     * Updates password and verification status simultaneously.
     *
     * @param body The body containing the new password and verification data.
     * @return A Flow emitting a Result indicating success or failure.
     */
    override fun updatePasswordAndVerification(body: UpdatePasswordAndVerificationBody): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = userApiService.updatePasswordAndVerification(body)

            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    emit(Result.Success(Unit))
                }
            }
        }.catch {
            println(it.toString())
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    /**
     * Requests a new verification OTP.
     *
     * @return A Flow emitting a Result indicating success or failure.
     */
    override fun resendVerificationOtp(): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = userApiService.resetVerificationOtp()

            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    emit(Result.Success(Unit))
                }
            }
        }.catch {
            println(it.toString())
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)
}