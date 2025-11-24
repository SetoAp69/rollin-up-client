package com.rollinup.rollinup.component.permit.model

import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.permit.PermitType

data class PermitRequestFormData(
    val type: PermitType? = null,
    val reason: String? = null,
    val attachment: MultiPlatformFile? = null,
    val note: String? = null,
    val duration: List<Long>? = null,

    val typeError: String? = null,
    val reasonError: String? = null,
    val attachmentError: String? = null,
    val noteError: String? = null,
    val durationError: String? = null,
)
