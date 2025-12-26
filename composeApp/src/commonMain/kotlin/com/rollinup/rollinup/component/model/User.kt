package com.rollinup.rollinup.component.model

data class User(
    val id: String = "",
    val role: String = "",
    val fullName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val classX: Class? = null,
    val deviceId: String? = null,
    val profilePicture: String = "",
    val phoneNumber: String = "",
    val accessToken: String = "",
    val studentId: String? = null,
    val address: String? = null,
) {
    data class Class(
        val id: String = "",
        val name: String = "",
        val grade: Int = 0,
        val key: Int = 0,
    )
}
