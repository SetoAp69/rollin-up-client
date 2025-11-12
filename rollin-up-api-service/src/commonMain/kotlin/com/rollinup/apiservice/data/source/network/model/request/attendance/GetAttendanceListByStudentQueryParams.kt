package com.rollinup.apiservice.data.source.network.model.request.attendance

data class GetAttendanceListByStudentQueryParams(
    val search: String? = null,
    val limit: Int? = null,
    val page: Int? = null,
    val dateRange: List<String>? = null,
) {
    fun toQueryMap(): Map<String, String?> {
        return mapOf(
            "search" to search,
            "limit" to limit.toString(),
            "page" to page.toString(),
            "dateRange" to dateRange?.toString()
        ).filterValues { it != null }
    }
}
