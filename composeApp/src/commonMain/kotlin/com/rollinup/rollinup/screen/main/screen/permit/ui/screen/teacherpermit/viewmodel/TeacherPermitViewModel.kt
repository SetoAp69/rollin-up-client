package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.rollinup.apiservice.domain.permit.GetPermitByClassListUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByClassPagingUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.dashboard.generateDummyPermitByClassList
import com.rollinup.rollinup.screen.dashboard.generateDummyPermitByStudentList
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeacherPermitViewModel(
    private val getPermitByClassListUseCase: GetPermitByClassListUseCase,
    private val getPermitByClassPagingUseCase: GetPermitByClassPagingUseCase,
//    private val
) : ViewModel() {
    private val _uiState = MutableStateFlow(TeacherPermitUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData = MutableStateFlow<PagingData<PermitByClassEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    val isMobile = getPlatform().isMobile()

    fun init(user: LoginEntity?) {
        if (user == null) return
        _uiState.update { it.copy(user = user) }

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
            onSearch = ::search
        )

    private fun getItemList() {
        val classKey = _uiState.value.user.classKey ?: return
        val queryParams = _uiState.value.queryParams

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            if(true){
                delay(500)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        itemList = generateDummyPermitByClassList(88)
                    )
                }
            }
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
        if (isMobile) {
            getItemPaging()
        } else {
            getItemList()
        }
    }

    private fun getItemPaging() {
        val classKey = _uiState.value.user.classKey ?: return
        val queryParams = _uiState.value.queryParams

        viewModelScope.launch {
            delay(1000)
            if(true){
                _pagingData.value = PagingData.from(
                    data=generateDummyPermitByClassList(88),
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.NotLoading(true),
                        prepend = LoadState.NotLoading(true),
                        append = LoadState.NotLoading(true)
                    )
                )
                return@launch
            }
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
        if (isMobile) {
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
}