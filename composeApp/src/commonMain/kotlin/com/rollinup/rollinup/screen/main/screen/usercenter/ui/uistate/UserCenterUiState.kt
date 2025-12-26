package com.rollinup.rollinup.screen.main.screen.usercenter.ui.uistate

import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterFilterData
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterFilterOption

data class UserCenterUiState(
    val isLoadingList: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val isLoadingFilter: Boolean = false,
    val deleteUserState: Boolean? = null,
    val searchQuery: String = "",
    val itemList: List<UserEntity> = emptyList(),
    val itemSelected: List<UserEntity> = emptyList(),
    val userSelected: List<UserEntity> = emptyList(),
    val filterData: UserCenterFilterData = UserCenterFilterData(),
    val user: LoginEntity = LoginEntity(),
    val filterOptions: UserCenterFilterOption = UserCenterFilterOption(),
) {
    val isAllSelected
        get() = itemList.size == itemSelected.size

    val queryParams
        get() = GetUserQueryParams(
            search = searchQuery.ifBlank { null },
            gender = filterData.gender.toJsonString(),
            role = filterData.role.toJsonString(),
            classX = filterData.classKey.toJsonString(),
        )
}