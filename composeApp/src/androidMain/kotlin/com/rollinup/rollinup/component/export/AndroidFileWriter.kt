package com.rollinup.rollinup.component.export

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.michaelflisar.lumberjack.core.L
import com.rollinup.rollinup.component.filepicker.MimeType
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.io.WorkBookType
import org.jetbrains.kotlinx.dataframe.io.writeExcel
import java.io.File

class AndroidFileWriter(
    private val context: Context,
) : FileWriter {
    override suspend fun writeExcel(
        fileName: String,
        data: List<Pair<String, List<*>>>,
    ) {
        val dataFrame = dataFrameOf(*data.toTypedArray())
        val resolver = context.contentResolver

        val tempFile = File(context.cacheDir, "fileName.xlsx")
        if (!tempFile.exists())
            tempFile.createNewFile()

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Files.getContentUri("external")
        }


        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.csv")
            put(MediaStore.MediaColumns.IS_DOWNLOAD, true)
            put(MediaStore.MediaColumns.MIME_TYPE, MimeType.DOCUMENT_EXCEL_TYPE)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val newUri = resolver.insert(uri, values) ?: return
        try {
            val outputStream = resolver.openOutputStream(newUri)
            val inputStream = tempFile.inputStream()

            outputStream?.let {
                inputStream.copyTo(outputStream)
            }
            dataFrame.writeExcel(
                file = tempFile,
                workBookType = WorkBookType.XLSX,
                sheetName = fileName
            )
            outputStream?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
            L.e { e.stackTraceToString() }
        }

//        try {
//            resolver.openOutputStream(newUri)?.let {
//                dataFrame.writeC
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            L.wtf { e.toString() }
//        }
    }

}