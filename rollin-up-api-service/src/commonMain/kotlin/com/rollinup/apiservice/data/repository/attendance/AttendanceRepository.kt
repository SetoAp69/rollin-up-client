package com.rollinup.apiservice.data.repository.attendance

import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.attendance.CheckInBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.EditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetExportAttendanceDataQueryParams
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.ExportAttendanceDataEntity
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.attendance.DashboardDataEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface AttendanceRepository {
    fun getAttendanceByClassPaging(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
    ): Flow<PagingData<AttendanceByClassEntity>>

    fun getAttendanceByStudentPaging(
        id: String,
        queryParams: GetAttendanceListByStudentQueryParams,
    ): Flow<PagingData<AttendanceByStudentEntity>>

    fun getAttendanceByStudentList(
        id: String,
        queryParams: GetAttendanceListByStudentQueryParams,
    ): Flow<Result<List<AttendanceByStudentEntity>, NetworkError>>

    fun getAttendanceByClassList(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
    ): Flow<Result<List<AttendanceByClassEntity>, NetworkError>>

    fun getAttendanceByStudentSummary(
        studentId: String,
        dateRange: List<Long>,
    ): Flow<Result<AttendanceSummaryEntity, NetworkError>>

    fun getAttendanceByClassSummary(
        classKey: Int,
        date: Long?,
    ): Flow<Result<AttendanceSummaryEntity, NetworkError>>

    fun getAttendanceById(
        id: String,
    ): Flow<Result<AttendanceDetailEntity, NetworkError>>

    fun createAttendanceData(body: CreateAttendanceBody): Flow<Result<Unit, NetworkError>>

    fun checkIn(body: CheckInBody): Flow<Result<Unit, NetworkError>>

    fun editAttendanceData(
        id: String,
        body: EditAttendanceBody,
    ): Flow<Result<Unit, NetworkError>>

    fun getDashboardData(
        id: String,
        date: LocalDate,
    ): Flow<Result<DashboardDataEntity, NetworkError>>

    fun getAttendanceExportData(
        queryParams: GetExportAttendanceDataQueryParams,
    ): Flow<Result<ExportAttendanceDataEntity, NetworkError>>


}