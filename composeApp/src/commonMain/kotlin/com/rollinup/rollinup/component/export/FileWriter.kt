package com.rollinup.rollinup.component.export

/**
 * Interface defining the contract for file writing operations, such as exporting data to Excel.
 *
 * **Note:** This interface is intended to be implemented using platform-specific mechanisms.
 * For example, Android may use specific FileProviders or Intents, while Desktop implementations
 * might use standard Java IO or different libraries.
 */
interface FileWriter {
    /**
     * Writes provided data into an Excel file.
     *
     * @param fileName The name of the file to create.
     * @param data A list of pairs where the key is the sheet name/identifier and the value is the list of data rows.
     */
    suspend fun writeExcel(
        fileName: String,
        data: List<Pair<String, List<*>>>,
    )
}