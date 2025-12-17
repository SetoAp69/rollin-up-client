package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.request.attendance.CheckInBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.EditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByStudentResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceSummaryResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetDashboardDataResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetExportAttendanceDataResponse

interface AttendanceApiService {

    suspend fun getAttendanceListByStudent(
        id: String,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceListByStudentResponse>

    suspend fun getAttendanceByStudentSummary(
        studentId: String,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceSummaryResponse>

    suspend fun getAttendanceListByClass(
        classKey: Int,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceListByClassResponse>

    suspend fun getAttendanceByClassSummary(
        classKey: Int,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceSummaryResponse>

    suspend fun getAttendanceById(id: String): ApiResponse<GetAttendanceByIdResponse>

    suspend fun createAttendanceData(
        body: CreateAttendanceBody,
    ): ApiResponse<Unit>

    suspend fun checkIn(
        body: CheckInBody,
    ): ApiResponse<Unit>

    suspend fun getDashboardData(
        id: String,
        query:Map<String,String?>
    ): ApiResponse<GetDashboardDataResponse>

    suspend fun editAttendance(
        id: String,
        body: EditAttendanceBody,
    ): ApiResponse<Unit>

    suspend fun getExportData(
        query: Map<String, String?>,
    ): ApiResponse<GetExportAttendanceDataResponse>
}