package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.data.source.network.model.request.user.CheckEmailOrUsernameQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.domain.user.CheckEmailOrUsernameUseCase
import com.rollinup.apiservice.domain.user.EditUserUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.domain.user.GetUserOptionsUseCase
import com.rollinup.apiservice.domain.user.RegisterUserUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserCallback
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormData
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.uistate.CreateEditUserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEditUserViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getUserOptionUseCase: GetUserOptionsUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val checkEmailOrUsernameUseCase: CheckEmailOrUsernameUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateEditUserUiState())
    val uiState = _uiState.asStateFlow()

    fun init(id: String?) {
        getOptions()

        _uiState.value = _uiState.value.copy(isEdit = id != null)
        id?.let {
            _uiState.value = _uiState.value.copy(isLoading = true)
            viewModelScope.launch {
                getUserByIdUseCase(id).collectLatest { result ->
                    if (result is Result.Success) {
                        _uiState.update {
                            it.copy(
                                formData = fetchFormFromDetail(result.data),
                                initialDetail = result.data
                            )
                        }
                    }
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun getOptions() {
        _uiState.update { it.copy(isLoadingOptions = true) }

        viewModelScope.launch {
            getUserOptionUseCase().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingOptions = false,
                                formOptions = state.formOptions.copy(
                                    role = result.data.roleIdOptions,
                                    classX = result.data.classIdOptions
                                )
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoadingOptions = false) }
                    }
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

    fun resetUiState() {
        _uiState.update { CreateEditUserUiState() }
    }

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

        if (!isEdit && formData.userName.isNullOrBlank()) {
            formData = formData.copy(userNameError = "Username can't be empty")
        }

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
                classError = formData.classId == null && isStudentRole,
                roleError = formData.role == null,
            )
        }
        updateForm(formData)
        return formData.isValid()
    }

    private fun submit(formData: CreateEditUserFormData, isEdit: Boolean) {
        if (!validateForm(formData, isEdit)) return

        val body = mapBodyFromForm(formData)
        val domain =
            if (isEdit)
                editUserUseCase(id = formData.id, body = body)
            else
                registerUserUseCase(body)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingOverlay = true, submitState = null) }
            domain.collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                        submitState = result is Result.Success
                    )
                }
            }
        }
    }

    private fun checkUserName(username: String) {
        viewModelScope.launch {
            val query = CheckEmailOrUsernameQueryParams(username = username)
            checkEmailOrUsernameUseCase(query).collectLatest { result ->
                val errorMessage =
                    if (result is Result.Error) "Username is already used" else null

                _uiState.update { state ->
                    state.copy(
                        formData = state.formData.copy(
                            userNameError = errorMessage
                        )
                    )
                }
            }
        }
    }

    private fun checkEmail(email: String) {
        viewModelScope.launch {
            val query = CheckEmailOrUsernameQueryParams(email = email)
            checkEmailOrUsernameUseCase(query).collectLatest { result ->
                val errorMessage =
                    if (result is Result.Error) "Email is already used" else null

                _uiState.update { state ->
                    state.copy(
                        formData = state.formData.copy(
                            emailError = errorMessage
                        )
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
            birthDay = data.birthDay.parseToLocalDateTime().toEpochMillis(),
            role = data.role.id,
            classId = data.classX?.id,
            studentId = data.studentId,
            address = data.address,
            phone = data.phoneNumber,
            email = data.email,
        )
    }

    private fun mapBodyFromForm(
        formData: CreateEditUserFormData,
    ): CreateEditUserBody {
        return CreateEditUserBody(
            username = formData.userName,
            firstName = formData.firstName,
            lastName = formData.lastName,
            email = formData.email,
            role = formData.role,
            address = formData.address,
            studentId = formData.studentId,
            classX = formData.classId,
            birthDay = formData.birthDay,
            phoneNumber = formData.phone,
            gender = formData.gender
        )
    }
}
