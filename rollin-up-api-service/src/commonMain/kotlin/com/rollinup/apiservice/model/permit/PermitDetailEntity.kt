package com.rollinup.apiservice.model.permit

data class PermitDetailEntity(
    val id: String = "",
    val date: String = "",
    val name: String = "",
    val student: User = User(),
    val startTime: String = "",
    val endTime: String = "",
    val attachment: String = "",
    val note: String? = null,
    val reason: String? = null,
    val createdAt: String = "",
    val updatedAt: String = "",
    val approvalStatus: String = "",
    val approvalNote: String? = null,
    val approvedBy: User? = null,
    val approvedAt: String? = null,
) {
    data class User(
        val id: String = "",
        val name: String = "",
        val username: String = "",
        val studentId: String? = null,
        val xClass: String? = null,
    )
}
