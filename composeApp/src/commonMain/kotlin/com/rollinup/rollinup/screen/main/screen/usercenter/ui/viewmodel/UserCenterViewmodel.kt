package com.rollinup.rollinup.screen.main.screen.usercenter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.domain.user.GetUserListUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.rollinup.screen.dashboard.getDummyUsers
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterCallback
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterFilterData
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.uistate.UserCenterUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserCenterViewmodel(
    private val getUserListUseCase: GetUserListUseCase,
    /* TODO: private val getUserListOptionUseCase: GetUserListOptionUseCase,
     private val deleteeUserUseCase: DeleteUserUseCase,*/
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserCenterUiState())
    val uiState = _uiState.asStateFlow()

    fun init() {
        getUserList()
        getUserOptions()
    }

    fun getCallback() =
        UserCenterCallback(
            onSearch = ::search,
            onFilter = ::filter,
            onRefresh = {},
            onUpdateSelection = ::updateSelection,
            onResetMessageState = ::resetMessageState,
            onDeleteUser = {},
            onSelectAll = ::selectAll,
        )

    private fun getUserList() {
        val queryParams = uiState.value.queryParams
        _uiState.update { it.copy(isLoadingList = true) }

        viewModelScope.launch {
            delay(1000)
            if (true) {
                _uiState.update { it.copy(isLoadingList = false, itemList = getDummyUsers(36)) }
                return@launch
            }
            getUserListUseCase(queryParams).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingList = false,
                                itemList = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingList = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getUserOptions() {

    }

    private fun search(searchQuery: String) {
        _uiState.update { it.copy(searchQuery = searchQuery) }
        getUserList()
    }

    private fun filter(filterData: UserCenterFilterData) {
        _uiState.update { it.copy(filterData = filterData) }
        getUserList()
    }

    private fun updateSelection(data: UserEntity) {
        val selectedItem = _uiState.value.itemSelected.toMutableList()
        if (selectedItem.contains(data)) {
            selectedItem.remove(data)
        } else {
            selectedItem.add(data)
        }
        _uiState.update { it.copy(itemSelected = selectedItem) }
    }

    private fun selectAll() {
        if (_uiState.value.isAllSelected) {
            _uiState.update { it.copy(itemSelected = emptyList()) }
        } else {
            _uiState.update { it.copy(itemSelected = _uiState.value.itemList) }
        }
    }

    private fun resetMessageState() {
        _uiState.update { it.copy(deleteUserState = null) }
    }

    private fun deleteUser() {

    }

}