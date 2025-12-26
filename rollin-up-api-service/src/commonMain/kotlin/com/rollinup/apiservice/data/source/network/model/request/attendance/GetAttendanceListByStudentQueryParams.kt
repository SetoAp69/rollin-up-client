package com.rollinup.apiservice.data.source.network.model.request.attendance

data class GetAttendanceListByStudentQueryParams(
    val search: String? = null,
    val limit: Int? = null,
    val page: Int? = null,
    val status: String? = null,
    val dateRange: String? = null,
) {
    fun toQueryMap(): Map<String, String?> {
        return mapOf(
            "search" to search,
            "limit" to limit.toString(),
            "page" to page.toString(),
            "status" to status,
            "dateRange" to dateRange
        ).filterValues { it != null }
    }
}
