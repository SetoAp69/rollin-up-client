package com.rollinup.apiservice.data.repository.attendance

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rollinup.apiservice.data.mapper.AttendanceMapper
import com.rollinup.apiservice.data.repository.attendance.pagingsource.GetAttendanceListByClassPaging
import com.rollinup.apiservice.data.repository.attendance.pagingsource.GetAttendanceListByStudentPagingSource
import com.rollinup.apiservice.data.source.network.apiservice.AttendanceApiService
import com.rollinup.apiservice.data.source.network.model.request.attendance.CheckInBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.EditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetExportAttendanceDataQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.attendance.DashboardDataEntity
import com.rollinup.apiservice.model.attendance.ExportAttendanceDataEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.utils.Utils
import com.rollinup.apiservice.utils.Utils.toJsonString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.LocalDate

class AttendanceRepositoryImpl(
    private val datasource: AttendanceApiService,
    private val mapper: AttendanceMapper,
    private val ioDispatcher: CoroutineDispatcher,
) : AttendanceRepository {
    override fun getAttendanceByClassPaging(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
    ): Flow<PagingData<AttendanceByClassEntity>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
        ),
        initialKey = 1,
    ) {
        GetAttendanceListByClassPaging(
            classKey = classKey,
            mapper = mapper,
            dataSource = datasource,
            queryParams = queryParams
        )
    }.flow

    override fun getAttendanceByStudentPaging(
        id: String,
        queryParams: GetAttendanceListByStudentQueryParams,
    ): Flow<PagingData<AttendanceByStudentEntity>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10
        ),
        initialKey = 1
    ) {
        GetAttendanceListByStudentPagingSource(
            id = id,
            dataSource = datasource,
            queryParams = queryParams,
            mapper = mapper
        )
    }.flow

    override fun getAttendanceByStudentList(
        id: String,
        queryParams: GetAttendanceListByStudentQueryParams,
    ): Flow<Result<List<AttendanceByStudentEntity>, NetworkError>> =
        flow {
            val response = datasource.getAttendanceListByStudent(id, queryParams.toQueryMap())
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> {
                    val data = mapper.mapAttendanceListByStudent(response.data.data.data)
                    emit(Result.Success(data))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun getAttendanceByClassList(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
    ): Flow<Result<List<AttendanceByClassEntity>, NetworkError>> =
        flow {
            val response = datasource.getAttendanceListByClass(classKey, queryParams.toQueryMap())
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> {
                    val data = mapper.mapAttendanceListByClass(response.data.data.data)
                    emit(Result.Success(data))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun getAttendanceByStudentSummary(
        studentId: String,
        dateRange: List<Long>,
    ): Flow<Result<AttendanceSummaryEntity, NetworkError>> = flow {
        val dateParams = mapOf("date" to dateRange.toJsonString())
        val response = datasource.getAttendanceByStudentSummary(studentId, dateParams)

        when (response) {
            is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
            is ApiResponse.Success -> {
                val data = mapper
                    .mapAttendanceByClassSummary(response.data.data)
                emit(Result.Success(data))
            }
        }
    }.catch {
        emit(Utils.handleApiError(it as Exception))
    }.flowOn(ioDispatcher)

    override fun getAttendanceByClassSummary(
        classKey: Int,
        date: Long?,
    ): Flow<Result<AttendanceSummaryEntity, NetworkError>> = flow {
        val dateParams = mapOf("date" to date?.toString())
        val response = datasource.getAttendanceByClassSummary(classKey, dateParams)

        when (response) {
            is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
            is ApiResponse.Success -> {
                val data = mapper
                    .mapAttendanceByClassSummary(response.data.data)
                emit(Result.Success(data))
            }
        }
    }.catch {
        emit(Utils.handleApiError(it as Exception))
    }.flowOn(ioDispatcher)


    override fun getAttendanceById(id: String): Flow<Result<AttendanceDetailEntity, NetworkError>> =
        flow {
            val response = datasource.getAttendanceById(id)
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> {
                    val data = mapper.mapAttendanceById(response.data.data)
                    emit(Result.Success(data))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun createAttendanceData(body: CreateAttendanceBody): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = datasource.createAttendanceData(body)
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> emit(Result.Success(Unit))
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun checkIn(body: CheckInBody): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = datasource.checkIn(body)
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> emit(Result.Success(Unit))
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun getDashboardData(
        id: String,
        date: LocalDate,
    ): Flow<Result<DashboardDataEntity, NetworkError>> =
        flow {
            val response = datasource.getDashboardData(id, mapOf("date" to date.toString()))
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> {
                    val data = mapper.mapDashboardData(response.data.data)
                    emit(Result.Success(data))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun editAttendanceData(
        id: String,
        body: EditAttendanceBody,
    ): Flow<Result<Unit, NetworkError>> =
        flow {
            val response = datasource.editAttendance(id, body)
            when (response) {
                is ApiResponse.Error -> emit(Utils.handleApiError(response.e))
                is ApiResponse.Success -> emit(Result.Success(Unit))
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)

    override fun getAttendanceExportData(queryParams: GetExportAttendanceDataQueryParams): Flow<Result<ExportAttendanceDataEntity, NetworkError>> =
        flow {
            val response = datasource.getExportData(queryParams.toQueryMap())
            when (response) {
                is ApiResponse.Error -> {
                    emit(Utils.handleApiError(response.e))
                }

                is ApiResponse.Success -> {
                    emit(Result.Success(mapper.mapExportAttendanceData(response.data.data)))
                }
            }
        }.catch {
            emit(Utils.handleApiError(it as Exception))
        }.flowOn(ioDispatcher)
}