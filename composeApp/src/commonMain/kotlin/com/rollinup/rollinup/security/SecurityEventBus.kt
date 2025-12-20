package com.rollinup.rollinup.security

import kotlinx.coroutines.flow.MutableSharedFlow

object SecurityEventBus {
    val rootDetected = MutableSharedFlow<Unit>()
    val locationSpoofDetected = MutableSharedFlow<Unit>()
    val timeSpoofDetected = MutableSharedFlow<Unit>()
}