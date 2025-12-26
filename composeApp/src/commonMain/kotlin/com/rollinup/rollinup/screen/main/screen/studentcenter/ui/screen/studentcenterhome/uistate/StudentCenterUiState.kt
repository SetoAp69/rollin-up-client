package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.uistate

import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterFilterData

data class StudentCenterUiState(
    val user: LoginEntity = LoginEntity(),
    val isMobile: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isLoadingFilter: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val itemList: List<UserEntity> = emptyList(),
    val filterData: StudentCenterFilterData = StudentCenterFilterData(),
    val classOptions: List<OptionData<Int>> = emptyList(),
    val exportState: Boolean? = null,
) {
    val genderOptions
        get() = Gender.entries.map {
            OptionData(it.label, it.value)
        }

    val queryParams
        get() = GetUserQueryParams(
            search = searchQuery.ifBlank { null },
            gender = filterData.gender.toJsonString(),
            role = listOf(2).toJsonString(),
            classX = filterData.classX.toJsonString()
        )
}
