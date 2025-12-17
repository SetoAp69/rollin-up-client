package com.rollinup.apiservice.data.source.network.model.request.attendance

data class GetExportAttendanceDataQueryParams(
    val classKey: Int? = null,
    val dateRange: String? = null,
) {
    fun toQueryMap(): Map<String, String?> {
        return mapOf(
            "class" to classKey?.toString(),
            "dateRange" to dateRange
        ).filterValues { it != null }
    }
}
