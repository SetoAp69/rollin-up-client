package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.domain.permit.GetPermitByClassListUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByClassPagingUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.common.utils.Utils.toEpochMilli
import com.rollinup.rollinup.component.date.DateFormatter
import com.rollinup.rollinup.component.export.FileWriter
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class TeacherPermitViewModel(
    private val getPermitByClassListUseCase: GetPermitByClassListUseCase,
    private val getPermitByClassPagingUseCase: GetPermitByClassPagingUseCase,
    private val fileWriter: FileWriter,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TeacherPermitUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData = MutableStateFlow<PagingData<PermitByClassEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    fun init(user: LoginEntity?, isMobile: Boolean) {
        if (user == null) return
        _uiState.update { it.copy(user = user, isMobile = isMobile) }
        if (isMobile) {
            getItemPaging()
        } else {
            getItemList()
        }
    }

    fun getCallback() =
        TeacherPermitCallback(
            onUpdateSelection = ::updateSelection,
            onSelectAll = ::selectAll,
            onTabChange = ::tabChange,
            onRefresh = ::refresh,
            onFilter = ::filter,
            onSearch = ::search,
            onResetSelection = ::resetSelection,
            onExportFile = ::exportFile,
            onResetMessageState = ::resetMessageState,
            onUpdateExportDateRange = ::updateExportDateRange
        )

    private fun getItemList() {
        val classKey = _uiState.value.user.classKey ?: return
        val queryParams = _uiState.value.queryParams

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getPermitByClassListUseCase(classKey, queryParams).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                itemList = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun refresh() {
        if (uiState.value.isMobile) {
            getItemPaging()
        } else {
            getItemList()
        }
    }

    private fun resetSelection() {
        _uiState.update { it.copy(itemSelected = emptyList()) }
    }

    private fun getItemPaging() {
        val classKey = _uiState.value.user.classKey ?: return
        val queryParams = _uiState.value.queryParams

        viewModelScope.launch {
            getPermitByClassPagingUseCase(classKey, queryParams).collectLatest { result ->
                _pagingData.value = result
            }
        }
    }

    private fun search(searchQuery: String) {
        _uiState.update { it.copy(searchQuery = searchQuery) }
        refresh()
    }


    private fun filter(filterData: PermitFilterData) {
        _uiState.update { it.copy(filterData = filterData) }
        refresh()
    }

    private fun tabChange(index: Int) {
        _uiState.update { it.copy(currentTab = PermitTab.entries[index]) }
        refresh()
    }

    private fun updateSelection(data: PermitByClassEntity) {
        val selected = _uiState.value.itemSelected.toMutableList()
        if (selected.contains(data)) {
            selected.remove(data)
        } else {
            selected.add(data)
        }
        _uiState.update { it.copy(itemSelected = selected) }
    }

    private fun selectAll() {
        if (uiState.value.isMobile) {
            pagingSelectAll()
        } else {
            listSelectAll()
        }
    }

    private fun listSelectAll() {
        val selected =
            if (_uiState.value.isAllSelected)
                emptyList()
            else
                _uiState.value.itemList

        _uiState.update { it.copy(itemSelected = selected) }
    }

    private fun pagingSelectAll() {
        if (_uiState.value.isAllSelected) {
            _uiState.update { it.copy(itemSelected = emptyList()) }
        } else {
            val classKey = _uiState.value.user.classKey ?: return
            val queryParams = _uiState.value.queryParams

            viewModelScope.launch {
                _uiState.update { it.copy(isLoadingOverlay = true) }
                getPermitByClassListUseCase(classKey, queryParams).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoadingOverlay = false,
                                    itemSelected = result.data,
                                    itemList = result.data
                                )
                            }
                        }

                        is Result.Error -> {
                            _uiState.update { it.copy(isLoadingOverlay = false) }
                        }
                    }
                }
            }
        }
    }

    private fun updateExportDateRange(dateRange: List<LocalDate>) {
        _uiState.update {
            it.copy(
                exportDateRange = dateRange.sorted()
            )
        }
    }

    private fun exportFile(fileName: String) {
        _uiState.update { it.copy(isLoadingOverlay = true, exportState = null) }
        viewModelScope.launch {
            val dateRange = _uiState.value.exportDateRange.map { it.toEpochMilli() }
            val queryParams = GetPermitListQueryParams(
                dateRange = dateRange.toJsonString(),
                isActive = (_uiState.value.currentTab == PermitTab.ACTIVE).toString(),
            )
            val classKey = _uiState.value.user.classKey ?: return@launch

            getPermitByClassListUseCase(classKey, queryParams).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        viewModelScope.launch {
                            val data = fetchExportData(result.data)
                            fileWriter.writeExcel(fileName, data)
                            _uiState.update {
                                it.copy(
                                    isLoadingOverlay = false,
                                    exportState = true
                                )
                            }
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingOverlay = false,
                                exportState = false
                            )
                        }
                    }

                }
                _uiState.update { it.copy(exportDateRange = emptyList()) }
            }
        }
    }

    private fun fetchExportData(data: List<PermitByClassEntity>): List<Pair<String, List<*>>> {
        return listOf(
            "Name" to data.map { d -> d.student.name },
            "Class" to data.map { d -> d.student.xClass },
            "Type" to data.map { d -> d.type.label },
            "Duration" to data.map { d ->
                DateFormatter.formatPermitDateRange(
                    type = d.type,
                    start = d.startTime,
                    end = d.endTime,
                )
            },
            "Reason" to data.map { d -> d.reason ?: "-" },
            "Status" to data.map { d -> d.approvalStatus.label },
            "Created at" to data.map { d -> DateFormatter.formateDateTimeFromString(d.createdAt) }
        )
    }

    private fun resetMessageState() {
        _uiState.update {
            it.copy(
                exportState = null
            )
        }
    }
}