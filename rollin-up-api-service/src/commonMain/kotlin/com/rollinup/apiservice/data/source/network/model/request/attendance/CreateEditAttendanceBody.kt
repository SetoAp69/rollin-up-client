package com.rollinup.apiservice.data.source.network.model.request.attendance

import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.common.MultiPlatformFile
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
            },
            contentType = ContentType.MultiPart.FormData
        )
    }
}


//studentUserId?.let { id->
//    add(
//        PartData.FormItem(
//            value = id,
//            dispose = {},
//            partHeaders = Headers.build {
//                append(HttpHeaders.ContentType, ContentType.Text.Plain.contentType)
//            }
//        )
//    )
//}