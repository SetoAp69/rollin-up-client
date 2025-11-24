package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByStudentResponse

interface PermitApiService {
    suspend fun getPermitListByStudent(
        id: String,
        queryParams: Map<String, String?>,
    ): ApiResponse<GetPermitListByStudentResponse>

    suspend fun cancelPermitRequest(
        id:String,
    ): ApiResponse<Unit>

    suspend fun getPermitListByClass(
        classKey: Int,
        queryParams: Map<String,String?>,
    ): ApiResponse<GetPermitListByClassResponse>

    suspend fun getPermitById(id: String): ApiResponse<GetPermitByIdResponse>

    suspend fun doApproval(body: PermitApprovalBody): ApiResponse<Unit>

    suspend fun createPermit(body: CreateEditPermitBody): ApiResponse<Unit>

    suspend fun editPermit(id: String, body: CreateEditPermitBody): ApiResponse<Unit>
}