package com.rollinup.rollinup.screen.main.screen.studentcenter.model

data class StudentCenterCallback(
    val onSearch: (String) -> Unit = {},
    val onFilter: (StudentCenterFilterData) -> Unit = {},
    val onRefresh: () -> Unit = {},
    val onExportFile: (String) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
)
