package com.rollinup.apiservice.data.repository.permit

import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import kotlinx.coroutines.flow.Flow

interface PermitRepository {
    fun getPermitByStudentPaging(
        id: String,
        queryParams: GetPermitListQueryParams,
    ): Flow<PagingData<PermitByStudentEntity>>

    fun getPermitByClassPaging(
        classKey: Int,
        queryParams: GetPermitListQueryParams,
    ): Flow<PagingData<PermitByClassEntity>>

    fun getPermitListByClass(
        classKey: Int,
        query: GetPermitListQueryParams,
    ): Flow<Result<List<PermitByClassEntity>, NetworkError>>

    fun getPermitListByStudent(
        id: String,
        query: GetPermitListQueryParams,
    ): Flow<Result<List<PermitByStudentEntity>, NetworkError>>

    fun getPermitById(
        id: String,
    ): Flow<Result<PermitDetailEntity, NetworkError>>

    fun doApproval(
        body: PermitApprovalBody,
    ): Flow<Result<Unit, NetworkError>>

    fun createPermit(
        body: CreateEditPermitBody,
    ): Flow<Result<Unit, NetworkError>>

    fun editPermit(
        id: String,
        body: CreateEditPermitBody,
    ): Flow<Result<Unit, NetworkError>>

    fun cancelPermit(
        id: String,
    ): Flow<Result<Unit, NetworkError>>
}