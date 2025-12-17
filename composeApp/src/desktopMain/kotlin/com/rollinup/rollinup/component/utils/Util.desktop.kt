package com.rollinup.rollinup.component.utils

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.model.Orientation

@Composable
actual fun getOrientation(): Orientation {
    return Orientation.PORTRAIT
}