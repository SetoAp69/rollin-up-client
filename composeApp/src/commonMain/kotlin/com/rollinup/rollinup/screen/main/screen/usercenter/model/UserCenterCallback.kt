package com.rollinup.rollinup.screen.main.screen.usercenter.model

import com.rollinup.apiservice.model.user.UserEntity

data class UserCenterCallback(
    val onSearch: (String) -> Unit = {},
    val onFilter: (UserCenterFilterData) -> Unit = {},
    val onRefresh: () -> Unit = {},
    val onSelectAll: () -> Unit = {},
    val onUpdateSelection: (UserEntity) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
    val onDeleteUser: (List<String>) -> Unit = {},
)
