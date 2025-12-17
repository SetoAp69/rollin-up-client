package com.rollinup.apiservice.model.user

import com.rollinup.common.model.OptionData

data class UserOptionEntity(
    val roleOptions: List<OptionData<Int>> = emptyList(),
    val classOptions: List<OptionData<Int>> = emptyList(),
    val roleIdOptions: List<OptionData<String>> = emptyList(),
    val classIdOptions: List<OptionData<String>> = emptyList(),
)
