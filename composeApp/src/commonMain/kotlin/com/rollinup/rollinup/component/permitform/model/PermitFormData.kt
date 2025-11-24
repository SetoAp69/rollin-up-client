package com.rollinup.rollinup.component.permitform.model

import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.permit.PermitType

data class PermitFormData(
    val duration: List<Long?> = emptyList(),
    val reason: String? = null,
    val isSick: Boolean = true,
    val type: PermitType = PermitType.ABSENT,
    val attachment: MultiPlatformFile? = null,
    val note: String? = null,
    val fileName: String? = null,
    val approvalStatus: Boolean? = null,

    val durationError: String? = null,
    val reasonError: String? = null,
    val attachmentError: String? = null,
    val noteError: String? = null,
) {
    val isValid
        get() = listOf(durationError, reasonError, attachmentError, noteError).all { it == null }
}
