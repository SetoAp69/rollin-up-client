package com.rollinup.apiservice.model.common

interface MultiPlatformFile {
    val name:String
    val extension:String
    fun readBytes(): ByteArray
}