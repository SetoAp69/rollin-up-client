package com.rollinup.apiservice.data.repository.permit

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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
import com.rollinup.apiservice.utils.Utils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Implementation of the [PermitRepository] interface.
 *
 * This repository handles data operations related to permits, including fetching
 * paginated lists, retrieving specific permit details, and performing actions
 * like creating, editing, approving, and canceling permits.
 *
 * @property dataSource The network API service for permit operations.
 * @property mapper The mapper to transform network DTOs into domain entities.
 * @property ioDispatcher The CoroutineDispatcher for performing IO operations.
 */
class PermitRepositoryImpl(
    private val dataSource: PermitApiService,
    private val mapper: PermitMapper,
    private val ioDispatcher: CoroutineDispatcher,
) : PermitRepository {
    /**
     * Retrieves a paginated list of permits for a specific student.
     *
     * @param id The ID of the student.
     * @param queryParams Parameters for filtering and sorting the list.
     * @return A Flow emitting [PagingData] of [PermitByStudentEntity].
     */
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


    /**
     * Retrieves a paginated list of permits for a specific class.
     *
     * @param classKey The ID (key) of the class.
     * @param queryParams Parameters for filtering and sorting the list.
     * @return A Flow emitting [PagingData] of [PermitByClassEntity].
     */
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

    /**
     * Retrieves a complete list of permits for a class based on query parameters.
     *
     * @param classKey The ID (key) of the class.
     * @param query Parameters for filtering the list.
     * @return A Flow emitting a [Result] containing a list of [PermitByClassEntity] or a [NetworkError].
     */
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

    /**
     * Retrieves a complete list of permits for a student based on query parameters.
     *
     * @param id The ID of the student.
     * @param query Parameters for filtering the list.
     * @return A Flow emitting a [Result] containing a list of [PermitByStudentEntity] or a [NetworkError].
     */
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

    /**
     * Fetches detailed information about a specific permit.
     *
     * @param id The ID of the permit.
     * @return A Flow emitting a [Result] containing [PermitDetailEntity] or a [NetworkError].
     */
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

    /**
     * Approves or rejects a permit request.
     *
     * @param body The approval body containing status and notes.
     * @return A Flow emitting a [Result] indicating success or failure.
     */
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

    /**
     * Creates a new permit request.
     *
     * @param body The body containing permit details.
     * @return A Flow emitting a [Result] indicating success or failure.
     */
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

    /**
     * Edits an existing permit request.
     *
     * @param id The ID of the permit to edit.
     * @param body The updated permit details.
     * @return A Flow emitting a [Result] indicating success or failure.
     */
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

    /**
     * Cancels a pending permit request.
     *
     * @param id The ID of the permit to cancel.
     * @return A Flow emitting a [Result] indicating success or failure.
     */
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