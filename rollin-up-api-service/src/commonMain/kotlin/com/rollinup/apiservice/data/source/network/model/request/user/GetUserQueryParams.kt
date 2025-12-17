package com.rollinup.apiservice.data.source.network.model.request.user


data class GetUserQueryParams(
    val search: String? = null,
    val page: Int? = null,
    val limit: Int? = null,
    val sortBy: String? = null,
    val sortOrder: String? = null,
    val gender: String? = null,
    val role: String? = null,
    val classX: String? = null,
) {
    fun toQueryMap(): Map<String, String?> {
        return hashMapOf(
            "search" to search,
            "page" to page.toString(),
            "limit" to limit.toString(),
            "sortBy" to sortBy,
            "sortOrder" to sortOrder,
            "gender" to gender,
            "role" to role,
            "class" to classX
        ).filterValues({ it != null })
    }
}
