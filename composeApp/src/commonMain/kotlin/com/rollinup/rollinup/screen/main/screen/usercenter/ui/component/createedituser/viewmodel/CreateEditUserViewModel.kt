package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.domain.user.EditUserUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.domain.user.RegisterUserUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.screen.dashboard.generateDummyUserDetail
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserCallback
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormData
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.uistate.CreateEditUserUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEditUserViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateEditUserUiState())
    val uiState = _uiState.asStateFlow()

    fun init(id: String?) {
        _uiState.value = _uiState.value.copy(isEdit = id != null)
        id?.let {
            viewModelScope.launch {
                if (true) {
                    delay(1000)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            formData = fetchFormFromDetail(generateDummyUserDetail())
                        )
                    }
                    return@launch
                }
                getUserByIdUseCase(id).collectLatest { result ->
                    if (result is Result.Success) {
                        _uiState.update { it.copy(formData = fetchFormFromDetail(result.data)) }
                    }
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun getCallback() =
        CreateEditUserCallback(
            onUpdateForm = ::updateForm,
            onResetForm = ::resetForm,
            onResetMessageState = ::resetMessageState,
            onSubmit = ::submit,
            onValidateForm = ::validateForm,
            onCheckEmail = ::checkEmail,
            onCheckUserName = ::checkUserName,
            onToggleStay = ::toggleStay,
        )

    private fun updateForm(formData: CreateEditUserFormData) {
        _uiState.update { it.copy(formData = formData) }
    }

    private fun resetMessageState() {
        _uiState.update { it.copy(submitState = null) }
    }

    private fun resetForm() {
        _uiState.update { it.copy(formData = CreateEditUserFormData()) }
    }

    private fun toggleStay(isStay: Boolean) {
        _uiState.update { it.copy(isStay = isStay) }
    }

    private fun validateForm(formData: CreateEditUserFormData, isEdit: Boolean): Boolean {
        var formData = formData
        val formOptions = uiState.value.formOptions

        val isStudentRole = formData.role ==
                formOptions.role.find { it.label.equals("Student", true) }?.value

        if (!isEdit && formData.lastName.isNullOrBlank()) {
            formData = formData.copy(lastNameError = "Last name can't be empty")
        }

        if (!isEdit && formData.firstName.isNullOrBlank()) {
            formData = formData.copy(firstNameError = "First bane can't be empty")
        }

        if (!isEdit && formData.studentId.isNullOrBlank() && isStudentRole) {
            formData = formData.copy(studentIdError = "Student ID can't be empty")
        }

        if (!isEdit && formData.email.isNullOrBlank()) {
            formData = formData.copy(emailError = "Email can't be empty")
        }

        if (!isEdit && formData.selectorData.any { it == null }) {
            formData = formData.copy(
                genderError = formData.gender == null,
                birthDayError = formData.birthDay == null,
                classError = formData.classId == null,
                roleError = formData.role == null,
            )
        }

        updateForm(formData)
        return formData.isValid()
    }

    private fun submit(formData: CreateEditUserFormData, isEdit: Boolean) {
        val body = mapBodyFromForm(formData)
        val domain =
            if (isEdit)
                editUserUseCase(id = formData.id, body = body)
            else
                registerUserUseCase(body)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingOverlay = true) }
            domain.collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        submitState = result is Result.Success
                    )
                }
            }
        }
    }

    private fun checkUserName(userName: String) {
        viewModelScope.launch {
            delay(300)
            if ((0..1).random() == 1) {
                _uiState.update {
                    it.copy(
                        formData = it.formData.copy(userNameError = "Username is already exists")
                    )
                }
            }
        }
    }

    private fun checkEmail(email: String) {
        viewModelScope.launch {
            delay(300)
            if ((0..1).random() == 1) {
                _uiState.update {
                    it.copy(
                        formData = it.formData.copy(emailError = "Email is already used")
                    )
                }
            }
        }
    }

    private fun fetchFormFromDetail(data: UserDetailEntity): CreateEditUserFormData {
        return CreateEditUserFormData(
            id = data.id,
            firstName = data.firstName,
            userName = data.userName,
            lastName = data.lastName,
            gender = data.gender.value,
            birthDay = data.birthDay.toLocalDateTime().toEpochMillis(),
            role = data.role.id,
            classId = data.classX?.id,
            studentId = data.studentId,
            address = data.address,
            phone = data.phoneNumber,
            email = data.email,
        )
    }

    private fun mapBodyFromForm(formData: CreateEditUserFormData): CreateEditUserBody {
        return CreateEditUserBody(
            username = formData.userName,
            firstName = formData.firstName,
            lastName = formData.lastName,
            email = formData.email,
            password = "TemporaryPassword12@",
            role = formData.role,
            address = formData.address,
            classX = formData.classId,
            phoneNumber = formData.phone,
            gender = formData.gender
        )
    }
}
