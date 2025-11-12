package com.rollinup.rollinup.component.filepicker

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.common.MultiPlatformFile

@Composable
actual fun FileHandler(
    onFileSelected: (MultiPlatformFile) -> Unit,
    value: MultiPlatformFile?,
    allowedType: List<String>,
    isLaunchHandler: Boolean,
) {
}