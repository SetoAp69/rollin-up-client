package com.rollinup.apiservice.data.source.network.model.request.permit

data class GetPermitListQueryParams(
    val limit: Int? = null,
    val page: Int? = null,
    val sortBy: String? = null,
    val order: String? = null,
    val search: String? = null,
    val listId: String? = null,
    val isActive: String = "true",
    val type: String? = null,
    val dateRange: String? = null,
    val date: String? = null,
    val status: String? = null,
) {
    fun toQueryMap(): Map<String, String?> {
        return mapOf(
            "limit" to limit.toString(),
            "page" to page.toString(),
            "sortBy" to sortBy,
            "order" to order,
            "search" to search,
            "listId" to listId,
            "isActive" to isActive,
            "type" to type,
            "dateRange" to dateRange,
            "date" to date,
            "status" to status,
        ).filterValues { it != null }
    }
}
