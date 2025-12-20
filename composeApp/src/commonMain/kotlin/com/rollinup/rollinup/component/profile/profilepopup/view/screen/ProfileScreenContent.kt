package com.rollinup.rollinup.component.profile.profilepopup.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.date.SingleDatePickerField
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.loading.LoadingOverlay
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.profile.ProfileInfoField
import com.rollinup.rollinup.component.profile.profilepopup.model.EditProfileFormData
import com.rollinup.rollinup.component.profile.profilepopup.model.ProfileCallback
import com.rollinup.rollinup.component.profile.profilepopup.uistate.ProfileDialogUiState
import com.rollinup.rollinup.component.selector.SingleSelectorField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.textfield.PhoneNumberTextField
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.Res.string
import rollin_up.composeapp.generated.resources.ic_calendar_fill_24
import rollin_up.composeapp.generated.resources.ic_female_line_24
import rollin_up.composeapp.generated.resources.ic_home_line_24
import rollin_up.composeapp.generated.resources.ic_id_card_line_24
import rollin_up.composeapp.generated.resources.ic_mail_user_line_24
import rollin_up.composeapp.generated.resources.ic_male_line_24
import rollin_up.composeapp.generated.resources.ic_phone_line_24
import rollin_up.composeapp.generated.resources.ic_user_board_line_24
import rollin_up.composeapp.generated.resources.ic_user_line_24
import rollin_up.composeapp.generated.resources.label_address
import rollin_up.composeapp.generated.resources.label_birthday
import rollin_up.composeapp.generated.resources.label_cancel
import rollin_up.composeapp.generated.resources.label_class
import rollin_up.composeapp.generated.resources.label_edit
import rollin_up.composeapp.generated.resources.label_email
import rollin_up.composeapp.generated.resources.label_first_name
import rollin_up.composeapp.generated.resources.label_full_name
import rollin_up.composeapp.generated.resources.label_gender
import rollin_up.composeapp.generated.resources.label_id
import rollin_up.composeapp.generated.resources.label_last_name
import rollin_up.composeapp.generated.resources.label_phone
import rollin_up.composeapp.generated.resources.label_role
import rollin_up.composeapp.generated.resources.label_student_id
import rollin_up.composeapp.generated.resources.label_submit
import rollin_up.composeapp.generated.resources.msg_address_error_max
import rollin_up.composeapp.generated.resources.msg_birthday_error_empty
import rollin_up.composeapp.generated.resources.msg_first_name_max_error
import rollin_up.composeapp.generated.resources.msg_last_name_max_error
import rollin_up.composeapp.generated.resources.msg_phone_error_max
import rollin_up.composeapp.generated.resources.msg_user_edit_error
import rollin_up.composeapp.generated.resources.msg_user_edit_success
import rollin_up.composeapp.generated.resources.ph_address
import rollin_up.composeapp.generated.resources.ph_first_name
import rollin_up.composeapp.generated.resources.ph_last_name
import rollin_up.composeapp.generated.resources.ph_phone
import rollin_up.composeapp.generated.resources.ph_select_birthday

@Composable
fun ProfileScreenContent(
    uiState: ProfileDialogUiState,
    cb: ProfileCallback,
    onShowSnackBar: OnShowSnackBar,
) {
    HandleState(
        state = uiState.editState,
        successMsg = stringResource(string.msg_user_edit_success),
        errorMsg = stringResource(string.msg_user_edit_error),
        onDispose = { cb.onResetMessageState() },
        onShowSnackBar = onShowSnackBar,
        onSuccess = {
            cb.onToggleEdit()
            cb.onRefresh()
        }
    )
    LoadingOverlay(uiState.isLoadingOverlay)
    Scaffold {
        if (uiState.isLoading) {
            ProfileContentLoading()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(screenPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    ProfileHeaderSection(
                        uiState = uiState,
                        onToggleEdit = cb.onToggleEdit
                    )
                    Spacer(12.dp)
                    if (uiState.isEdit) {
                        EditProfileForm(
                            uiState = uiState,
                            onUpdateFormData = cb.onUpdateForm
                        )
                    } else {
                        ProfileInfoSection(uiState.userDetail)
                    }
                }

                if (uiState.isEdit) {
                    Spacer(itemGap8)
                    Button(
                        text = stringResource(string.label_submit)
                    ) {
                        cb.onSubmit(uiState.formData)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeaderSection(
    uiState: ProfileDialogUiState,
    onToggleEdit: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(itemGap4)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(color = theme.primary, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = uiState.userDetail.fullName.take(1),
                style = Style.title,
                color = theme.textBtnPrimary
            )
        }
        Text(
            text = uiState.userDetail.fullName,
            style = Style.popupTitle,
            color = theme.textPrimary,
        )
        Text(
            text = uiState.userDetail.userName,
            style = Style.body,
            color = theme.bodyText,
        )
        if (uiState.showEdit) {
            val text =
                if (uiState.isEdit) stringResource(string.label_cancel) else stringResource(string.label_edit)
            Text(
                text = text,
                style = Style.title,
                color = theme.textPrimary,
                modifier = Modifier.clickable {
                    onToggleEdit()
                }
            )
        }
    }
}

@Composable
private fun ProfileInfoSection(
    userDetail: UserDetailEntity,
) {
    val genderIcon = when (userDetail.gender) {
        Gender.MALE -> Res.drawable.ic_male_line_24
        Gender.FEMALE -> Res.drawable.ic_female_line_24
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileInfoField(
            title = stringResource(string.label_full_name),
            icon = Res.drawable.ic_user_line_24,
            value = userDetail.fullName
        )
        ProfileInfoField(
            title = stringResource(string.label_student_id),
            icon = Res.drawable.ic_id_card_line_24,
            value = userDetail.id
        )
        ProfileInfoField(
            title = stringResource(string.label_class),
            icon = Res.drawable.ic_user_board_line_24,
            value = userDetail.classX?.name ?: "-"
        )
        ProfileInfoField(
            title = stringResource(string.label_role),
            icon = Res.drawable.ic_user_board_line_24,
            value = userDetail.role.name
        )
        ProfileInfoField(
            title = stringResource(string.label_email),
            icon = Res.drawable.ic_mail_user_line_24,
            value = userDetail.email
        )
        ProfileInfoField(
            title = stringResource(string.label_birthday),
            icon = Res.drawable.ic_calendar_fill_24,
            value = userDetail.birthDay
        )
        ProfileInfoField(
            title = stringResource(string.label_gender),
            icon = genderIcon,
            value = userDetail.gender.label
        )
        ProfileInfoField(
            title = stringResource(string.label_phone),
            icon = Res.drawable.ic_phone_line_24,
            value = userDetail.phoneNumber
        )
        ProfileInfoField(
            title = stringResource(string.label_address),
            icon = Res.drawable.ic_home_line_24,
            value = userDetail.address
        )
    }
}

@Composable
private fun EditProfileForm(
    uiState: ProfileDialogUiState,
    onUpdateFormData: (EditProfileFormData) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(itemGap8),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditProfileFormNameSection(
            formData = uiState.formData,
            onUpdateFormData = onUpdateFormData
        )
        EditProfileFormDisabledSection(uiState.userDetail)
        EditProfileFormAdditionalSection(
            uiState = uiState,
            onUpdateFormData = onUpdateFormData
        )
    }
}

@Composable
private fun EditProfileFormNameSection(
    formData: EditProfileFormData,
    onUpdateFormData: (EditProfileFormData) -> Unit,
) {
    val firstNameErrorMsg = stringResource(string.msg_first_name_max_error)
    val lastNameErrorMsg = stringResource(string.msg_last_name_max_error)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                title = stringResource(string.label_first_name),
                value = formData.firstName ?: "",
                maxChar = 15,
                onValueChange = { value ->
                    val errorMsg = if (value.length > 15) {
                        firstNameErrorMsg
                    } else {
                        null
                    }
                    onUpdateFormData(
                        formData.copy(
                            firstName = value,
                            firstNameError = errorMsg
                        )
                    )
                },
                isError = formData.firstNameError != null,
                errorMsg = formData.firstNameError,
                placeholder = stringResource(string.ph_first_name),
            )
        }
        Spacer(itemGap8)
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                title = stringResource(string.label_last_name),
                value = formData.lastName ?: "",
                maxChar = 15,
                onValueChange = { value ->
                    val errorMsg = if (value.length > 15) {
                        lastNameErrorMsg
                    } else {
                        null
                    }
                    onUpdateFormData(
                        formData.copy(
                            lastName = value,
                            lastNameError = errorMsg
                        )
                    )
                },
                isError = formData.lastNameError != null,
                errorMsg = formData.lastName,
                placeholder = stringResource(string.ph_last_name),
            )
        }
    }
}

@Composable
private fun EditProfileFormAdditionalSection(
    uiState: ProfileDialogUiState,
    onUpdateFormData: (EditProfileFormData) -> Unit,
) {
    val formData = uiState.formData

    ProfileInfoField(
        title = stringResource(string.label_email),
        icon = Res.drawable.ic_mail_user_line_24,
        value = uiState.userDetail.email,
        enabled = false
    )

//    TODO: Make Email editable for next sprint

    Row {
        Box(modifier = Modifier.weight(1f)) {
            SingleDatePickerField(
                title = stringResource(string.label_birthday),
                placeholder = stringResource(string.ph_select_birthday),
                value = formData.birthDay,
                isError = formData.birthDayError,
                isAllSelectable = true,
                errorText = stringResource(string.msg_birthday_error_empty),
                enabled = true,
                isDisablePastSelection = false,
                onValueChange = {
                    onUpdateFormData(
                        formData.copy(
                            birthDay = it,
                            birthDayError = false
                        )
                    )
                }
            )

        }
        Spacer(itemGap8)
        Box(modifier = Modifier.weight(1f)) {
            SingleSelectorField(
                title = stringResource(string.label_gender),
                value = formData.gender,
                options = uiState.genderOptions,
                onValueChange = {
                    onUpdateFormData(
                        formData.copy(
                            gender = it
                        )
                    )
                }
            )
        }

    }

    val phoneErrorMessage = stringResource(string.msg_phone_error_max)
    val addressErrorMessage = stringResource(string.msg_address_error_max)
    PhoneNumberTextField(
        value = formData.phone ?: "",
        onValueChange = { value ->
            val errorMsg = if (value.length > 16) {
                phoneErrorMessage
            } else {
                null
            }
            onUpdateFormData(
                formData.copy(
                    phone = value,
                    phoneError = errorMsg
                )
            )
        },
        placeholder = stringResource(string.ph_phone),
        title = stringResource(string.label_phone),
        isError = formData.phoneError != null,
        errorMsg = formData.phoneError,
    )
    TextField(
        title = stringResource(string.label_address),
        value = formData.address ?: "",
        maxChar = 15,
        onValueChange = { value ->
            val errorMsg = if (value.length > 120) {
                addressErrorMessage
            } else {
                null
            }
            onUpdateFormData(
                formData.copy(
                    address = value,
                    addressError = errorMsg
                )
            )
        },
        isError = formData.addressError != null,
        errorMsg = formData.address,
        placeholder = stringResource(string.ph_address),
    )
}

@Composable
private fun EditProfileFormDisabledSection(
    userDetail: UserDetailEntity,
) {
    ProfileInfoField(
        enabled = false,
        title = stringResource(string.label_id),
        icon = Res.drawable.ic_id_card_line_24,
        value = userDetail.studentId.ifBlank { "-" }
    )
    ProfileInfoField(
        enabled = false,
        title = stringResource(string.label_class),
        icon = Res.drawable.ic_user_board_line_24,
        value = userDetail.classX?.name ?: "-"
    )
    ProfileInfoField(
        enabled = false,
        title = stringResource(string.label_role),
        icon = Res.drawable.ic_user_board_line_24,
        value = userDetail.role.name
    )
}

@Composable
private fun ProfileContentLoading() {
    Column(
        modifier = Modifier.padding(screenPadding),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )
            ShimmerEffect(120.dp)
            ShimmerEffect(80.dp)
        }
        Spacer(12.dp)
        Column {
            repeat(9) {
                ShimmerEffect(
                    modifier = Modifier
                        .height(24.dp)
                        .fillMaxWidth()
                )
                Spacer(12.dp)
            }
        }
    }

}