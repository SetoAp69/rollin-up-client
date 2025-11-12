package com.rollinup.rollinup.component.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.AwtWindow
import com.rollinup.rollinup.component.model.JvmFile
import com.rollinup.apiservice.model.common.MultiPlatformFile
import java.awt.FileDialog
import java.awt.Frame

@Composable
actual fun FileHandler(
    onFileSelected: (MultiPlatformFile) -> Unit,
    value: MultiPlatformFile?,
    allowedType: List<String>,
    isLaunchHandler: Boolean,
) {
    var openDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isLaunchHandler) {
        if (isLaunchHandler) openDialog = true
    }

    if (openDialog) {
        AwtWindow(
            create = {
                object : FileDialog(Frame(), "Select File", LOAD) {
                    init {
                        file = when {
                            allowedType.any { it.contains("image") } -> "*.png;*.jpg;*.jpeg"
                            allowedType.any { it.contains("pdf") } -> "*.pdf"
                            else -> "*.*"
                        }
                    }

                    override fun setVisible(value: Boolean) {
                        super.setVisible(value)
                        if (value) {
                            val selectedFile = files.firstOrNull()
                            if (selectedFile != null) {
                                onFileSelected(JvmFile(selectedFile))
                            }
                            openDialog = false
                        }
                    }
                }
            },
            dispose = FileDialog::dispose
        )
    }
}
