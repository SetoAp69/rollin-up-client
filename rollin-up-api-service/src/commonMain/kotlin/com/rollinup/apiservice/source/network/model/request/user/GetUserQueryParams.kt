package com.rollinup.apiservice.source.network.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserQueryParams(
    @SerialName("search")
    val search: String? = null,
    @SerialName("page")
    val page: Int? = null,
    @SerialName("limit")
    val limit: Int? = null,
    @SerialName("sortBy")
    val sortBy: String? = null,
    @SerialName("sortOrder")
    val sortOrder: String? = null,
    @SerialName("gender")
    val gender: String? = null,
    @SerialName("role")
    val role: String? = null,
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
        ).filterValues({ it != null })
    }
}
