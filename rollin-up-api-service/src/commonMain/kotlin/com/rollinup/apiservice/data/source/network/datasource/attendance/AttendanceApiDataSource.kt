package com.rollinup.apiservice.data.source.network.datasource.attendance

import com.rollinup.apiservice.data.source.network.apiservice.AttendanceApiService
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateEditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByStudentResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AttendanceApiDataSource(
    private val httpClient: HttpClient,
) : AttendanceApiService {
    val baseUrl = "/attendance"

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
            ApiResponse.Error(e)
        }
    }

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

    override suspend fun getAttendanceById(id: String): ApiResponse<GetAttendanceByIdResponse> {
        return try {
            val response = httpClient.get("$baseUrl/$id")
            val body = response.body<GetAttendanceByIdResponse>()
            ApiResponse.Success(body, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun createAttendanceData(body: CreateEditAttendanceBody): ApiResponse<Unit> {
        return try {
            val response = httpClient.post {
                contentType(ContentType.MultiPart.FormData)
                setBody(body.toMultiPartData())
            }
            ApiResponse.Success(data = Unit, statusCode = response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun editAttendance(
        id: String,
        body: CreateEditAttendanceBody,
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
}