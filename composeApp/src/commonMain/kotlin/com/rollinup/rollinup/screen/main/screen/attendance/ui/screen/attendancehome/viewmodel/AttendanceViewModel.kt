package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.getDummyAttendanceByClass
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceCallback
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceFilterData
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendanceViewModel(
    private val getAttendanceByClassListUseCase: GetAttendanceByClassListUseCase,
    private val getAttendanceByIdUseCase: GetAttendanceByIdUseCase,
    private val getAttendanceByClassPagingUseCase: GetAttendanceByClassPagingUseCase,
) : ViewModel() {
    private val isMobile = getPlatform().isMobile()

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData =
        MutableStateFlow<PagingData<AttendanceByClassEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    fun init(localUser: LoginEntity?) {
        if (localUser == null) return
        _uiState.update { it.copy(user = localUser) }
        if(isMobile){
            getPagingData()
        }else{
            getAttendanceList()
        }

    }

    fun getCallback() = AttendanceCallback(
        onRefresh = ::refresh,
        onSearch = ::search,
        onFilter = ::filter,
        onGetDetail = ::getDetail,
    )

    private fun getAttendanceList() {
        val queryParams = _uiState.value.queryParams
        val key = _uiState.value.user.classKey ?: return

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (true) {
                delay(1000)
                _uiState.update { it.copy(isLoading = false, attendanceList = getDummyAttendanceByClass(20)) }
                return@launch
            }
            getAttendanceByClassListUseCase(
                classKey = key,
                queryParams = queryParams
            ).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                attendanceList = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun getPagingData() {
        val queryParams = _uiState.value.queryParams
        val key = _uiState.value.user.classKey ?: return
        viewModelScope.launch {
            if (true) {
                delay(1000)
                _pagingData.value = PagingData.from(
                    getDummyAttendanceByClass(20),
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.NotLoading(endOfPaginationReached = true),
                        append = LoadState.NotLoading(endOfPaginationReached = true),
                        prepend = LoadState.NotLoading(endOfPaginationReached = true)
                    )
                )
                return@launch
            }
            getAttendanceByClassPagingUseCase(key, queryParams).collectLatest {
                _pagingData.value = it
            }
        }
    }

    private fun getSummary() {
        _uiState.update { it.copy(isLoadingSummary = true) }
        viewModelScope.launch {
            if (true) {
                delay(500)
                _uiState.update { it.copy(isLoadingSummary = false) }
                return@launch
            }
        }
    }

    private fun getDetail(data: AttendanceByClassEntity) {
        if (data.attendance?.status == AttendanceStatus.NO_DATA) {
            _uiState.update { it.copy(attendanceDetail = generateBlankDetail(data)) }
            return
        }

        _uiState.update { it.copy(isLoadingDetail = true) }
        viewModelScope.launch {
            getAttendanceByIdUseCase(data.attendance!!.id).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingDetail = false,
                                attendanceDetail = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoadingDetail = false) }
                    }
                }
            }
        }
    }

    private fun search(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        refresh()
    }

    private fun filter(filterData: AttendanceFilterData) {
        _uiState.update { it.copy(filterData = filterData) }
        refresh()
    }

    private fun refresh() {
        if (isMobile) {
            getPagingData()
        } else {
            getAttendanceList()
        }
        getSummary()
    }

    private fun generateBlankDetail(data: AttendanceByClassEntity) =
        AttendanceDetailEntity(
            id = "",
            student = AttendanceDetailEntity.User(
                id = data.student.id,
                studentId = data.student.studentId,
                name = data.student.name,
            ),
            status = AttendanceStatus.NO_DATA,
        )
}
