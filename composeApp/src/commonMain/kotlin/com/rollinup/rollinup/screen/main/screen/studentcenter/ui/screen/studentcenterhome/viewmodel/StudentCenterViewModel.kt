package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.rollinup.apiservice.domain.user.GetUserListUseCase
import com.rollinup.apiservice.domain.user.GetUserPagingUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.getDummyUsers
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterCallback
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterFilterData
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.uistate.StudentCenterUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentCenterViewModel(
    private val getUserListUseCase: GetUserListUseCase,
    private val getUserPagingUseCase: GetUserPagingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudentCenterUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData = MutableStateFlow<PagingData<UserEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    val isMobile = getPlatform().isMobile()

    fun init(user: LoginEntity?) {
        if (user == null) return
        _uiState.value = _uiState.value.copy(user = user)

        if (isMobile) {
            getPagingData()
        } else {
            getList()
        }
    }

    fun getCallback() = StudentCenterCallback(
        onSearch = ::search,
        onFilter = ::filter,
        onRefresh = ::refresh
    )

    private fun refresh() {
        if (isMobile) {
            getPagingData()
        } else {
            getList()
        }
    }

    private fun getPagingData() {
        val queryParams = _uiState.value.queryParams
        viewModelScope.launch {
            if(true){
                delay(1000)
                _pagingData.value = PagingData.from(getDummyUsers(20), sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true)
                ))
                return@launch
            }
            getUserPagingUseCase(queryParams).collectLatest {
                _pagingData.value = it
            }
        }
    }

    private fun getList() {
        val queryParams = _uiState.value.queryParams
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if(true){
                delay(1000)
                _uiState.update { it.copy(isLoading = false, itemList = getDummyUsers(20)) }
                return@launch
            }
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
}