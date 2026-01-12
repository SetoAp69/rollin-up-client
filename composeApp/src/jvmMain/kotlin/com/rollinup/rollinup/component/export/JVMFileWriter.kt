package com.rollinup.rollinup.component.export

import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.io.writeExcel
import java.io.File

class JVMFileWriter() : FileWriter {
    override suspend fun writeExcel(
        fileName: String,
        data: List<Pair<String, List<*>>>,
    ) {
        val home = System.getProperty("user.home")
        val path = "$home/Downloads/$fileName.xlsx"
        val file = File(path).apply {
            if (!exists()) {
                createNewFile()
            }
        }
        val dataFrame = dataFrameOf(*data.toTypedArray())
        try {
            dataFrame.writeExcel(
                file = file,
                sheetName = fileName,
                writeHeader = true,
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}