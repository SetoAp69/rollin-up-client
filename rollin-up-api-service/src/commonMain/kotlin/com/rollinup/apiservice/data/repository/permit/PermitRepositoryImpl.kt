package com.rollinup.apiservice.data.repository.permit

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rollinup.apiservice.utils.Utils
import com.rollinup.apiservice.data.mapper.PermitMapper
import com.rollinup.apiservice.data.repository.permit.pagingsource.GetPermitByClassPagingSource
import com.rollinup.apiservice.data.repository.permit.pagingsource.GetPermitByStudentPagingSource
import com.rollinup.apiservice.data.source.network.apiservice.PermitApiService
import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PermitRepositoryImpl(
    private val dataSource: PermitApiService,
    private val mapper: PermitMapper,
    private val ioDispatcher: CoroutineDispatcher,
) : PermitRepository {
    override fun getPermitByStudentPaging(
        id: String,
        queryParams: GetPermitListQueryParams,
    ): Flow<PagingData<PermitByStudentEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
            )
        ) {
            GetPermitByStudentPagingSource(
                datasource = dataSource,
                mapper = mapper,
                id = id,
                queryParams = queryParams
            )
        }.flow


    override fun getPermitByClassPaging(
        classKey: Int,
        queryParams: GetPermitListQueryParams,
    ): Flow<PagingData<PermitByClassEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            )
        ) {
            GetPermitByClassPagingSource(
                datasource = dataSource,
                mapper = mapper,
                classKey = classKey,
                queryParams = queryParams
            )
        }.flow

    override fun getPermitListByClass(
        classKey: Int,
        query: GetPermitListQueryParams,
    ): Flow<Result<List<PermitByClassEntity>, NetworkError>> = flow {
        val response = dataSource.getPermitListByClass(classKey, query.toQueryMap())
        when (response) {
            is ApiResponse.Success -> {
                val data = mapper.mapPermitListByClass(response.data.data.data)
                emit(Result.Success(data))
            }

            is ApiResponse.Error -> {
                emit(Utils.handleApiError(response.e))
            }
        }
    }.catch {
        emit(Utils.handleApiError(it as Exception))
    }.flowOn(ioDispatcher)

    override fun getPermitListByStudent(
        id: String,
        query: GetPermitListQueryParams,
    ): Flow<Result<List<PermitByStudentEntity>, NetworkError>> =
        flow {
            val response = dataSource.getPermitListByStudent(id, query.toQueryMap())
            when (response) {
                is ApiResponse.Success -> {
                    val data = mapper.mapPermitListByStudentResponse(response.data.data.data)
                    emit(Result.Success(data))
                }

                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun getPermitById(id: String): Flow<Result<PermitDetailEntity, NetworkError>> =
        flow {
            val response = dataSource.getPermitById(id)
            when (response) {
                is ApiResponse.Success -> {
                    val data = mapper.mapPermitById(response.data.data)
                    emit(Result.Success(data))
                }

                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun doApproval(body: PermitApprovalBody): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = dataSource.doApproval(body)
            when (response) {
                is ApiResponse.Success -> {
                    emit(Result.Success(Unit))
                }

                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun createPermit(body: CreateEditPermitBody): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = dataSource.createPermit(body)
            when (response) {
                is ApiResponse.Success -> {
                    emit(Result.Success(Unit))
                }

                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun editPermit(
        id: String,
        body: CreateEditPermitBody,
    ): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = dataSource.editPermit(id, body)
            when (response) {
                is ApiResponse.Success -> {
                    emit(Result.Success(Unit))
                }

                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun cancelPermit(id: String): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = dataSource.cancelPermitRequest(id)
            when (response) {
                is ApiResponse.Success -> {
                    emit(Result.Success(Unit))
                }

                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)
}

