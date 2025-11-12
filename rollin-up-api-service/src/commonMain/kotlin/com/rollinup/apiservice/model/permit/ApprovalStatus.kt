package com.rollinup.apiservice.model.permit

enum class ApprovalStatus(val value: String) {
    APPROVED("approved"),
    APPROVAL_PENDING("approval_pending"),
    REJECTED("rejected")
    ;

    companion object {
        fun fromValue(value: String): ApprovalStatus {
            return entries.find { it.value.equals(value, true) } ?: APPROVAL_PENDING
        }
    }
}