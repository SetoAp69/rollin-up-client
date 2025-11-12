package com.rollinup.rollinup.component.filepicker

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.rollinup.rollinup.component.model.AndroidFile
import com.rollinup.apiservice.model.common.MultiPlatformFile
import java.io.File


@Composable
actual fun FileHandler(
    onFileSelected: (MultiPlatformFile) -> Unit,
    value: MultiPlatformFile?,
    allowedType: List<String>,
    isLaunchHandler: Boolean,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            val file = uri?.toFile(context)
            file?.let {
                onFileSelected(
                    AndroidFile(it)
                )
            }
        }
    )

    LaunchedEffect(isLaunchHandler) {
        if (isLaunchHandler) {
            launcher.launch(
                allowedType.toTypedArray()
            )
        }
    }
}

private fun Uri.toFile(context: Context): File? {
    val inputStream = context.contentResolver.openInputStream(this)
    val mimeType = context.contentResolver.getType(this)
    val extension = when (mimeType) {
        "image/jpeg" -> "jpg"
        "image/png" -> "png"
        "application/pdf" -> "pdf"
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> "xlsx"
        else -> ""
    }

    val tempFile = File.createTempFile(this.path?.substringAfterLast("/") ?: "temp", ".$extension")
    return try {
        tempFile.outputStream().use { fileOut ->
            inputStream?.copyTo(fileOut)
        }
        tempFile.deleteOnExit()
        inputStream?.close()
        tempFile
    } catch (e: Exception) {
        null
    }

}