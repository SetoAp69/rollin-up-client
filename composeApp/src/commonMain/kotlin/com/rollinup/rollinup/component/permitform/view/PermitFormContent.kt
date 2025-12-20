package com.rollinup.rollinup.component.permitform.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.date.DateRangePickerField
import com.rollinup.rollinup.component.filepicker.FilePicker
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.loading.LoadingOverlay
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.permitform.model.PermitFormCallback
import com.rollinup.rollinup.component.permitform.model.PermitFormData
import com.rollinup.rollinup.component.permitform.uistate.PermitFormUiState
import com.rollinup.rollinup.component.radio.RadioSelectorRow
import com.rollinup.rollinup.component.selector.SingleSelector
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.textfield.TextFieldDefaults
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.globalSetting
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.time.TimeDurationTextField
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_right_24
import rollin_up.composeapp.generated.resources.label_absent
import rollin_up.composeapp.generated.resources.label_attachment
import rollin_up.composeapp.generated.resources.label_create_permit_request
import rollin_up.composeapp.generated.resources.label_dispensation
import rollin_up.composeapp.generated.resources.label_duration
import rollin_up.composeapp.generated.resources.label_edit_permit_request
import rollin_up.composeapp.generated.resources.label_note
import rollin_up.composeapp.generated.resources.label_permit_type
import rollin_up.composeapp.generated.resources.label_reason
import rollin_up.composeapp.generated.resources.label_select_permit_duration
import rollin_up.composeapp.generated.resources.label_submit
import rollin_up.composeapp.generated.resources.msg_duration_error_invalid
import rollin_up.composeapp.generated.resources.msg_duration_error_too_early
import rollin_up.composeapp.generated.resources.msg_duration_error_too_late
import rollin_up.composeapp.generated.resources.msg_file_error_max_size
import rollin_up.composeapp.generated.resources.msg_permit_submit_error
import rollin_up.composeapp.generated.resources.msg_permit_submit_success
import rollin_up.composeapp.generated.resources.ph_permit_note
import rollin_up.composeapp.generated.resources.ph_permit_reason

@Composable
fun PermitFormContent(
    formData: PermitFormData,
    onUpdateFormData: (PermitFormData) -> Unit,
) {
    PermitFormDurationSection(
        formData = formData,
        onUpdateFormData = onUpdateFormData
    )
    if (formData.type == PermitType.ABSENCE) {
        PermitReasonSection(
            onUpdateFormData = onUpdateFormData,
            formData = formData
        )
    }
    val maxFileSizeError = stringResource(Res.string.msg_file_error_max_size)
    FilePicker(
        title = stringResource(Res.string.label_attachment),
        isRequired = true,
        isError = formData.attachmentError != null,
        errorMsg = formData.attachmentError,
        value = formData.attachment,
        fileName = formData.fileName,
        showCameraOption = true,
        onValueChange = {
            val newValue: MultiPlatformFile?
            val textError: String?

            if ((it?.readBytes()?.size ?: 0) > 1 * 1024 * 1024) {
                newValue = null
                textError = maxFileSizeError
            } else {
                newValue = it
                textError = null
            }

            onUpdateFormData(
                formData.copy(
                    attachment = newValue,
                    attachmentError = textError
                )
            )
        }
    )
    TextField(
        title = stringResource(Res.string.label_note),
        value = formData.note ?: "",
        onValueChange = {
            onUpdateFormData(
                formData.copy(
                    note = it,
                    noteError = null
                )
            )
        },
        placeholder = stringResource(Res.string.ph_permit_note),
        isError = formData.noteError != null,
        errorMsg = formData.noteError
    )
}

@Composable
fun PermitFormContent(
    uiState: PermitFormUiState,
    cb: PermitFormCallback,
    onSuccess: () -> Unit,
    onError: () -> Unit,
    onShowSnackbar: OnShowSnackBar,
) {
    val formData = uiState.formData
    val isEdit = uiState.isEdit
    val title =
        if (isEdit) stringResource(Res.string.label_edit_permit_request)
        else stringResource(Res.string.label_create_permit_request)

    LoadingOverlay(uiState.isLoadingOverlay)

    LaunchedEffect(formData.type) {
        if (formData.type == PermitType.DISPENSATION) {
            cb.onUpdateFormData(
                formData.copy(
                    reason = null,
                    duration = emptyList(),
                    reasonError = null,
                    durationError = null
                )
            )
        }
    }

    HandleState(
        state = uiState.submitState,
        successMsg = stringResource(Res.string.msg_permit_submit_success),
        errorMsg = stringResource(Res.string.msg_permit_submit_error),
        onDispose = cb.onResetMessageState,
        onError = onSuccess,
        onSuccess = onSuccess,
        onShowSnackBar = onShowSnackbar,
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PermitFormHeader(
            title = title,
            formData = formData,
            onUpdateFormData = cb.onUpdateFormData
        )
        PermitFormContent(
            formData = formData,
            onUpdateFormData = cb.onUpdateFormData
        )
        Button(
            text = "Submit",
            modifier = Modifier.fillMaxWidth()
        ) {
            cb.onSubmit(formData, onSuccess, onError)
        }

    }
}

@Composable
fun PermitReasonSection(
    onUpdateFormData: (PermitFormData) -> Unit,
    formData: PermitFormData,
) {
    val options = listOf(
        OptionData("Sick", true),
        OptionData("Other", false)
    )

    TextFieldTitle(
        title = stringResource(Res.string.label_reason),
        isRequired = true,
    ) {
        RadioSelectorRow(
            options = options,
            modifier = Modifier.fillMaxWidth(),
            value = formData.isSick,
            onValueChange = { isSick ->
                val reason = if (isSick) "Sick" else null

                onUpdateFormData(
                    formData.copy(
                        isSick = isSick,
                        reason = reason,
                        reasonError = null
                    )
                )
            }
        )

        if (!formData.isSick) {
            TextField(
                value = formData.reason ?: "",
                onValueChange = {
                    onUpdateFormData(
                        formData.copy(
                            reason = it,
                            reasonError = null
                        )
                    )
                },
                placeholder = stringResource(Res.string.ph_permit_reason),
                isError = formData.reasonError != null,
                errorMsg = formData.reasonError
            )
        }
    }
}

@Composable
fun PermitFormDurationSection(
    formData: PermitFormData,
    onUpdateFormData: (PermitFormData) -> Unit,
) {
    when (formData.type) {
        PermitType.ABSENCE -> {
            @Suppress("UNCHECKED_CAST")
            DateRangePickerField(
                title = stringResource(Res.string.label_duration),
                placeholder = stringResource(Res.string.label_select_permit_duration),
                value = formData.duration.filter { it != null } as List<Long>,
                isError = formData.durationError != null,
                errorText = formData.durationError
            ) {
                onUpdateFormData(
                    formData.copy(
                        duration = it,
                        durationError = null
                    )
                )
            }
        }

        PermitType.DISPENSATION -> {
            val schoolStart = globalSetting.schoolPeriodStart
            val schoolEnd = globalSetting.schoolPeriodEnd

            val tooEarlyMsgError = stringResource(Res.string.msg_duration_error_too_early)
            val tooLateMsgError = stringResource(Res.string.msg_duration_error_too_late)
            val invalidDurationError = stringResource(Res.string.msg_duration_error_invalid)

            TimeDurationTextField(
                value = formData.duration.map { second ->
                    if (second == null) null
                    else LocalTime.fromSecondOfDay(second.toInt())
                },
                onValueChange = { value ->
                    val from = value.first()
                    val to = value[1]

                    val min = value.first() ?: schoolStart
                    val max = value[1] ?: schoolEnd

                    val errorMessage = when {
                        from != null && to == null && from < schoolStart -> tooEarlyMsgError
                        to != null && from == null && to > schoolEnd -> tooLateMsgError
                        (to != null && to < min) || (from != null && from > max) -> invalidDurationError
                        (value.all { it != null } && to!! > schoolEnd && from!! < schoolStart) -> invalidDurationError
                        else -> null
                    }

                    onUpdateFormData(
                        formData.copy(
                            duration = value.map { time ->
                                if (time == null) null
                                else time.toSecondOfDay().toLong()
                            },
                            durationError = errorMessage
                        )
                    )
                },
                isRequired = true,
                isError = formData.durationError != null,
                textError = formData.durationError,
                title = stringResource(Res.string.label_duration)
            )
        }
    }
}

@Composable
fun PermitFormHeader(
    title: String,
    formData: PermitFormData,
    onUpdateFormData: (PermitFormData) -> Unit,
) {
    var isShowSelector by remember { mutableStateOf(false) }
    val animateState by animateFloatAsState(targetValue = if (isShowSelector) 90f else 0f)

    val options = listOf(
        OptionData(
            label = stringResource(Res.string.label_absent),
            value = PermitType.ABSENCE
        ),
        OptionData(
            label = stringResource(Res.string.label_dispensation),
            value = PermitType.DISPENSATION
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = Style.popupTitle,
            color = theme.bodyText
        )
        Row(
            modifier = Modifier
                .clickable {
                    isShowSelector = !isShowSelector
                }
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .rotate(animateState),
                painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                contentDescription = null,
                tint = theme.textPrimary
            )
            Text(
                text = formData.type.value,
                style = Style.title,
                color = theme.textPrimary
            )
        }
    }

    SingleSelector(
        isShowSelector = isShowSelector,
        onDismissRequest = { isShowSelector = it },
        title = stringResource(Res.string.label_permit_type),
        value = formData.type,
        options = options,
        onValueChange = {
            onUpdateFormData(
                formData.copy(
                    type = it
                )
            )
        }
    )
}

@Composable
fun PermitLoadingContent(
    isEdit: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val title = if (isEdit) stringResource(Res.string.label_edit_permit_request) else  stringResource(Res.string.label_create_permit_request)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = Style.popupTitle,
                color = theme.bodyText
            )
            ShimmerEffect(80.dp)
        }
        listOf(
            stringResource(Res.string.label_duration),
            "Attachment",
            "Note"
        ).forEach {
            TextFieldTitle(
                title = it
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(TextFieldDefaults.height)
                )
            }
        }
        Button(
            text = stringResource(Res.string.label_submit),
            modifier = Modifier.fillMaxWidth()
        ) {
        }
    }
}