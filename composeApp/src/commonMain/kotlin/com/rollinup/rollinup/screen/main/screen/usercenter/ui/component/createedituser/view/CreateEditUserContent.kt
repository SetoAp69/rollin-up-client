package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.apiservice.model.common.Role
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.checkbox.CheckBox
import com.rollinup.rollinup.component.date.SingleDatePickerField
import com.rollinup.rollinup.component.loading.LoadingOverlay
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.model.getLabel
import com.rollinup.rollinup.component.selector.SingleDropDownSelector
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.PhoneNumberTextField
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserCallback
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormData
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormErrorType
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormOption
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.uistate.CreateEditUserUiState
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.viewmodel.CreateEditUserViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_address
import rollin_up.composeapp.generated.resources.label_admin
import rollin_up.composeapp.generated.resources.label_birthday
import rollin_up.composeapp.generated.resources.label_class
import rollin_up.composeapp.generated.resources.label_email
import rollin_up.composeapp.generated.resources.label_full_name
import rollin_up.composeapp.generated.resources.label_gender
import rollin_up.composeapp.generated.resources.label_phone
import rollin_up.composeapp.generated.resources.label_role
import rollin_up.composeapp.generated.resources.label_student_id
import rollin_up.composeapp.generated.resources.label_username
import rollin_up.composeapp.generated.resources.msg_stay_in_form
import rollin_up.composeapp.generated.resources.ph_address
import rollin_up.composeapp.generated.resources.ph_email
import rollin_up.composeapp.generated.resources.ph_full_Name
import rollin_up.composeapp.generated.resources.ph_phone
import rollin_up.composeapp.generated.resources.ph_student_id
import rollin_up.composeapp.generated.resources.ph_username

@Composable
fun CreateEditUserContent(
    showDialog: Boolean,
    id: String?,
    onDismissRequest: (Boolean) -> Unit,
    onShowSnackBar: OnShowSnackBar,
    onSuccess: () -> Unit,
) {
    val viewModel: CreateEditUserViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()

    DisposableEffect(Unit) {
        if (showDialog) {
            viewModel.init(id)
        }
        onDispose {
            viewModel.resetUiState()
        }
    }
    CreateEditUserStateHandler(
        uiState = uiState,
        onResetMessageState = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar,
        onDismissRequest = onDismissRequest,
        onResetForm = cb.onResetForm,
        onSuccess = onSuccess
    )
    CreateEditUserForm(
        uiState = uiState,
        cb = cb
    )
    LoadingOverlay(uiState.isLoadingOverlay)
}

@Composable
fun CreateEditUserForm(
    uiState: CreateEditUserUiState,
    cb: CreateEditUserCallback,
) {
    Column {
        if (!uiState.isLoading) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserNameSection(
                    isEdit = uiState.isEdit,
                    formData = uiState.formData,
                    onUpdateForm = cb.onUpdateForm,
                    onCheckUserName = cb.onCheckUserName,
                    initialUsername = uiState.initialDetail.userName
                )
                NameSection(
                    isEdit = uiState.isEdit,
                    formData = uiState.formData,
                    onUpdateForm = cb.onUpdateForm
                )
                SelectorSection(
                    options = uiState.formOptions,
                    formData = uiState.formData,
                    onUpdateForm = cb.onUpdateForm,
                    isLoading = uiState.isLoadingOptions
                )
                AdditionalInfoSection(
                    uiState = uiState,
                    onUpdateForm = cb.onUpdateForm,
                    onCheckEmail = cb.onCheckEmail,
                )
            }
        } else {
            CreateEditUserFormLoading()
        }
        UserFormFooter(
            uiState = uiState,
            cb = cb
        )
    }

}

@Composable
private fun UserFormFooter(
    uiState: CreateEditUserUiState,
    cb: CreateEditUserCallback,
) {
    Column(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        if (!uiState.isEdit) {
            Row(
                modifier = Modifier.clickable { cb.onToggleStay(!uiState.isStay) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckBox(
                    checked = uiState.isStay,
                    onCheckedChange = cb.onToggleStay
                )
                Text(
                    text = stringResource(Res.string.msg_stay_in_form),
                    style = Style.body,
                    color = theme.bodyText
                )
            }
        }
        Spacer(itemGap4)
        Button(
            text = "Submit",
            modifier = Modifier.fillMaxWidth()
        ) {
            cb.onSubmit(uiState.formData, uiState.isEdit)
        }
    }
}

@Composable
fun UserNameSection(
    isEdit: Boolean,
    initialUsername: String,
    formData: CreateEditUserFormData,
    onUpdateForm: (CreateEditUserFormData) -> Unit,
    onCheckUserName: (String) -> Unit,
) {
    TextField(
        isRequired = !isEdit,
        modifier = Modifier.onFocusChanged { focusState ->
            if (!focusState.isFocused && !formData.userName.isNullOrBlank() && formData.userName != initialUsername) {
                onCheckUserName(formData.userName)
            }
        },
        onValueChange = { newValue ->
            val errorMessage = if (newValue.contains(" ")) {
                CreateEditUserFormErrorType.USERNAME_INVALID
            } else {
                null
            }

            onUpdateForm(
                formData.copy(
                    userName = newValue.ifBlank { null },
                    userNameError = errorMessage
                )
            )
        },
        value = formData.userName ?: "",
        placeholder = stringResource(Res.string.ph_username),
        title = stringResource(Res.string.label_username),
        isError = formData.userNameError != null,
        errorMsg = formData.userNameError?.getErrorMessage()
    )
}

@Composable
fun NameSection(
    isEdit: Boolean,
    formData: CreateEditUserFormData,
    onUpdateForm: (CreateEditUserFormData) -> Unit,
) {
    Row {
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                isRequired = !isEdit,
                title = stringResource(Res.string.label_full_name),
                maxChar = 60,
                value = formData.fullName ?: "",
                placeholder = stringResource(Res.string.ph_full_Name),
                onValueChange = { value ->
                    onUpdateForm(
                        formData.copy(
                            fullName = value.ifBlank { null },
                            lastNameError = null
                        )
                    )
                },
                isError = formData.firstNameError != null,
                errorMsg = formData.firstNameError?.getErrorMessage()
            )
        }
    }
}

@Composable
private fun SelectorSection(
    isLoading: Boolean,
    options: CreateEditUserFormOption,
    formData: CreateEditUserFormData,
    onUpdateForm: (CreateEditUserFormData) -> Unit,
) {
    val roleOptions = mapRoleOptionsString(options.role)
    val adminRole =
        options.role.find { it.label.equals(stringResource(Res.string.label_admin), true) }
    val isAdmin = formData.role == adminRole?.value

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        itemVerticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(itemGap8),
        verticalArrangement = Arrangement.spacedBy(itemGap8)
    ) {
        SingleDropDownSelector(
            title = stringResource(Res.string.label_gender),
            value = formData.gender,
            width = 120.dp,
            isError = formData.genderError,
            options = options.gender,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        gender = it,
                        genderError = false
                    )
                )
            },
        )
        SingleDatePickerField(
            title = stringResource(Res.string.label_birthday),
            value = formData.birthDay,
            width = 120.dp,
            isError = formData.birthDayError,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        birthDay = it,
                        birthDayError = false
                    )
                )
            },
            isAllSelectable = true,
        )
        SingleDropDownSelector(
            title = stringResource(Res.string.label_role),
            value = formData.role,
            isError = formData.roleError,
            isLoading = isLoading,
            width = 120.dp,
            options = roleOptions,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        role = it,
                        roleError = false
                    )
                )
            },
        )

        if (!isAdmin) {
            LaunchedEffect(Unit) {
                onUpdateForm(
                    formData.copy(
                        classId = null,
                        classError = false
                    )
                )
            }
            SingleDropDownSelector(
                title = stringResource(Res.string.label_class),
                value = formData.classId,
                isError = formData.classError,
                isLoading = isLoading,
                options = options.classX,
                width = 120.dp,
                onValueChange = {
                    onUpdateForm(
                        formData.copy(
                            classId = it,
                            classError = false
                        )
                    )
                },
            )
        }
    }
}


@Composable
private fun AdditionalInfoSection(
    uiState: CreateEditUserUiState,
    onUpdateForm: (CreateEditUserFormData) -> Unit,
    onCheckEmail: (String) -> Unit,
) {
    val formData = uiState.formData
    val initialDetail = uiState.initialDetail
    val studentRole = uiState.formOptions.role.find {
        it.label.equals("Student", true)
    }
    val isStudent = formData.role == studentRole?.value

    if (isStudent) {
        TextField(
            isRequired = !uiState.isEdit,
            onValueChange = { newValue ->
                val errorMessage = if (newValue.contains(" ")) {
                    CreateEditUserFormErrorType.STUDENT_ID_INVALID
                } else {
                    null
                }

                onUpdateForm(
                    formData.copy(
                        studentId = newValue.ifBlank { null },
                        studentIdError = errorMessage
                    )
                )
            },
            value = formData.studentId ?: "",
            placeholder = stringResource(Res.string.ph_student_id),
            title = stringResource(Res.string.label_student_id),
            isError = formData.studentIdError != null,
            errorMsg = formData.studentIdError?.getErrorMessage()
        )
    }

    TextField(
        title = stringResource(Res.string.label_address),
        maxChar = 120,
        value = formData.address ?: "",
        placeholder = stringResource(Res.string.ph_address),
        onValueChange = {
            onUpdateForm(
                formData.copy(
                    address = it.ifBlank { null },
                    addressError = null
                )
            )
        },
        isError = formData.addressError != null,
        errorMsg = formData.addressError?.getErrorMessage(),
    )
    PhoneNumberTextField(
        title = stringResource(Res.string.label_phone),
        value = formData.phone ?: "",
        maxChar = 16,
        onValueChange = {
            onUpdateForm(
                formData.copy(
                    phone = it.ifBlank { null },
                    phoneError = null
                )
            )
        },
        placeholder = stringResource(Res.string.ph_phone),
        isError = formData.phoneError != null,
        errorMsg = formData.phoneError?.getErrorMessage()
    )
    TextField(
        title = stringResource(Res.string.label_email),
        isRequired = true,
        value = formData.email ?: "",
        maxChar = 30,
        onValueChange = { newValue ->
            val errorMsg =
                if (newValue.isBlank() || newValue.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex())) {
                    null
                } else {
                    CreateEditUserFormErrorType.EMAIL_INVALID
                }
            onUpdateForm(
                formData.copy(
                    email = newValue.ifBlank { null },
                    emailError = errorMsg
                )
            )
        },
        modifier = Modifier.onFocusChanged { state ->
            if (!state.isFocused && !formData.email.isNullOrBlank() && formData.emailError == null && formData.email != initialDetail.email) {
                onCheckEmail(formData.email)
            }
        },
        placeholder = stringResource(Res.string.ph_email),
        isError = formData.emailError != null,
        errorMsg = formData.emailError?.getErrorMessage()
    )
}

@Composable
fun CreateEditUserFormLoading() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(6) {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }
    }
}

@Composable
private fun mapRoleOptionsString(optionData: List<OptionData<String>>): List<OptionData<String>> {
    return optionData.map { option ->
        OptionData(
            label = Role.fromValue(option.label).getLabel(),
            value = option.value
        )
    }
}