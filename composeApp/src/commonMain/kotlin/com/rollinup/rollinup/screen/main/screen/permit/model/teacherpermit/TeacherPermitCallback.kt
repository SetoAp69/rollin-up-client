package com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit

import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData

data class TeacherPermitCallback(
    val onUpdateSelection: (PermitByClassEntity) -> Unit = {},
    val onSelectAll: () -> Unit = {},
    val onTabChange: (Int) -> Unit = {},
    val onRefresh: () -> Unit = {},
    val onFilter: (PermitFilterData) -> Unit = {},
    val onSearch: (String) -> Unit = {},
)
