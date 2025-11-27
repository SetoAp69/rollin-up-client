package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rollinup.apiservice.domain.permit.CancelPermitUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByStudentPagingUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit.StudentPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.uistate.StudentPermitUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentPermitViewModel(
    private val getPermitByStudentPagingUseCase: GetPermitByStudentPagingUseCase,
    private val cancelPermitUseCase: CancelPermitUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudentPermitUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData =
        MutableStateFlow<PagingData<PermitByStudentEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    fun init(user: LoginEntity?) {
        if(user == null) return
        _uiState.update { it.copy(user = user) }
        getPermitPaging()
    }

    fun getCallback() =
        StudentPermitCallback(
            onTabChange = ::tabChange,
            onRefresh = ::getPermitPaging,
            onFilter = ::filter,
            onSearch = ::search,
            onCancelPermit = ::cancelPermit
        )

    private fun getPermitPaging() {
        val queryparams = _uiState.value.queryParams
        val id = _uiState.value.user.id.ifBlank { return }

        viewModelScope.launch {
            getPermitByStudentPagingUseCase(id, queryparams).collectLatest { result ->
                _pagingData.value = result
            }
        }
    }

    private fun cancelPermit(id: String) {
        _uiState.update { it.copy(isLoadingOverlay = true) }
        viewModelScope.launch {
            cancelPermitUseCase(id).collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                        cancelState = result is Result.Success
                    )
                }
            }
        }
    }

    private fun search(searchQuery: String) {
        _uiState.update { it.copy(searchQuery = searchQuery) }
        getPermitPaging()
    }


    private fun filter(filterData: PermitFilterData) {
        _uiState.update { it.copy(filterData = filterData) }
        getPermitPaging()
    }

    private fun tabChange(index: Int) {
        _uiState.update { it.copy(currentTab = PermitTab.entries[index]) }
        getPermitPaging()
    }

    private fun resetMessageState() {
        _uiState.update { it.copy(cancelState = null) }
    }
}