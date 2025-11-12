package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByStudentResponse
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.apiservice.model.permit.PermitType

class PermitMapper {
    fun mapPermitListByStudentResponse(data: List<GetPermitListByStudentResponse.Data.PermitListDTO>): List<PermitByStudentEntity> {
        return data.map { d ->
            PermitByStudentEntity(
                id = d.id,
                studentId = d.studentId,
                name = d.name,
                date = d.date,
                startTime = d.startTime,
                reason = d.reason,
                approvalStatus = ApprovalStatus.fromValue(d.approvalStatus),
                type = PermitType.fromValue(d.type),
                endTime = d.endTime,
                createdAt = d.createdAt
            )
        }
    }
}