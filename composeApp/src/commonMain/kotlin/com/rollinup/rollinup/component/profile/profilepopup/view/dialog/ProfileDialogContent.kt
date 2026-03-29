package com.rollinup.rollinup.component.profile.profilepopup.view.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.getLabel
import com.rollinup.rollinup.component.profile.ProfileInfoField
import com.rollinup.rollinup.component.profile.profilepopup.model.EditProfileFormData
import com.rollinup.rollinup.component.profile.profilepopup.model.ProfileCallback
import com.rollinup.rollinup.component.profile.profilepopup.uistate.ProfileDialogUiState
import com.rollinup.rollinup.component.selector.SingleDropDownSelector
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.PhoneNumberTextField
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.stringResource
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
import rollin_up.composeapp.generated.resources.label_address
import rollin_up.composeapp.generated.resources.label_birthday
import rollin_up.composeapp.generated.resources.label_class
import rollin_up.composeapp.generated.resources.label_edit
import rollin_up.composeapp.generated.resources.label_email
import rollin_up.composeapp.generated.resources.label_full_name
import rollin_up.composeapp.generated.resources.label_gender
import rollin_up.composeapp.generated.resources.label_id
import rollin_up.composeapp.generated.resources.label_phone
import rollin_up.composeapp.generated.resources.label_role
import rollin_up.composeapp.generated.resources.msg_error_max_char
import rollin_up.composeapp.generated.resources.ph_address
import rollin_up.composeapp.generated.resources.ph_email
import rollin_up.composeapp.generated.resources.ph_full_name
import rollin_up.composeapp.generated.resources.ph_phone
import rollin_up.composeapp.generated.resources.ph_select_birthday

@Composable
fun ProfileDialogContent(
    uiState: ProfileDialogUiState,
    cb: ProfileCallback,
) {
    Column {
        if (uiState.isLoading) {
            ProfileLoading()
        } else {
            ProfileDialogHeader(
                userDetail = uiState.userDetail,
                showEdit = uiState.showEdit,
                onToggleEdit = cb.onToggleEdit
            )
            Spacer(24.dp)
            if (!uiState.isEdit) {
                ProfileInfoSection(uiState.userDetail)
            } else {
                ProfileEditForm(
                    uiState = uiState,
                    onUpdateFormData = cb.onUpdateForm
                )
                Spacer(itemGap8)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    cb.onSubmit(uiState.formData)
                }
            }
        }
    }
}

@Composable
private fun ProfileDialogHeader(
    userDetail: UserDetailEntity,
    showEdit: Boolean,
    onToggleEdit: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(color = theme.primary, shape = CircleShape)
                .size(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userDetail.fullName.take(1),
                style = Style.title,
                color = theme.textBtnPrimary
            )
        }
        Spacer(itemGap8)
        Column {
            Text(
                text = userDetail.fullName,
                style = Style.title,
                color = theme.bodyText
            )
            Spacer(itemGap4)
            Text(
                text = userDetail.userName,
                style = Style.body,
                color = theme.bodyText
            )
        }
        if (showEdit) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(Res.string.label_edit),
                modifier = Modifier.clickable {
                    onToggleEdit()
                },
                color = theme.primary,
                style = Style.title
            )
        }
    }
}


@Composable
private fun ProfileEditForm(
    uiState: ProfileDialogUiState,
    onUpdateFormData: (EditProfileFormData) -> Unit,
) {
    val formData = uiState.formData
    val userDetail = uiState.userDetail

    FlowRow(
        itemVerticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        maxItemsInEachRow = 2
    ) {
        val fullName = stringResource(Res.string.label_full_name)
        val fullNameError = stringResource(Res.string.msg_error_max_char, fullName, 60)

        Box(modifier = Modifier.weight(1f)) {
            TextField(
                title = fullName,
                value = formData.fullName ?: "",
                maxChar = 60,
                onValueChange = { value ->
                    val errorMsg = if (value.length > 60) {
                        fullNameError
                    } else {
                        null
                    }
                    onUpdateFormData(
                        formData.copy(
                            fullName = value,
                            fullNameError = errorMsg
                        )
                    )
                },
                isError = formData.fullNameError != null,
                errorMsg = formData.fullName,
                placeholder = stringResource(Res.string.ph_full_name),
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            SingleDatePickerField(
                title = stringResource(Res.string.label_birthday),
                value = formData.birthDay,
                enable = true,
                placeHolder = stringResource(Res.string.ph_select_birthday),
                width = null,
                onValueChange = {
                    onUpdateFormData(
                        formData.copy(
                            birthDay = it
                        )
                    )
                },
                isError = formData.birthDayError
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                enabled = false,
                title = stringResource(Res.string.label_id),
                icon = Res.drawable.ic_id_card_line_24,
                value = userDetail.id
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            SingleDropDownSelector(
                title = stringResource(Res.string.label_gender),
                value = formData.gender,
                options = uiState.genderOptions,
                onValueChange = {
                    onUpdateFormData(
                        formData.copy(
                            gender = it
                        )
                    )
                },
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                enabled = false,
                title = stringResource(Res.string.label_class),
                icon = Res.drawable.ic_user_board_line_24,
                value = userDetail.classX?.name ?: "-"
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            val phoneNumberLabel = stringResource(Res.string.label_phone)
            val phoneError = stringResource(Res.string.msg_error_max_char, phoneNumberLabel, 16)
            PhoneNumberTextField(
                value = formData.phone ?: "",
                onValueChange = { value ->
                    val errorMsg = if (value.length > 16) {
                        phoneError
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
                placeholder = stringResource(Res.string.ph_phone),
                title = phoneNumberLabel,
                isError = formData.phoneError != null,
                errorMsg = formData.phoneError,
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                enabled = false,
                title = stringResource(Res.string.label_role),
                icon = Res.drawable.ic_user_board_line_24,
                value = userDetail.role.name
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            val addressLabel = stringResource(Res.string.label_address)
            val addressError = stringResource(Res.string.msg_error_max_char, addressLabel, 120)
            TextField(
                title = stringResource(Res.string.label_address),
                value = formData.address ?: "",
                maxChar = 15,
                onValueChange = { value ->
                    val errorMsg = if (value.length > 120) {
                        addressError
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
                placeholder = stringResource(Res.string.ph_address),
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            val emailLabel = stringResource(Res.string.label_email)
            val emailError = stringResource(Res.string.msg_error_max_char, emailLabel, 30)

            TextField(
                title = emailLabel,
                value = formData.email ?: "",
                maxChar = 15,
                onValueChange = { value ->
                    val errorMsg = if (value.length > 30) {
                        emailError
                    } else {
                        null
                    }
                    onUpdateFormData(
                        formData.copy(
                            email = value,
                            emailError = errorMsg
                        )
                    )
                },
                isError = formData.emailError != null,
                errorMsg = formData.email,
                placeholder = stringResource(Res.string.ph_email),
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
    FlowRow(
        itemVerticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        maxItemsInEachRow = 2
    ) {
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = stringResource(Res.string.label_full_name),
                icon = Res.drawable.ic_user_line_24,
                value = userDetail.fullName
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = stringResource(Res.string.label_birthday),
                icon = Res.drawable.ic_calendar_fill_24,
                value = userDetail.birthDay
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = stringResource(Res.string.label_id),
                icon = Res.drawable.ic_id_card_line_24,
                value = userDetail.id
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = stringResource(Res.string.label_gender),
                icon = genderIcon,
                value = userDetail.gender.getLabel()
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = stringResource(Res.string.label_class),
                icon = Res.drawable.ic_user_board_line_24,
                value = userDetail.classX?.name ?: "-"
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = stringResource(Res.string.label_phone),
                icon = Res.drawable.ic_phone_line_24,
                value = userDetail.phoneNumber
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = stringResource(Res.string.label_role),
                icon = Res.drawable.ic_user_board_line_24,
                value = userDetail.role.name
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = stringResource(Res.string.label_address),
                icon = Res.drawable.ic_home_line_24,
                value = userDetail.address
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = stringResource(Res.string.label_email),
                icon = Res.drawable.ic_mail_user_line_24,
                value = userDetail.email
            )
        }
    }
}

@Composable
private fun ProfileLoading() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            ShimmerEffect(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(44.dp),
            )
            Spacer(itemGap8)
            Column {
                ShimmerEffect(120.dp)
                Spacer(itemGap4)
                ShimmerEffect(80.dp)
            }
        }
        Spacer(24.dp)
        FlowRow(
            itemVerticalAlignment = Alignment.CenterVertically,
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(9) {
                ShimmerEffect(
                    modifier = Modifier
                        .height(24.dp)
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}