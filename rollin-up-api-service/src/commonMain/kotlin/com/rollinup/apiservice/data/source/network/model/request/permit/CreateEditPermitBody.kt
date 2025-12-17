package com.rollinup.apiservice.data.source.network.model.request.permit

import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.utils.Utils.appendFile
import com.rollinup.apiservice.utils.Utils.toJsonString
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData

data class CreateEditPermitBody(
    val studentId: String? = null,
    val reason: String? = null,
    val duration: List<Long>? = null,
    val type: PermitType? = null,
    val note: String? = null,
    val attachment: MultiPlatformFile? = null,
    val approvalStatus: ApprovalStatus? = null,
    val approvedBy: String? = null,
    val approvedAt: Long? = null,
    val approvalNote: String? = null,
) {
    fun toMultiPart(): MultiPartFormDataContent {
        return MultiPartFormDataContent(
            parts = formData {
                studentId?.let {
                    append(
                        key = "studentId",
                        value = it,
                    )
                }
                reason?.let {
                    append(
                        key = "reason",
                        value = it
                    )
                }
                duration?.toJsonString()?.let {
                    append(
                        key = "duration",
                        value = it
                    )
                }
                type?.let {
                    append(
                        key = "type",
                        value = it.value
                    )
                }
                attachment?.let {
                    appendFile(
                        file = it,
                        key = "attachment"
                    )
                }
                approvalStatus?.let {
                    append(
                        key = "approvalStatus",
                        value = it.value
                    )
                }
                approvedBy?.let {
                    append(
                        key = "approvedBy",
                        value = it
                    )
                }
                approvedAt?.let {
                    append(
                        key = "approvedAt",
                        value = it
                    )
                }
                approvalNote?.let {
                    append(
                        key = "approvalNote",
                        value = it
                    )
                }
            },
        )
    }
}
