package com.rollinup.rollinup.component.utils

import com.rollinup.rollinup.component.model.Platform

actual fun getPlatform(): Platform {
    return Platform.WASM
}