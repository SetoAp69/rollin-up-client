package com.rollinup.rollinup.component.export

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.michaelflisar.lumberjack.core.L
import com.rollinup.rollinup.component.filepicker.MimeType
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.io.writeExcel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual class ExportFile actual constructor() {
    private fun exportToExcel(
        context: Context,
        fileName: String,
        data: List<Pair<String, List<Any>>>,
    ) {
        val dataFrame = dataFrameOf(*data.toTypedArray())
        val resolver = context.contentResolver
        val values = ContentValues()
        values.apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, MimeType.DOCUMENT_EXCEL_TYPE)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)

        try {
            uri?.let {

                val os = resolver.openOutputStream(it)
                val inputStream = resolver.openInputStream(it)

                dataFrame.writeExcel(
                    outputStream = os!!,
                    sheetName = fileName,
                    writeHeader = true,
                    factory = HSSFWorkbook(inputStream)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            L.wtf { e.toString() }
        }
    }

    actual fun exportToExcel() {

    }
}

actual fun fileExporterModule(): Module {
    return module {
        single<FileWriter> {
            AndroidFileWriter(androidContext())
        }
    }
}