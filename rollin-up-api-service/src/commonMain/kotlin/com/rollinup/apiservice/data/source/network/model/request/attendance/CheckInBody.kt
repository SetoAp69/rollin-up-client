package com.rollinup.apiservice.data.source.network.model.request.attendance

import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.utils.Utils.appendFile
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

data class CheckInBody(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val attachment: MultiPlatformFile? = null,
    val checkedInAt: Long? = null,
    val date: String? = null,
) {
    fun toMultiPartData(): MultiPartFormDataContent {
        return MultiPartFormDataContent(
            parts = formData {
                date?.let {
                    append(
                        key = "date",
                        value = it
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
                    appendFile(
                        file = it,
                        key = "attachment"
                    )
                }
            },
        )
    }
}