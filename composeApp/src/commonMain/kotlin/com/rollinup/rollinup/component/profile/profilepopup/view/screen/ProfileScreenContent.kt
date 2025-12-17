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
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_calendar_fill_24
import rollin_up.composeapp.generated.resources.ic_female_line_24
import rollin_up.composeapp.generated.resources.ic_home_line_24
import rollin_up.composeapp.generated.resources.ic_id_card_line_24
import rollin_up.composeapp.generated.resources.ic_mail_user_line_24
import rollin_up.composeapp.generated.resources.ic_male_line_24
import rollin_up.composeapp.generated.resources.ic_phone_line_24
import rollin_up.composeapp.generated.resources.ic_user_board_line_24
import rollin_up.composeapp.generated.resources.ic_user_line_24

@Composable
fun ProfileScreenContent(
    uiState: ProfileDialogUiState,
    cb: ProfileCallback,
    onShowSnackBar: OnShowSnackBar,
) {
    HandleState(
        state = uiState.editState,
        successMsg = "Success, user data successfully edited.",
        errorMsg = "Error, failed to edit user data, please try again.",
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
                        text = "Submit"
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
            val text = if (uiState.isEdit) "Cancel" else "Edit"
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
            title = "Full Name",
            icon = Res.drawable.ic_user_line_24,
            value = userDetail.fullName
        )
        ProfileInfoField(
            title = "ID",
            icon = Res.drawable.ic_id_card_line_24,
            value = userDetail.id
        )
        ProfileInfoField(
            title = "Class",
            icon = Res.drawable.ic_user_board_line_24,
            value = userDetail.classX?.name ?: "-"
        )
        ProfileInfoField(
            title = "Role",
            icon = Res.drawable.ic_user_board_line_24,
            value = userDetail.role.name
        )
        ProfileInfoField(
            title = "Email",
            icon = Res.drawable.ic_mail_user_line_24,
            value = userDetail.email
        )
        ProfileInfoField(
            title = "Birthday",
            icon = Res.drawable.ic_calendar_fill_24,
            value = userDetail.birthDay
        )
        ProfileInfoField(
            title = "Gender",
            icon = genderIcon,
            value = userDetail.gender.label
        )
        ProfileInfoField(
            title = "Phone",
            icon = Res.drawable.ic_phone_line_24,
            value = userDetail.phoneNumber
        )
        ProfileInfoField(
            title = "Address",
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                title = "First name",
                value = formData.firstName ?: "",
                maxChar = 15,
                onValueChange = { value ->
                    val errorMsg = if (value.length > 15) {
                        "First name can't be more than 15 characters"
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
                placeholder = "Enter first name",
            )
        }
        Spacer(itemGap8)
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                title = "Last name",
                value = formData.lastName ?: "",
                maxChar = 15,
                onValueChange = { value ->
                    val errorMsg = if (value.length > 15) {
                        "Last name can't be more than 15 characters"
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
                placeholder = "Enter last name",
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
        title = "Email",
        icon = Res.drawable.ic_mail_user_line_24,
        value = uiState.userDetail.email,
        enabled = false
    )

//    TODO: Make Email editable for next sprint
//    TextField(
//        title = "Email",
//        value = formData.email ?: "",
//        maxChar = 15,
//        onValueChange = { value ->
//            val errorMsg = if (value.length > 30) {
//                "Email can't be more than 30 characters"
//            } else {
//                null
//            }
//            onUpdateFormData(
//                formData.copy(
//                    email = value,
//                    emailError = errorMsg
//                )
//            )
//        },
//        isError = formData.emailError != null,
//        errorMsg = formData.email,
//        placeholder = "Enter email",
//    )
    Row {
        Box(modifier = Modifier.weight(1f)) {
            SingleDatePickerField(
                title = "Birthday",
                placeholder = "Select birthday",
                value = formData.birthDay,
                isError = formData.birthDayError,
                isAllSelectable = true,
                errorText = "Birthday can't be empty",
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
                title = "Gender",
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

    PhoneNumberTextField(
        value = formData.phone ?: "",
        onValueChange = { value ->
            val errorMsg = if (value.length > 16) {
                "Phone number can't be more than 16 characters"
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
        placeholder = "Enter phone number",
        title = "Phone",
        isError = formData.phoneError != null,
        errorMsg = formData.phoneError,
    )
    TextField(
        title = "Address",
        value = formData.address ?: "",
        maxChar = 15,
        onValueChange = { value ->
            val errorMsg = if (value.length > 120) {
                "Address can't be more than 30 characters"
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
        placeholder = "Enter address",
    )
}

@Composable
private fun EditProfileFormDisabledSection(
    userDetail: UserDetailEntity,
) {
    ProfileInfoField(
        enabled = false,
        title = "ID",
        icon = Res.drawable.ic_id_card_line_24,
        value = userDetail.studentId.ifBlank { "-" }
    )
    ProfileInfoField(
        enabled = false,
        title = "Class",
        icon = Res.drawable.ic_user_board_line_24,
        value = userDetail.classX?.name ?: "-"
    )
    ProfileInfoField(
        enabled = false,
        title = "Role",
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