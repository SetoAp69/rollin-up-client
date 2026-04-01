package com.rollinup.rollinup.screen.main.screen.usercenter.model

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.common.Gender
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.model.getLabel

data class UserCenterFilterOption(
    val classOptions: List<OptionData<Int>> = emptyList(),
    val roleOptions: List<OptionData<Int>> = emptyList(),
) {
    val genderOptions
        @Composable
        get() = Gender.entries.map {
            OptionData(
                it.getLabel(),
                it.value
            )
        }
}
