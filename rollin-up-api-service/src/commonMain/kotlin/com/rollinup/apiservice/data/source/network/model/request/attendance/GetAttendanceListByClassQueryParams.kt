package com.rollinup.apiservice.data.source.network.model.request.attendance

data class GetAttendanceListByClassQueryParams(
    val limit: Int? = null,
    val page: Int? = null,
    val sortBy: String? = null,
    val order: String? = null,
    val search: String? = null,
    val status: String? = null,
    val date: String? = null,
) {
    fun toQueryMap(): Map<String, String?> {
        return mapOf(
            "limit" to limit.toString(),
            "page" to page.toString(),
            "sortBy" to sortBy,
            "order" to order,
            "search" to search,
            "status" to status?.toString(),
            "date" to date.toString(),
        ).filterValues { it != null }
    }
}