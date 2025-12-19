package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.common.model.OptionData
import com.rollinup.common.utils.Utils.now
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.permitform.view.PermitFormContent
import com.rollinup.rollinup.component.selector.SingleSelector
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.textfield.TextFieldDefaults
import com.rollinup.rollinup.component.theme.LocalGlobalSetting
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.time.TimePickerTextField
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view.SubmitAttendanceEditDialog
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.EditAttendanceFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import dev.jordond.compass.Coordinates
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_edit_line_24

@Composable
fun TeacherDashboardEditAttendance(
    isShowForm: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
) {
    if (getPlatform().isMobile()) {
        BottomSheet(
            isShowSheet = isShowForm,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.padding(horizontal = screenPadding)
        ) {
            TeacherDashboardEditAttendanceContent(
                uiState = uiState,
                cb = cb,
                onDismissRequest = onDismissRequest
            )
        }
    } else {
        val heightMax = getScreenHeight() * 0.8f
        val widthMax = getScreenWidth() * 0.3f

        Dialog(
            showDialog = isShowForm,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .sizeIn(maxWidth = widthMax, maxHeight = heightMax)
                .padding(screenPadding),
        ) {
            TeacherDashboardEditAttendanceContent(
                uiState = uiState,
                cb = cb,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
fun TeacherDashboardEditAttendanceContent(
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
    onDismissRequest: (Boolean) -> Unit,
) {
    val detail = uiState.attendanceDetail
    val initialStatus = detail.status
    val formData = uiState.editAttendanceFormData
    var showDialog by remember { mutableStateOf(false) }
    val generalSetting = LocalGlobalSetting.current

    LaunchedEffect(uiState.submitEditAttendanceState){
        if(uiState.submitEditAttendanceState == true) onDismissRequest(false)
    }

    LaunchedEffect(uiState.attendanceDetail) {
        cb.onUpdateEditForm(uiState.fetchEditAttendanceForm())
    }

    LaunchedEffect(uiState.editAttendanceFormData.status) {
        if (uiState.editAttendanceFormData.status == initialStatus) {
            cb.onUpdateEditForm(uiState.fetchEditAttendanceForm())
        }

        when (uiState.editAttendanceFormData.status) {
            initialStatus -> {
                cb.onUpdateEditForm(uiState.fetchEditAttendanceForm())
            }

            in listOf(
                AttendanceStatus.ON_TIME,
                AttendanceStatus.ON_TIME
            ),
                -> {
                cb.onUpdateEditForm(
                    uiState.editAttendanceFormData.copy(
                        location = Coordinates(
                            latitude = generalSetting.latitude,
                            longitude = generalSetting.longitude
                        )
                    )
                )
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                onUpdateFormData = cb.onUpdateEditForm,
                formData = formData,
                isLoading = uiState.isLoadingDetail
            )

            if (uiState.isLoadingDetail) {
                EditAttendanceLoading()
            } else {
                when (formData.status) {
                    AttendanceStatus.ABSENT, AttendanceStatus.EXCUSED -> {
                        PermitFormContent(
                            formData = formData,
                            onUpdateFormData = cb.onUpdateEditForm
                        )
                    }

                    AttendanceStatus.ON_TIME, AttendanceStatus.LATE -> {
                        AttendanceForm(
                            formData = formData,
                            onUpdateFormData = cb.onUpdateEditForm,
                        )
                    }

                    else -> {}
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Submit"
        ) {
            if (formData == uiState.fetchEditAttendanceForm()) onDismissRequest(false)
            if (!cb.onValidateEditForm(formData, initialStatus)) return@Button
            showDialog = true
        }
    }

    SubmitAttendanceEditDialog(
        isShowDialog = showDialog,
        onDismissRequest = { showDialog = it },
        status = formData.status,
        initialStatus = initialStatus,
        onConfirm = {
            cb.onSubmitEditAttendance(detail, formData)
        },
        onCancel = {}
    )
}

@Composable
private fun Header(
    isLoading: Boolean,
    onUpdateFormData: (EditAttendanceFormData) -> Unit,
    formData: EditAttendanceFormData,
) {
    var showSelectStatus by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Status",
            color = theme.bodyText,
            style = Style.body
        )
        if (isLoading) {
            ShimmerEffect(60.dp, 24.dp)
        } else {
            Box{
                Chip(
                    text = formData.status.label,
                    trailingIcon = Res.drawable.ic_edit_line_24,
                    onClickTrailIcon = {
                        showSelectStatus = true
                    },
                    severity = formData.status.severity
                )
                SingleSelector(
                    options = AttendanceStatus.entries.map {
                        OptionData(it.label, it)
                    },
                    value = formData.status,
                    onDismissRequest = { showSelectStatus = it },
                    isShowSelector = showSelectStatus,
                    onValueChange = { status ->
                        var newFormData = EditAttendanceFormData(status = status)
                        when (status) {
                            AttendanceStatus.ABSENT -> {
                                newFormData = newFormData.copy(
                                    permitFormData = formData.permitFormData.copy(
                                        type = PermitType.ABSENCE
                                    )
                                )
                            }

                            AttendanceStatus.EXCUSED -> {
                                newFormData = newFormData.copy(
                                    permitFormData = formData.permitFormData.copy(
                                        type = PermitType.DISPENSATION
                                    )
                                )
                            }

                            else -> {}
                        }
                        onUpdateFormData(newFormData)
                    },
                    title = "Select Attendance Status"
                )
            }
        }
    }


}

@Composable
fun AttendanceForm(
    formData: EditAttendanceFormData,
    onUpdateFormData: (EditAttendanceFormData) -> Unit,
) {
    TimePickerTextField(
        title = "Check in time",
        placeholder = "Select time",
        value = formData.checkInTime,
        onValueChange = {
            onUpdateFormData(
                formData.copy(
                    checkInTime = it,
                    checkInTimeError = null
                )
            )
        },
    )

}

@Composable
private fun PermitFormContent(
    formData: EditAttendanceFormData,
    onUpdateFormData: (EditAttendanceFormData) -> Unit,
) {
    val duration = formData.permitFormData.duration
    LaunchedEffect(duration){
        if(duration.isNotEmpty()){
            val from = duration.first()!!.parseToLocalDateTime()
            val to = duration.last()!!.parseToLocalDateTime()
            val today = LocalDateTime(LocalDate.now(), LocalTime(0,0,0))
            if(today !in from..to){
                onUpdateFormData(
                    formData.copy(
                        permitFormData = formData.permitFormData.copy(
                            durationError = "Permit must include current date"
                        )
                    )
                )
            }
        }
    }

    PermitFormContent(
        formData = formData.permitFormData,
        onUpdateFormData = {
            onUpdateFormData(
                formData.copy(
                    permitFormData = it
                )
            )
        }
    )

}

@Composable
private fun EditAttendanceLoading() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(3) {
            ShimmerEffect(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .height(TextFieldDefaults.height)
            )
        }
    }
}