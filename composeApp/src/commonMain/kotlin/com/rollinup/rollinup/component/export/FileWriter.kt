package com.rollinup.rollinup.component.export

interface FileWriter {
    suspend fun writeExcel(
        fileName: String,
        data: List<Pair<String, List<*>>>,
    )
}