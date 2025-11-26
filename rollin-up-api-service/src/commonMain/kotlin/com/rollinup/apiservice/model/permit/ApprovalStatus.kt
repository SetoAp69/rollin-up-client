package com.rollinup.apiservice.model.permit

import com.rollinup.common.model.Severity

enum class ApprovalStatus(val value: String, val label: String, val severity: Severity) {
    APPROVED("approved", "Approved", Severity.SUCCESS),
    APPROVAL_PENDING("approval_pending", "Approval Pending", Severity.WARNING),
    DECLINED("declined", "Declined", Severity.DANGER),
    CANCELED("canceled", "Canceled", Severity.DANGER),
    ;

    companion object {
        fun fromValue(value: String): ApprovalStatus {
            return entries.find { it.value.equals(value, true) } ?: APPROVAL_PENDING
        }
    }
}