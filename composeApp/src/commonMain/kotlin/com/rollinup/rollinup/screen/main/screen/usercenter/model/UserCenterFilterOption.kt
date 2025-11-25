package com.rollinup.rollinup.screen.main.screen.usercenter.model

import com.rollinup.apiservice.model.common.Gender
import com.rollinup.rollinup.component.model.OptionData

data class UserCenterFilterOption(
    val classOptions: List<OptionData<Int>> = emptyList(),
    val roleOptions: List<OptionData<Int>> = emptyList(),
) {
    val genderOptions
        get() = Gender.entries.map {
            OptionData(
                it.label,
                it.value
            )
        }
}
