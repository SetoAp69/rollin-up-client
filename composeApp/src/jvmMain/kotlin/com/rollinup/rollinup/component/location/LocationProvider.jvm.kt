package com.rollinup.rollinup.component.location

import androidx.compose.runtime.Composable
import dev.jordond.compass.Location


@Composable
actual fun LocationHandler(
    onLocationChanges: (Location?) -> Unit,
    startTracking: Boolean,
) {
}