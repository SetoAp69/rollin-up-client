package com.rollinup.apiservice.model.permit

data class PermitByStudentEntity(
    val id: String = "",
    val studentId: String = "",
    val name: String = "",
    val date: String = "",
    val startTime: String = "",
    val reason: String? = null,
    val approvalStatus: ApprovalStatus = ApprovalStatus.APPROVAL_PENDING,
    val type: PermitType = PermitType.DISPENSATION,
    val endTime: String = "",
    val createdAt: String = "",
)
