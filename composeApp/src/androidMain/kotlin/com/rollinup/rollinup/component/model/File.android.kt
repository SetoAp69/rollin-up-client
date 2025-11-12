package com.rollinup.rollinup.component.model

import com.rollinup.apiservice.model.common.MultiPlatformFile
import java.io.File

class AndroidFile(
    private val file: File,
) : MultiPlatformFile {
    override val name: String
        get() = file.name
    override val extension: String = file.extension
    override fun readBytes(): ByteArray {
        return file.readBytes()
    }
}