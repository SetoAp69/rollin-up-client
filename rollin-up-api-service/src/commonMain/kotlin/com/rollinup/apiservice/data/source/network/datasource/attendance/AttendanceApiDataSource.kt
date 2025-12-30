package com.rollinup.apiservice.data.source.network.datasource.attendance

import com.rollinup.apiservice.data.source.network.apiservice.AttendanceApiService
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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Implementation of [AttendanceApiService] using Ktor [HttpClient].
 * Manages all network requests related to attendance tracking, reporting, and dashboard data.
 *
 * @property httpClient The Ktor client instance used to make network requests.
 */
class AttendanceApiDataSource(
    private val httpClient: HttpClient,
) : AttendanceApiService {
    val baseUrl = "/attendance"

    /**
     * Retrieves a list of attendance records for a specific student.
     *
     * @param id The unique identifier of the student.
     * @param query A map of query parameters (e.g., date range, filters).
     * @return [ApiResponse] containing [GetAttendanceListByStudentResponse].
     */
    override suspend fun getAttendanceListByStudent(
        id: String,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceListByStudentResponse> {
        return try {
            val response = httpClient.get("$baseUrl/by-student/$id") {
                query.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            val body = response.body<GetAttendanceListByStudentResponse>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse.Error(e)
        }
    }

    /**
     * Retrieves an attendance summary (stats) for a specific student.
     *
     * @param studentId The unique identifier of the student.
     * @param query A map of query parameters for filtering the summary.
     * @return [ApiResponse] containing [GetAttendanceSummaryResponse].
     */
    override suspend fun getAttendanceByStudentSummary(
        studentId: String,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceSummaryResponse> {
        return try {
            val response = httpClient.get("$baseUrl/by-student/$studentId/summary") {
                query.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            val body = response.body<GetAttendanceSummaryResponse>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    /**
     * Retrieves a list of attendance records for a specific class.
     *
     * @param classKey The unique integer key identifying the class.
     * @param query A map of query parameters for filtering the list.
     * @return [ApiResponse] containing [GetAttendanceListByClassResponse].
     */
    override suspend fun getAttendanceListByClass(
        classKey: Int,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceListByClassResponse> {
        return try {
            val response = httpClient.get("$baseUrl/by-class/$classKey") {
                query.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            val body = response.body<GetAttendanceListByClassResponse>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    /**
     * Retrieves an attendance summary (stats) for a specific class.
     *
     * @param classKey The unique integer key identifying the class.
     * @param query A map of query parameters for filtering the summary.
     * @return [ApiResponse] containing [GetAttendanceSummaryResponse].
     */
    override suspend fun getAttendanceByClassSummary(
        classKey: Int,
        query: Map<String, String?>,
    ): ApiResponse<GetAttendanceSummaryResponse> {
        return try {
            val response = httpClient.get("$baseUrl/by-class/$classKey/summary") {
                query.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            val body = response.body<GetAttendanceSummaryResponse>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    /**
     * Retrieves the details of a specific attendance record by its ID.
     *
     * @param id The unique identifier of the attendance record.
     * @return [ApiResponse] containing [GetAttendanceByIdResponse].
     */
    override suspend fun getAttendanceById(id: String): ApiResponse<GetAttendanceByIdResponse> {
        return try {
            val response = httpClient.get("$baseUrl/$id")
            val body = response.body<GetAttendanceByIdResponse>()
            ApiResponse.Success(body, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    /**
     * Creates a new attendance record manually.
     * Typically used by admins or teachers to record attendance without a check-in.
     *
     * @param body The [CreateAttendanceBody] containing attendance details.
     * @return [ApiResponse] containing [Unit] on success.
     */
    override suspend fun createAttendanceData(body: CreateAttendanceBody): ApiResponse<Unit> {
        return try {
            val response = httpClient.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            ApiResponse.Success(data = Unit, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    /**
     * Performs a check-in action.
     * Sends the data as Multipart/FormData, implying support for file uploads (e.g., photos).
     *
     * @param body The [CheckInBody] containing check-in data and potential files.
     * @return [ApiResponse] containing [Unit] on success.
     */
    override suspend fun checkIn(body: CheckInBody): ApiResponse<Unit> {
        return try {
            val response = httpClient.post("$baseUrl/check-in") {
                accept(ContentType.MultiPart.FormData)
                setBody(body.toMultiPartData())
            }
            ApiResponse.Success(data = Unit, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    /**
     * Retrieves aggregated data for the dashboard view.
     *
     * @param id The identifier for the user or entity requesting the dashboard.
     * @param query A map of query parameters to filter dashboard metrics.
     * @return [ApiResponse] containing [GetDashboardDataResponse].
     */
    override suspend fun getDashboardData(
        id: String,
        query: Map<String, String?>,
    ): ApiResponse<GetDashboardDataResponse> {

        return try {
            val response = httpClient.get("/dashboard") {
                query.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            val body = response.body<GetDashboardDataResponse>()
            ApiResponse.Success(data = body, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    /**
     * Edits an existing attendance record.
     * Uses Multipart/FormData, allowing updates to associated files (e.g., evidence photos).
     *
     * @param id The unique identifier of the attendance record to edit.
     * @param body The [EditAttendanceBody] containing updated data.
     * @return [ApiResponse] containing [Unit] on success.
     */
    override suspend fun editAttendance(
        id: String,
        body: EditAttendanceBody,
    ): ApiResponse<Unit> {
        return try {
            val response = httpClient.put("$baseUrl/$id") {
                contentType(ContentType.MultiPart.FormData)
                setBody(body.toMultiPartData())
            }
            ApiResponse.Success(Unit, response.status)

        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    /**
     * Retrieves attendance data formatted for export (e.g., Excel, CSV).
     *
     * @param query A map of query parameters defining the scope of the export.
     * @return [ApiResponse] containing [GetExportAttendanceDataResponse].
     */
    override suspend fun getExportData(query: Map<String, String?>): ApiResponse<GetExportAttendanceDataResponse> =
        try {
            val response = httpClient.get("$baseUrl/export") {
                contentType(ContentType.Application.Json)
                query.forEach { (key, value) ->
                    parameter(key, value)
                }
            }

            val body = response.body<GetExportAttendanceDataResponse>()
            ApiResponse.Success(body, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
}