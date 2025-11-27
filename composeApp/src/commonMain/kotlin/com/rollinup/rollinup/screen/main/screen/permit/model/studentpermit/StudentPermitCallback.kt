package com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit

import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData

data class StudentPermitCallback(
    val onTabChange: (Int) -> Unit = {},
    val onRefresh: () -> Unit = {},
    val onFilter: (PermitFilterData) -> Unit = {},
    val onSearch: (String) -> Unit = {},
    val onCancelPermit: (String) -> Unit = {},
    val resetMessageState:()->Unit = {}
)
