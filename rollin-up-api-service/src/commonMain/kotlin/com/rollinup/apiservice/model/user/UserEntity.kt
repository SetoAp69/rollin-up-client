package com.rollinup.apiservice.model.user

import com.rollinup.apiservice.model.common.Gender

data class UserEntity(
    val id:String = "",
    val userName:String = "",
    val classX:String = "",
    val email:String = "",
    val fullName:String = "",
    val studentId:String = "",
    val address:String = "",
    val gender: Gender = Gender.MALE,
    val role:String = "",
)