package com.rollinup.apiservice.domain.attendance

import androidx.paging.PagingData
import com.rollinup.apiservice.data.repository.attendance.AttendanceRepository
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateEditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import kotlinx.coroutines.flow.Flow

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
    operator fun invoke(body: CreateEditAttendanceBody) = repository.createAttendanceData(body)
}


class EditAttendanceDataUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(id: String, body: CreateEditAttendanceBody) =
        repository.editAttendanceData(id, body)
}

class GetDashboardDataUseCase(private val repository: AttendanceRepository) {
    operator fun invoke(id: String) = repository.getDashboardData(id)
}
