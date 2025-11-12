package com.rollinup.rollinup.component.model

import com.rollinup.apiservice.model.common.MultiPlatformFile
import platform.Foundation.*
import kotlinx.cinterop.*

@OptIn(ExperimentalForeignApi::class)
class IOSFile(
    val nsurl: NSURL
) : MultiPlatformFile {
    override val name: String
        get() = ""
    override val extension: String
        get() = ""

    override fun readBytes(): ByteArray {
        val data = NSData.dataWithContentsOfURL(nsurl)
            ?: throw IllegalStateException("Unable to read file at: ${nsurl.absoluteString}")
        return ByteArray(data.length.toInt()).apply {
            usePinned { pinned ->
                data.getBytes(pinned.addressOf(0), data.length)
            }
        }
    }
}