package com.rollinup.rollinup.component.export

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toFile
import com.michaelflisar.lumberjack.core.L
import com.rollinup.rollinup.component.filepicker.MimeType
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.io.writeExcel

class AndroidFileWriter(
    private val context: Context,
) : FileWriter {
    override suspend fun writeExcel(
        fileName: String,
        data: List<Pair<String, List<*>>>,
    ) {
        val dataFrame = dataFrameOf(*data.toTypedArray())
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, MimeType.DOCUMENT_EXCEL_TYPE)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(
            MediaStore.Files.getContentUri("external"), values
        ) ?: return

        try {
            val file = uri.toFile()
            dataFrame.writeExcel(
                file = file,
                keepFile = true,
                sheetName = fileName.substringBeforeLast(".")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            L.wtf { e.toString() }
        }
    }

}