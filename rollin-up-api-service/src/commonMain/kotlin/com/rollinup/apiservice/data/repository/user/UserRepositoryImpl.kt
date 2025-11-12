package com.rollinup.apiservice.data.repository.user

import com.rollinup.apiservice.Utils
import com.rollinup.apiservice.data.mapper.UserMapper
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.data.source.network.apiservice.UserApiService
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val mapper: UserMapper,
    private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {

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

    override fun registerUser(body: CreateEditUserBody): Flow<Result<Unit, NetworkError>> =
        flow {

            val response = userApiService.registerUser(body)
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
                    if (response.statusCode == HttpStatusCode.OK) {
                        emit(Result.Success(Unit))
                    } else {
                        emit(Result.Error(NetworkError.RESPONSE_ERROR))
                    }
                }
            }

        }.catch { emit(Utils.handleApiError(it as Exception)) }.flowOn(ioDispatcher)

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

    override fun submitResetOtp(body: SubmitOTPBody): Flow<Result<String, NetworkError>> =
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
}