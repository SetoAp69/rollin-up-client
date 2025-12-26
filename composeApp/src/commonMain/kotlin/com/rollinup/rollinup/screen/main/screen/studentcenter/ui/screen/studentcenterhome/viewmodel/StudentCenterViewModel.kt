package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rollinup.apiservice.domain.user.GetUserListUseCase
import com.rollinup.apiservice.domain.user.GetUserOptionsUseCase
import com.rollinup.apiservice.domain.user.GetUserPagingUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.rollinup.component.export.FileWriter
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterCallback
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterFilterData
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.uistate.StudentCenterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentCenterViewModel(
    private val getUserListUseCase: GetUserListUseCase,
    private val getUserPagingUseCase: GetUserPagingUseCase,
    private val getUserOptionUseCase: GetUserOptionsUseCase,
    private val fileWriter: FileWriter,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudentCenterUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData = MutableStateFlow<PagingData<UserEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    fun init(user: LoginEntity?, isMobile: Boolean) {
        if (user == null) return
        _uiState.value = _uiState.value.copy(user = user, isMobile = isMobile)

        if (isMobile) {
            getPagingData()
        } else {
            getList()
        }
        getFilter()
    }

    fun getCallback() = StudentCenterCallback(
        onSearch = ::search,
        onFilter = ::filter,
        onRefresh = ::refresh,
        onExportFile = ::exportFile,
        onResetMessageState = ::resetMessageState,
    )

    private fun getFilter() {
        _uiState.update { it.copy(isLoadingFilter = true) }
        viewModelScope.launch {
            getUserOptionUseCase().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingFilter = false,
                                classOptions = result.data.classOptions
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoadingFilter = false) }
                    }
                }
            }
        }
    }

    private fun refresh() {
        if (_uiState.value.isMobile) {
            getPagingData()
        } else {
            getList()
        }
    }

    private fun getPagingData() {
        val queryParams = _uiState.value.queryParams
        viewModelScope.launch {
            getUserPagingUseCase(queryParams).collectLatest {
                _pagingData.value = it
            }
        }
    }

    private fun getList() {
        val queryParams = _uiState.value.queryParams
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getUserListUseCase(queryParams).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                itemList = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun filter(filterData: StudentCenterFilterData) {
        _uiState.update { it.copy(filterData = filterData) }
        refresh()
    }

    private fun search(searchQuery: String) {
        _uiState.update { it.copy(searchQuery = searchQuery) }
        refresh()
    }

    private fun exportFile(filename: String) {
        _uiState.update { it.copy(isLoadingOverlay = true, exportState = false) }
        val queryParams = _uiState.value.queryParams
        viewModelScope.launch {
            getUserListUseCase(queryParams).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        viewModelScope.launch {
                            val data = fetchExportData(result.data)
                            fileWriter.writeExcel(filename, data)
                            _uiState.update {
                                it.copy(
                                    exportState = true,
                                    isLoadingOverlay = false
                                )
                            }
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                exportState = false,
                                isLoadingOverlay = false
                            )
                        }
                    }
                }

            }
        }
    }

    private fun fetchExportData(data: List<UserEntity>): List<Pair<String, List<*>>> {
        return listOf(
            "Student ID" to data.map { d -> d.studentId },
            "Name" to data.map { d -> d.fullName },
            "Class" to data.map { d -> d.classX },
            "Email" to data.map { d -> d.email },
            "Address" to data.map { d -> d.address },
            "Gender" to data.map { d -> d.gender.label }
        )
    }

    private fun resetMessageState() {
        _uiState.update {
            it.copy(exportState = null)
        }
    }

}