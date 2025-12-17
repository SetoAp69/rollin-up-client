package com.rollinup.apiservice.domain.attendance

import androidx.paging.PagingData
import com.rollinup.apiservice.data.repository.attendance.AttendanceRepository
import com.rollinup.apiservice.data.source.network.model.request.attendance.CheckInBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.EditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetExportAttendanceDataQueryParams
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class GetAttendanceByStudentPagingUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(
        id: String,
        queryParams: GetAttendanceListByStudentQueryParams,
    ): Flow<PagingData<AttendanceByStudentEntity>> =
        repository.getAttendanceByStudentPaging(id, queryParams)
}

class GetAttendanceByClassPagingUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
    ) = repository.getAttendanceByClassPaging(classKey, queryParams)
}

class GetAttendanceByStudentSummaryUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(
        studentId: String,
        date: List<Long>,
    ) = repository.getAttendanceByStudentSummary(studentId, date)
}

class GetAttendanceByClassSummaryUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(
        classKey: Int,
        date: Long?,
    ) = repository.getAttendanceByClassSummary(classKey, date)
}

class GetAttendanceByStudentListUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(
        id: String,
        queryParams: GetAttendanceListByStudentQueryParams,
    ) = repository.getAttendanceByStudentList(id, queryParams)
}

class GetAttendanceByClassListUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
    ) = repository.getAttendanceByClassList(classKey, queryParams)
}

class GetAttendanceByIdUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(id: String) = repository.getAttendanceById(id)
}

class CreateAttendanceDataUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(body: CreateAttendanceBody) = repository.createAttendanceData(body)
}

class CheckInUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(body: CheckInBody) = repository.checkIn(body)
}

class EditAttendanceDataUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(id: String, body: EditAttendanceBody) =
        repository.editAttendanceData(id, body)
}

class GetDashboardDataUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(id: String, date: LocalDate) = repository.getDashboardData(id, date)
}

class GetExportAttendanceDataUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(queryParams: GetExportAttendanceDataQueryParams) =
        repository.getAttendanceExportData(queryParams)
}
