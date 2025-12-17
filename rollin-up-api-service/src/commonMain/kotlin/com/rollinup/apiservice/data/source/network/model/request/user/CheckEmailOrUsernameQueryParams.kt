package com.rollinup.apiservice.data.source.network.model.request.user

data class CheckEmailOrUsernameQueryParams(
    val email: String? = null,
    val username: String? = null,
) {
    fun toQueryMap() = mapOf(
        "email" to email,
        "username" to username
    ).filterValues { it != null }
}
