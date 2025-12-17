package com.rollinup.apiservice.utils

import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.BuildConfig
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.FormPart
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.serialization.json.Json

object Utils {
    fun handleApiError(e: Exception): Result.Error<NetworkError> {
        L.w(e) { e.message.toString() }

        return when (e) {
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    inline fun <reified T> List<T>.toJsonString(): String? {
        return if (this.isEmpty()) null else Json.Default.encodeToString(this)
    }

    fun FormBuilder.appendFile(
        file: MultiPlatformFile,
        key: String,
    ) {
        val contentType = when (file.extension) {
            "pdf" -> ContentType.Application.Pdf
            "jpg", "jpeg", "png" -> ContentType.Image.Any
            else -> ContentType.Any
        }.toString()

        this.append(
            FormPart(
                key = key,
                value = file.name,
            )
        )
        this.appendInput(
            key = key,
            headers = Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                append(HttpHeaders.ContentType, contentType)
            }
        ){
            buildPacket { writeFully(file.readBytes()) }
        }
    }

    fun String.getFileLink():String{
        return "${BuildConfig.FILE_URL}$this"
    }
}