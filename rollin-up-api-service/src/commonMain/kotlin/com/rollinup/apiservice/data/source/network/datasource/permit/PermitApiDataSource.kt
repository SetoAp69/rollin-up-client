package com.rollinup.apiservice.data.source.network.datasource.permit

import com.rollinup.apiservice.data.source.network.apiservice.PermitApiService
import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByStudentResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PermitApiDataSource(
    val httpClient: HttpClient,
) : PermitApiService {
    val baseUrl = "/permit"
    override suspend fun getPermitListByStudent(
        id: String,
        queryParams: Map<String, String?>,
    ): ApiResponse<GetPermitListByStudentResponse> {
        return try {
            val response = httpClient.get("$baseUrl/by-student/$id") {
                queryParams.forEach { (key, value) ->
                    parameter(key = key, value = value)
                }
            }

            val body = response.body<GetPermitListByStudentResponse>()
            ApiResponse.Success(body, response.status)

        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun cancelPermitRequest(id: String): ApiResponse<Unit> {
        return try {
            val response = httpClient.put("$baseUrl/$id/cancel")
            val body = response.body<Unit>()
            ApiResponse.Success(body, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun getPermitListByClass(
        classKey: Int,
        queryParams: Map<String, String?>,
    ): ApiResponse<GetPermitListByClassResponse> {
        return try {
            val response = httpClient.get("$baseUrl/by-class/$classKey") {
                queryParams.forEach { (key, value) ->
                    parameter(key = key, value = value)
                }
            }

            val body = response.body<GetPermitListByClassResponse>()
            ApiResponse.Success(body, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun getPermitById(id: String): ApiResponse<GetPermitByIdResponse> {
        return try {
            val response = httpClient.get("$baseUrl/$id")
            val body = response.body<GetPermitByIdResponse>()
            ApiResponse.Success(body, response.status)

        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun doApproval(body: PermitApprovalBody): ApiResponse<Unit> {
        return try {
            val response = httpClient.post("$baseUrl/approval") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }

            ApiResponse.Success(Unit, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun createPermit(body: CreateEditPermitBody): ApiResponse<Unit> {
        return try {
            val response = httpClient.post(baseUrl) {
                contentType(ContentType.MultiPart.FormData)
                setBody(body.toMultiPart())
            }

            ApiResponse.Success(Unit, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun editPermit(
        id: String,
        body: CreateEditPermitBody,
    ): ApiResponse<Unit> {
        return try {
            val response = httpClient.post("$baseUrl/$id") {
                contentType(ContentType.MultiPart.FormData)
                setBody(body.toMultiPart())
            }

            ApiResponse.Success(Unit, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

}