package com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.view

import SnackBarHost
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.GlobalSettingCallback
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.GlobalSettingFormData
import com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.uistate.GlobalSettingUiState
import kotlinx.coroutines.launch

@Composable
fun GlobalSettingContent(
    cb: GlobalSettingCallback,
    uiState: GlobalSettingUiState,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isSuccess by remember { mutableStateOf(false) }
    HandleState(
        state = uiState.submitState,
        successMsg = "Success, global setting is successfully updated",
        errorMsg = "Error, failed to update global setting",
        onDispose = cb.onResetMessageState,
        onShowSnackBar = { msg, success ->
            scope.launch {
                isSuccess = success
                snackBarHostState.showSnackbar(msg)
            }
        },
        onSuccess = cb.onRefresh
    )
    Scaffold(
        showLoadingOverlay = uiState.isLoadingOverlay
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Global Setting",
                color = theme.textPrimary,
                style = Style.headerBold
            )
            Spacer(12.dp)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(theme.popUpBg)
                    .padding(screenPaddingValues),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    GlobalSettingMapSection(
                        cb = cb,
                        uiState = uiState
                    )
                }
                VerticalDivider(
                    color = theme.lineStroke,
                    thickness = 2.dp
                )
                Box(
                    modifier = Modifier.width(200.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    GlobalSettingTimeSection(
                        formData = uiState.formData,
                        onUpdateForm = cb.onUpdateForm,
                        onSubmit = cb.onSubmit,
                        isLoading = uiState.isLoading
                    )
                    SnackBarHost(
                        snackBarHostState = snackBarHostState,
                        isSuccess = isSuccess
                    )
                }
            }
        }
    }
}


@Composable
fun GlobalSettingTimeSection(
    isLoading: Boolean,
    formData: GlobalSettingFormData,
    onUpdateForm: (GlobalSettingFormData) -> Unit,
    onSubmit: (GlobalSettingFormData) -> Unit,
) {
    TextFieldTitle(
        title = "Time Setting",
        textStyle = Style.title
    ) {
        if (isLoading) {
            GLobalSettingTimeFieldLoading()
        } else {
            GlobalSettingTimeSectionContent(
                formData = formData,
                onUpdateForm = onUpdateForm,
                onSubmit = onSubmit
            )
        }
    }
}

@Composable
fun GlobalSettingTimeSectionContent(
    formData: GlobalSettingFormData,
    onUpdateForm: (GlobalSettingFormData) -> Unit,
    onSubmit: (GlobalSettingFormData) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(itemGap8),
    ) {
        TimePickerTextField(
            title = "Attendance Period Start",
            placeholder = "-",
            value = formData.attendanceStart,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        attendanceStart = it,
                        attendanceStartError = null,
                        attendanceEndError = null
                    )
                )
            },
            isError = formData.attendanceStartError != null,
            errorText = formData.attendanceStartError,
            onError = {
                onUpdateForm(
                    formData.copy(
                        attendanceStartError = it
                    )
                )
            }
        )

        TimePickerTextField(
            title = "Attendance Period End",
            placeholder = "-",
            value = formData.attendanceEnd,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        attendanceEnd = it,
                        attendanceStartError = null,
                        attendanceEndError = null
                    )
                )
            },
            isError = formData.attendanceEndError != null,
            errorText = formData.attendanceEndError,
            onError = {
                onUpdateForm(
                    formData.copy(
                        attendanceEndError = it
                    )
                )
            }
        )
        TimePickerTextField(
            title = "School Period Start",
            placeholder = "-",
            value = formData.schoolStart,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        schoolStart = it,
                        schoolStartError = null,
                        schoolEndError = null,
                    )
                )
            },
            isError = formData.schoolStartError != null,
            errorText = formData.schoolStartError,
            onError = {
                onUpdateForm(
                    formData.copy(
                        schoolStartError = it
                    )
                )
            }
        )
        TimePickerTextField(
            title = "School Period End",
            placeholder = "-",
            value = formData.schoolEnd,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        schoolEnd = it,
                        schoolStartError = null,
                        schoolEndError = null
                    )
                )
            },
            isError = formData.schoolEndError != null,
            errorText = formData.schoolEndError,
            onError = {
                onUpdateForm(
                    formData.copy(
                        schoolEndError = it
                    )
                )
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            text = "Apply",
            modifier = Modifier.fillMaxWidth()
        ) {
            onSubmit(formData)
        }
    }
}

@Composable
fun GLobalSettingTimeFieldLoading() {
    Column(
        verticalArrangement = Arrangement.spacedBy(itemGap8),
    ) {
        repeat(4) {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            )
        }
    }
}
