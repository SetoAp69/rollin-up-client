package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.common.utils.Utils.toEpochMilli
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.checkbox.CheckBox
import com.rollinup.rollinup.component.date.DatePickerDialog
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.selector.SingleDropDownSelector
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.PhoneNumberTextField
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserCallback
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormData
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormOption
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.uistate.CreateEditUserUiState
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.viewmodel.CreateEditUserViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_right_24

@Composable
fun CreateEditUserContent(
    showDialog: Boolean,
    id: String?,
    onDismissRequest: (Boolean) -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: CreateEditUserViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()

    DisposableEffect(Unit) {
        if (showDialog) {
            viewModel.init(id)
        }
        onDispose {
            cb.onResetForm()
        }
    }
    CreateEditUserStateHandler(
        uiState = uiState,
        onResetMessageState = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar,
        onDismissRequest = onDismissRequest,
        onResetForm = cb.onResetForm,
    )
    CreateEditUserForm(
        uiState = uiState,
        cb = cb
    )
}

@Composable
fun CreateEditUserForm(
    uiState: CreateEditUserUiState,
    cb: CreateEditUserCallback,
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            UserNameSection(
                isEdit = uiState.isEdit,
                formData = uiState.formData,
                onUpdateForm = cb.onUpdateForm,
                onCheckUserName = cb.onCheckUserName
            )
            NameSection(
                isEdit = uiState.isEdit,
                formData = uiState.formData,
                onUpdateForm = cb.onUpdateForm
            )
            SelectorSection(
                options = uiState.formOptions,
                formData = uiState.formData,
                onUpdateForm = cb.onUpdateForm
            )
            AdditionalInfoSection(
                isEdit = uiState.isEdit,
                isStudent = uiState.isStudentRole,
                formData = uiState.formData,
                onUpdateForm = cb.onUpdateForm,
                onCheckEmail = cb.onCheckEmail
            )
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
    Column {
        if (uiState.isEdit) {
            Row(
                modifier = Modifier.clickable { cb.onToggleStay(!uiState.isStay) }
            ) {
                CheckBox(
                    checked = uiState.isStay,
                    onCheckedChange = cb.onToggleStay
                )
                Spacer(itemGap8)
                Text(
                    text = "Stay in form",
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
            if (cb.onValidateForm(uiState.formData, uiState.isEdit))
                return@Button
            cb.onSubmit(uiState.formData, uiState.isEdit)
        }
    }
}

@Composable
fun UserNameSection(
    isEdit: Boolean,
    formData: CreateEditUserFormData,
    onUpdateForm: (CreateEditUserFormData) -> Unit,
    onCheckUserName: (String) -> Unit,
) {
    TextField(
        isRequired = !isEdit,
        modifier = Modifier.onFocusChanged() { focusState ->
            if (!focusState.isFocused && !formData.userName.isNullOrBlank()) {
                onCheckUserName(formData.userName)
            }
        },
        onValueChange = { newValue ->
            val errorMessage: String?
            if (newValue.contains(" ")) {
                errorMessage = "Username cannot contain space"
            } else {
                errorMessage = null
            }

            onUpdateForm(
                formData.copy(
                    userName = newValue.ifBlank { null },
                    userNameError = errorMessage
                )
            )
        },
        value = formData.userName ?: "",
        placeholder = "Enter username",
        title = "Username",
        isError = formData.userNameError != null,
        errorMsg = formData.userNameError
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
                title = "First Name",
                isRequired = !isEdit,
                maxChar = 15,
                value = formData.firstName ?: "",
                placeholder = "Enter first name",
                onValueChange = { value ->
                    onUpdateForm(
                        formData.copy(
                            firstName = value.ifBlank { null },
                            firstNameError = null
                        )
                    )
                },
                isError = formData.firstNameError != null,
                errorMsg = formData.firstNameError
            )
        }
        Spacer(itemGap8)
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                isRequired = !isEdit,
                title = "Last Name",
                maxChar = 15,
                value = formData.lastName ?: "",
                placeholder = "Enter last name",
                onValueChange = { value ->
                    onUpdateForm(
                        formData.copy(
                            lastName = value.ifBlank { null },
                            lastNameError = null

                        )
                    )
                },
                isError = formData.firstNameError != null,
                errorMsg = formData.firstNameError
            )
        }
    }
}

@Composable
private fun SelectorSection(
    options: CreateEditUserFormOption,
    formData: CreateEditUserFormData,
    onUpdateForm: (CreateEditUserFormData) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(itemGap8)
    ) {
        SingleDropDownSelector(
            title = "Gender",
            value = formData.gender,
            options = options.gender,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        gender = it,
                        genderError = false
                    )
                )
            },
            modifier = Modifier.weight(1f)
        )
        DatePicker(
            title = "Birth day",
            value = formData.birthDay,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        birthDay = it,
                        birthDayError = false
                    )
                )
            }
        )
        SingleDropDownSelector(
            title = "Role",
            value = formData.role,
            options = options.role,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        role = it,
                        roleError = false
                    )
                )
            },
            modifier = Modifier.weight(1f)
        )
        SingleDropDownSelector(
            title = "Class",
            value = formData.classId,
            options = options.classX,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        classId = it,
                        classError = false
                    )
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AdditionalInfoSection(
    isEdit: Boolean,
    isStudent: Boolean,
    formData: CreateEditUserFormData,
    onUpdateForm: (CreateEditUserFormData) -> Unit,
    onCheckEmail: (String) -> Unit,
) {
    if (isStudent) {
        TextField(
            isRequired = !isEdit,
            onValueChange = { newValue ->
                val errorMessage: String?
                if (newValue.contains(" ")) {
                    errorMessage = "Student Id can't contain spaces"
                } else {
                    errorMessage = null
                }

                onUpdateForm(
                    formData.copy(
                        studentId = newValue.ifBlank { null },
                        studentIdError = errorMessage
                    )
                )
            },
            value = formData.studentId ?: "",
            placeholder = "Enter student id",
            title = "Student Id",
            isError = formData.studentIdError != null,
            errorMsg = formData.studentIdError
        )
    }
    TextField(
        title = "Address",
        maxChar = 120,
        value = formData.address ?: "",
        placeholder = "Enter address",
        onValueChange = {
            onUpdateForm(
                formData.copy(
                    address = it.ifBlank { null },
                    addressError = null
                )
            )
        },
        isError = formData.addressError != null,
        errorMsg = formData.addressError,
    )
    PhoneNumberTextField(
        title = "Phone",
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
        placeholder = "Enter phone number",
        isError = formData.phoneError != null,
        errorMsg = formData.phoneError
    )
    TextField(
        title = "Email",
        value = formData.email ?: "",
        maxChar = 30,
        onValueChange = { newValue ->
            val errorMsg =
                if (newValue.isNotBlank() && newValue.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex())) {
                    null
                } else {
                    "Invalid email format"
                }
            onUpdateForm(
                formData.copy(
                    email = newValue.ifBlank { null },
                    emailError = errorMsg
                )
            )
        },
        modifier = Modifier.onFocusChanged { state ->
            if (!state.isFocused && !formData.email.isNullOrBlank() && formData.emailError == null) {
                onCheckEmail(formData.email)
            }
        },
        placeholder = "Enter email",
        isError = formData.email != null,
        errorMsg = formData.emailError
    )
}


@Composable
private fun DatePicker(
    title: String,
    value: Long?,
    enable: Boolean = true,
    contentColor: Color = theme.textPrimary,
    backgroundColor: Color = theme.secondary,
    placeHolder: String = "-",
    modifier: Modifier = Modifier,
    onValueChange: (Long) -> Unit,
) {
    var showSelector by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (showSelector) 90f else 0F)
    val sValue = value?.toString()?.toLocalDateTime()?.date?.toString() ?: placeHolder
    val dateValue = value?.toLocalDateTime()?.date

    TextFieldTitle(
        text = title,
    ) {
        Row(
            modifier = Modifier
                .clickable(enable) {
                    showSelector = true
                }
                .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                .width(100.dp)
                .padding(itemGap4)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(itemGap4),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                tint = contentColor,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(rotationState),
                contentDescription = null
            )
            Text(
                text = sValue,
                style = Style.title,
                color = contentColor
            )
        }
    }
    DatePickerDialog(
        isShowDialog = showSelector,
        onDismissRequest = { showSelector },
        value = dateValue?.let {
            listOf(it, it)
        } ?: emptyList(),
        maxSelection = 1,
        isDisabledPastSelection = false,
        isAllSelectable = true,
        onValueChange = {
            onValueChange(it.first().toEpochMilli())
        }
    )
}
