package com.rollinup.rollinup.screen.main.screen.usercenter.model

data class UserCenterFilterData(
    val classKey: List<Int> = emptyList(),
    val role: List<Int> = emptyList(),
    val gender: List<String> = emptyList(),
)
