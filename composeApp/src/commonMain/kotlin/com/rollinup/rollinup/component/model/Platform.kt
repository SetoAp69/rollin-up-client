package com.rollinup.rollinup.component.model

enum class Platform {
    IOS, ANDROID, JVM, NATIVE, WASM
    ;

    companion object {
        fun Platform.isMobile() = this in listOf(IOS, ANDROID)
    }
}