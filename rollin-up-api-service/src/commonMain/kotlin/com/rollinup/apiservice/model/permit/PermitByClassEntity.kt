package com.rollinup.apiservice.model.permit

data class PermitByClassEntity(
    val id: String = "",
    val name: String = "",
    val date: String = "",
    val startTime: String = "",
    val reason: String? = null,
    val approvalStatus: ApprovalStatus = ApprovalStatus.APPROVAL_PENDING,
    val type: PermitType = PermitType.DISPENSATION,
    val endTime: String = "",
    val student: User = User(),
    val createdAt: String = "",
) {
    data class User(
        val id: String = "",
        val name: String = "",
        val xClass: String = "",
    )
}
