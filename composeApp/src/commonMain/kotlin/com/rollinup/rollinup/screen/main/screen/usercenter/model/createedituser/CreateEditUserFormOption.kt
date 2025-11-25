package com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser

import com.rollinup.apiservice.model.common.Gender
import com.rollinup.rollinup.component.model.OptionData

data class CreateEditUserFormOption(
    val role: List<OptionData<String>> = emptyList(),
    val classX: List<OptionData<String>> = emptyList(),
) {
    val gender
        get() = Gender.entries.map {
            OptionData(it.label, it.value)
        }
}
