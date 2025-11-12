package com.rollinup.apiservice.data.repository.auth

import com.rollinup.apiservice.Utils
import com.rollinup.apiservice.data.mapper.LoginMapper
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.data.source.network.apiservice.AuthApiService
import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl(
    private val dataSource: AuthApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val mapper: LoginMapper,
) : AuthRepository {

    override fun login(body: LoginBody): Flow<Result<LoginEntity, NetworkError>> =
        flow {
            val response = dataSource.login(body)
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> emit(Result.Success(mapper.mapLoginResponse(response.data.data)))
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun loginJWT(token: String): Flow<Result<LoginEntity, NetworkError>> =
        flow {
            val response = dataSource.loginJWT(token)
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> emit(Result.Success(mapper.mapLoginResponse(response.data.data)))
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)
}

