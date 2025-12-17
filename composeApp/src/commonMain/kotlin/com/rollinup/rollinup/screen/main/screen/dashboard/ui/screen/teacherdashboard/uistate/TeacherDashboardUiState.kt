package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate

import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.common.model.OptionData
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.rollinup.component.permitform.model.PermitFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.EditAttendanceFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardApprovalFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardFilterData
import dev.jordond.compass.Coordinates
import kotlinx.datetime.LocalDate

data class TeacherDashboardUiState(
    val user: LoginEntity = LoginEntity(),
    val isLoadingHeader: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val isLoadingDetail: Boolean = false,
    val isLoadingList: Boolean = false,
    val isAllSelected: Boolean = false,
    val itemSelected: List<AttendanceByClassEntity> = emptyList(),
    val attendanceList: List<AttendanceByClassEntity> = emptyList(),
    val submitApprovalState: Boolean? = null,
    val submitEditAttendanceState: Boolean? = null,
    val attendanceDetail: AttendanceDetailEntity = AttendanceDetailEntity(),
    val approvalFormData: TeacherDashboardApprovalFormData = TeacherDashboardApprovalFormData(),
    val editAttendanceFormData: EditAttendanceFormData = EditAttendanceFormData(),
    val filterData: TeacherDashboardFilterData = TeacherDashboardFilterData(),
    val exportDateRanges: List<LocalDate> = emptyList(),
    val exportState: Boolean? = null,
) {
    val statusOptions
        get() = AttendanceStatus.entries.map {
            OptionData(it.label, it)
        }

    val classOption
        get() = (1..5).map {
            OptionData("XII MIPA $it", it)
        }

    fun fetchEditAttendanceForm(): EditAttendanceFormData {
        return EditAttendanceFormData(
            status = attendanceDetail.status,
            permitFormData = attendanceDetail.permit?.let { permit ->
                PermitFormData(
                    duration =
                        listOf(
                            permit.startTime.parseToLocalDateTime().toEpochMillis(),
                            permit.endTime.parseToLocalDateTime().toEpochMillis()
                        ),
                    reason = permit.reason,
                    type = permit.type,
                    note = permit.note,
                    fileName = permit.attachment.substringAfter("/"),

                    )
            } ?: PermitFormData(),
            location =
                if (listOf(attendanceDetail.longitude, attendanceDetail.latitude)
                        .contains(null)
                )
                    null
                else
                    Coordinates(attendanceDetail.latitude!!, attendanceDetail.longitude!!),
            checkInTime = attendanceDetail.checkedInAt?.parseToLocalDateTime()?.toEpochMillis(),
        )
    }
}
