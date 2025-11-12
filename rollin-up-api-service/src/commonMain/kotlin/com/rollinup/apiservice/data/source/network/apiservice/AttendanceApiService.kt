package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateEditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByStudentResponse

interface AttendanceApiService {

    suspend fun getAttendanceListByStudent(
        id: String,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceListByStudentResponse>

    suspend fun getAttendanceListByClass(
        classKey: Int,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceListByClassResponse>

    suspend fun getAttendanceById(id: String): ApiResponse<GetAttendanceByIdResponse>

    suspend fun createAttendanceData(
        body: CreateEditAttendanceBody,
    ): ApiResponse<Unit>

    suspend fun editAttendance(
        id: String,
        body: CreateEditAttendanceBody,
    ): ApiResponse<Unit>
}