package com.rollinup.apiservice.data.repository.attendance

import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateEditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import kotlinx.coroutines.flow.Flow

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

    fun getAttendanceById(
        id: String,
    ): Flow<Result<AttendanceDetailEntity, NetworkError>>

    fun createAttendanceData(body: CreateEditAttendanceBody): Flow<Result<Unit, NetworkError>>

    fun editAttendanceData(
        id: String,
        body: CreateEditAttendanceBody,
    ): Flow<Result<Unit, NetworkError>>


}