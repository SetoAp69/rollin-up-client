package com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard

import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import kotlinx.datetime.LocalDate

data class TeacherDashboardCallback(
    val onRefresh: () -> Unit = {},
    val onResetMessageState: () -> Unit = {},
    val onUpdateFilter: (TeacherDashboardFilterData) -> Unit = {},
    val onUpdateApprovalForm: (TeacherDashboardApprovalFormData) -> Unit = {},
    val onSubmitApproval: (TeacherDashboardApprovalFormData) -> Unit = {},
    val onUpdateSelection: (AttendanceByClassEntity) -> Unit = {},
    val onSelectAll: () -> Unit = {},
    val onRefreshList: () -> Unit = {},
    val onGetDetail: (AttendanceByClassEntity) -> Unit = {},
    val onUpdateEditForm: (EditAttendanceFormData) -> Unit = {},
    val onValidateEditForm: (EditAttendanceFormData, AttendanceStatus) -> Boolean = { _, _ -> false },
    val onValidateApproval: (TeacherDashboardApprovalFormData) -> Boolean = { false },
    val onSubmitEditAttendance: (AttendanceDetailEntity, EditAttendanceFormData) -> Unit = { _, _ -> },
    val onResetSelection: () -> Unit = {},
    val onResetEditForm: () -> Unit = {},
    val onExportFile: (String) -> Unit = {},
    val onUpdateExportDateRanges: (List<LocalDate>) -> Unit = {},
)
