package com.rollinup.apiservice.domain.permit

import com.rollinup.apiservice.data.repository.permit.PermitRepository
import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody

class GetPermitByStudentPagingUseCase(private val repository: PermitRepository) {
    operator fun invoke(id: String, queryParams: GetPermitListQueryParams) =
        repository.getPermitByStudentPaging(id, queryParams)
}

class GetPermitByClassPagingUseCase(private val repository: PermitRepository) {
    operator fun invoke(classKey: Int, queryParams: GetPermitListQueryParams) =
        repository.getPermitByClassPaging(classKey, queryParams)
}

class GetPermitByStudentListUseCase(private val repository: PermitRepository) {
    operator fun invoke(id: String, queryParams: GetPermitListQueryParams) =
        repository.getPermitListByStudent(id, queryParams)
}

class GetPermitByClassListUseCase(private val repository: PermitRepository) {
    operator fun invoke(classKey: Int, queryParams: GetPermitListQueryParams) =
        repository.getPermitListByClass(classKey, queryParams)
}

class GetPermitByIdUseCase(private val repository: PermitRepository) {
    operator fun invoke(id: String) =
        repository.getPermitById(id)
}

class DoApprovalUseCase(private val repository: PermitRepository) {
    operator fun invoke(body: PermitApprovalBody) =
        repository.doApproval(body)
}

class CreatePermitUseCase(private val repository: PermitRepository) {
    operator fun invoke(body: CreateEditPermitBody) =
        repository.createPermit(body)
}

class EditPermitUseCase(private val repository: PermitRepository) {
    operator fun invoke(id: String, body: CreateEditPermitBody) =
        repository.editPermit(id, body)
}

class CancelPermitUseCase(private val repository: PermitRepository) {
    operator fun invoke(id: String) =
        repository.editPermit(id, CreateEditPermitBody())
}
