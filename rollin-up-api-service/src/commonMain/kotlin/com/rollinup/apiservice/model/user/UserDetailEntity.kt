package com.rollinup.apiservice.model.user

import com.rollinup.apiservice.model.common.Gender

data class UserDetailEntity(
    val id: String = "",
    val userName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val classX: Data? = null,
    val email: String = "",
    val fullName: String = "",
    val studentId: String = "",
    val address: String = "",
    val gender: Gender = Gender.MALE,
    val phoneNumber:String = "",
    val birthDay:String = "",
    val role:String = ""
) {
    data class Data(
        val id: String = "",
        val name: String = "",
        val key: Int = 0,
    )
}
