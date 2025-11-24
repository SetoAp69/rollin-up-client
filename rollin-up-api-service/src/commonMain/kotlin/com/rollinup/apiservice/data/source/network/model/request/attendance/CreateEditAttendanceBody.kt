package com.rollinup.apiservice.data.source.network.model.request.attendance

import com.rollinup.apiservice.Utils.toJsonString
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitType
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

data class CreateEditAttendanceBody(
    val studentUserId: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val attachment: MultiPlatformFile? = null,
    val status: AttendanceStatus? = null,
    val checkedInAt: Long? = null,

    val reason: String? = null,
    val duration: List<Long> = emptyList(),
    val type: PermitType? = null,
    val note: String? = null,
    val approvalStatus: ApprovalStatus? = null,
    val approvedBy: String? = null,
    val approvedAt: Long? = null,
    val approvalNote: String? = null,
) {
    fun toMultiPartData(): MultiPartFormDataContent {
        return MultiPartFormDataContent(
            parts = formData {
                studentUserId?.let {
                    append(
                        key = "studentUserId",
                        value = it,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Text.Plain.contentType)
                        }
                    )
                }
                latitude?.let {
                    append(
                        key = "latitude",
                        value = it,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Text.Plain.contentType)
                        }
                    )
                }
                longitude?.let {
                    append(
                        key = "longitude",
                        value = it,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Text.Plain.contentType)
                        }
                    )
                }
                status?.let {
                    append(
                        key = "status",
                        value = it.value,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Text.Plain.contentType)
                        }
                    )
                }
                checkedInAt?.let {
                    append(
                        key = "checkInAt",
                        value = it,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Text.Plain.contentType)
                        }
                    )
                }
                attachment?.let {
                    append(
                        key = "attachment",
                        value = it.readBytes(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Image.Any.contentType)
                        }
                    )
                }
                reason?.let {
                    append(
                        key = "reason",
                        value = it
                    )
                }
                duration.let {
                    if (it.isNotEmpty()) {
                        append(
                            key = "duration",
                            value = it.toJsonString()
                        )
                    }
                }
                type?.let {
                    append(
                        key = "type",
                        value = it.name
                    )
                }
                note?.let {
                    append(
                        key = "note",
                        value = it
                    )
                }
                approvalStatus?.let {
                    append(
                        key = "approvalStatus",
                        value = it.name
                    )
                }
                approvedAt?.let {
                    append(
                        key = "approvedAt",
                        value = it.toString()
                    )
                }
                approvalNote?.let {
                    append(
                        key = "approvalNote",
                        value = it
                    )
                }
            },
            contentType = ContentType.MultiPart.FormData
        )
    }
}