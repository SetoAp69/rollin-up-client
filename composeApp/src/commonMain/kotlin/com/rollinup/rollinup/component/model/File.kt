package com.rollinup.rollinup.component.model

interface MultiPlatformFile {
    val name:String
    fun readBytes(): ByteArray
}