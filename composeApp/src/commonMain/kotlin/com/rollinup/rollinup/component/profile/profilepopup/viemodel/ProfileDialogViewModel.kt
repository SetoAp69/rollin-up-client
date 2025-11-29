package com.rollinup.rollinup.component.profile.profilepopup.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.domain.user.EditUserUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.profile.profilepopup.model.EditProfileFormData
import com.rollinup.rollinup.component.profile.profilepopup.uistate.ProfileDialogUiState
import com.rollinup.rollinup.getDummyUserDetail
import com.rollinup.rollinup.component.profile.profilepopup.model.ProfileCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileDialogViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val editUserByIdUseCase: EditUserUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileDialogUiState())
    val uiState = _uiState.asStateFlow()

    fun init(id: String, showEdit: Boolean) {
        _uiState.update { it.copy(isLoading = true, showEdit = showEdit) }
        viewModelScope.launch {
            if (true) {
                delay(1000)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userDetail = getDummyUserDetail()
                    )
                }
                return@launch
            }
            getUserByIdUseCase(id).collectLatest { result ->
                if (result is Result.Success) {
                    _uiState.update { it.copy(userDetail = result.data) }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun reset() {
        _uiState.update { ProfileDialogUiState() }
    }

    fun getCallback() = ProfileCallback(
        onToggleEdit = ::toggleEdit,
        onUpdateForm = ::updateFormData,
        onSubmit = ::submit,
        onValidate = ::validateForm,
        onResetMessageState = ::resetMessageState
    )

    private fun toggleEdit() {
        val isEdit = !_uiState.value.isEdit
        val formData = if (isEdit) {
            fetchFormDataFromProfile(uiState.value.userDetail)
        } else {
            EditProfileFormData()
        }
        _uiState.update {
            it.copy(
                isEdit = isEdit,
                formData = formData
            )
        }
    }

    private fun updateFormData(formData: EditProfileFormData) {
        _uiState.update { it.copy(formData = formData) }
    }

    private fun resetMessageState() {
        _uiState.update { it.copy(editState = null) }
    }

    private fun validateForm(formData: EditProfileFormData): Boolean {
        var formData = formData
        if (formData.firstName?.isBlank() ?: false) {
            formData = formData.copy(
                firstNameError = "First name can\'t be empty"
            )
        }
        if (formData.lastName?.isBlank() ?: false) {
            formData = formData.copy(
                lastNameError = "Last name can\'t be empty"
            )
        }
        if (formData.email?.isBlank() ?: false) {
            formData = formData.copy(
                emailError = "Email can\'t be empty"
            )
        }

        _uiState.update {
            it.copy(formData = formData)
        }
        return formData.isValid()
    }

    private fun submit(formData: EditProfileFormData) {
        if (!validateForm(formData)) return
        val body = mapFormToBody(formData)
        _uiState.update { it.copy(isLoading = false) }

        viewModelScope.launch {
            editUserByIdUseCase(formData.id, body).collectLatest { result ->
                _uiState.update {
                    it.copy(editState = result is Result.Success, isLoading = false)
                }
            }
        }
    }

    private fun fetchFormDataFromProfile(
        profile: UserDetailEntity,
    ) = EditProfileFormData(
        id = profile.id,
        firstName = profile.firstName,
        lastName = profile.lastName,
        userName = profile.userName,
        gender = profile.gender.value,
        birthDay = profile.birthDay.toLocalDateTime().toEpochMillis(),
        address = profile.address,
        phone = profile.phoneNumber,
        email = profile.email,
    )

    private fun mapFormToBody(formData: EditProfileFormData) = CreateEditUserBody(
        username = formData.userName,
        firstName = formData.firstName,
        lastName = formData.lastName,
        email = formData.email,
        address = formData.address,
        phoneNumber = formData.phone,
        gender = formData.gender
    )
}