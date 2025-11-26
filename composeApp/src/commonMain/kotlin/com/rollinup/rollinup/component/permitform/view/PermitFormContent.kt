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
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.date.DatePickerField
import com.rollinup.rollinup.component.filepicker.FilePicker
import com.rollinup.rollinup.component.loading.LoadingOverlay
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.OptionData
import com.rollinup.rollinup.component.permitform.model.PermitFormCallback
import com.rollinup.rollinup.component.permitform.model.PermitFormData
import com.rollinup.rollinup.component.permitform.uistate.PermitFormUiState
import com.rollinup.rollinup.component.radio.RadioSelectorRow
import com.rollinup.rollinup.component.selector.SingleSelector
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.textfield.TextFieldDefaults
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.time.TimeDurationTextField
import com.rollinup.rollinup.component.utils.getPlatform
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_right_24

@Composable
fun PermitFormContent(
    formData: PermitFormData,
    onUpdateFormData: (PermitFormData) -> Unit,
) {
    PermitFormDurationSection(
        formData = formData,
        onUpdateFormData = onUpdateFormData
    )
    if (formData.type == PermitType.ABSENT) {
        PermitReasonSection(
            onUpdateFormData = onUpdateFormData,
            formData = formData
        )
    }
    FilePicker(
        title = "Attachment",
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
                textError = "File size must be less than 1MB"
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
        title = "Note",
        value = formData.note ?: "",
        onValueChange = {
            onUpdateFormData(
                formData.copy(
                    note = it,
                    noteError = null
                )
            )
        },
        placeholder = "Enter permit note",
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
) {
    val formData = uiState.formData
    val isEdit = uiState.isEdit

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

    LoadingOverlay(uiState.isLoadingOverlay)

    val title = if (isEdit) "Edit Permit Request" else "Create Permit Request"

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
        title = "Reason",
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
                placeholder = "Enter permit reason",
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
        PermitType.ABSENT -> {
            @Suppress("UNCHECKED_CAST")
            DatePickerField(
                title = "Duration",
                placeholder = "Select permit duration",
                value = formData.duration.filter { it != null } as List<Long>,
                platform = getPlatform(),
                onValueChange = {
                    onUpdateFormData(
                        formData.copy(
                            duration = it,
                            durationError = null
                        )
                    )
                },
                isError = formData.durationError != null,
                errorText = formData.durationError
            )
        }

        PermitType.DISPENSATION -> {
            TimeDurationTextField(
                value = formData.duration,
                onValueChange = {
                    onUpdateFormData(
                        formData.copy(
                            duration = it,
                            durationError = null
                        )
                    )
                },
                isRequired = true,
                title = "Duration"
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
            label = "Absent",
            value = PermitType.ABSENT
        ),
        OptionData(
            label = "Dispensation",
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
        title = "Permit Type",
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
        val title = if (isEdit) "Edit Permit Request " else "Create Permit Request"
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
            "Duration",
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
            text = "Submit",
            modifier = Modifier.fillMaxWidth()
        ) {
        }

    }
}