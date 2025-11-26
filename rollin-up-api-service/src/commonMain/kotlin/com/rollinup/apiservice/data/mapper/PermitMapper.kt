package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByStudentResponse
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.apiservice.model.permit.PermitDetailEntity
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

    fun mapPermitListByClass(data: List<GetPermitListByClassResponse.Data.PermitListDTO>): List<PermitByClassEntity> {
        return data.map { d ->
            PermitByClassEntity(
                id = d.id,
                name = d.name,
                date = d.date,
                startTime = d.startTime,
                reason = d.reason,
                approvalStatus = d.approvalStatus.let { ApprovalStatus.fromValue(it) },
                type = d.type,
                endTime = d.endTime,
                student = PermitByClassEntity.User(
                    id = d.student.id,
                    name = d.student.name,
                    xClass = d.student.xClass
                ),
                createdAt = d.createdAt
            )
        }
    }

    fun mapPermitById(data: GetPermitByIdResponse.Data): PermitDetailEntity {
        return PermitDetailEntity(
            id = data.id,
            date = data.date,
            name = data.name,
            student = data.student.let { st ->
                PermitDetailEntity.User(
                    id = st.id,
                    name = st.name,
                    username = st.username,
                    studentId = st.studentId,
                    xClass = st.xClass
                )
            },
            startTime = data.startTime,
            endTime = data.endTime,
            attachment = data.attachment,
            note = data.note,
            reason = data.reason,
            createdAt = data.createdAt,
            updatedAt = data.updatedAt,
            approvalStatus = ApprovalStatus.fromValue(data.approvalStatus),
            approvalNote = data.approvalNote,
            approvedBy = data.approvedBy?.let { ap ->
                PermitDetailEntity.User(
                    id = ap.id,
                    name = ap.name,
                    username = ap.username,
                    xClass = ap.xClass
                )
            },
            approvedAt = data.approvedAt
        )
    }
}