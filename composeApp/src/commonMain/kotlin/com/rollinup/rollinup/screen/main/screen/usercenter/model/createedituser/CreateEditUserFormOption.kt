package com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.common.Gender
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.model.getLabel

data class CreateEditUserFormOption(
    val role: List<OptionData<String>> = emptyList(),
    val classX: List<OptionData<String>> = emptyList(),
) {
    val gender
        @Composable
        get() = Gender.entries.map {
            OptionData(it.getLabel(), it.value)
        }
}
