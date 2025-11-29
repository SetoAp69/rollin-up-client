package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentPagingUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.getDummyAttendanceByStudent
import com.rollinup.rollinup.getDummyProfile
import com.rollinup.rollinup.getDummyUserDetail
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent.AttendanceByStudentCallback
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendanceByStudentViewModel(
    private val getAttendanceByStudentListUseCase: GetAttendanceByStudentListUseCase,
    private val getAttendanceByStudentPagingUseCase: GetAttendanceByStudentPagingUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getAttendanceByIdUseCase: GetAttendanceByIdUseCase,
) : ViewModel() {
    private val isMobile = getPlatform().isMobile()

    private val _uiState = MutableStateFlow(AttendanceByStudentUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData =
        MutableStateFlow<PagingData<AttendanceByStudentEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    fun init(studentUserId: String) {
        if (studentUserId.isBlank()) return
        if (isMobile) {
            getPaging()
        } else {
            getItemList()
        }
        getProfile()
        getSummary()
    }

    fun reset(){
        _uiState.update { AttendanceByStudentUiState() }
    }

    fun getCallback() = AttendanceByStudentCallback(
        onSelectStatus = ::selectStatus,
        onRefresh = ::refresh,
        onGetDetail = ::getDetail
    )

    private fun refresh() {
        if (isMobile) {
            getPaging()
        } else {
            getItemList()
        }
        getSummary()
    }

    private fun getProfile() {
        val studentUserId = _uiState.value.studentUserId
        _uiState.update { it.copy(isLoadingProfile = true) }
        viewModelScope.launch {
            if(true) {
                delay(1000)
                _uiState.update { it.copy(isLoadingProfile = false, student = getDummyUserDetail()) }
                return@launch
            }
            getUserByIdUseCase(studentUserId).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                student = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoadingProfile = false) }
                    }
                }
            }
        }
    }

    private fun selectStatus(status: List<AttendanceStatus>) {
        _uiState.update { it.copy(statusSelected = status) }
        refresh()
    }

    private fun getSummary() {
        val studentUserId = _uiState.value.studentUserId
        _uiState.update { it.copy(isLoadingSummary = true) }
        viewModelScope.launch {
            delay(1000)
            _uiState.update { it.copy(isLoadingSummary = false) }
        }
    }

    private fun getPaging() {
        val query = uiState.value.queryParams
        val studentUserId = _uiState.value.studentUserId

        viewModelScope.launch {
            if (true) {
                delay(1000)
                _pagingData.value = PagingData.from(getDummyAttendanceByStudent(20), sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true)
                ))
                return@launch
            }
            getAttendanceByStudentPagingUseCase(studentUserId, query).collectLatest {
                _pagingData.value = it
            }
        }
    }

    private fun getItemList() {
        val query = uiState.value.queryParams
        val studentUserId = _uiState.value.studentUserId

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            if (true) {
                delay(1000)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        attendanceList = getDummyAttendanceByStudent(20)
                    )
                }
                return@launch
            }
            getAttendanceByStudentListUseCase(studentUserId, query).collectLatest { result ->
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
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun getDetail(id: String) {
        if (id.isBlank()) return
        _uiState.update { it.copy(isLoadingDetail = true) }
        viewModelScope.launch {
            if (true) {
                delay(1000)
                _uiState.update {
                    it.copy(
                        isLoadingDetail = false,
//                        attendanceDetail = getAttendanceDetailDummy()
                    )
                }
                return@launch
            }
            getAttendanceByIdUseCase(id).collectLatest { result ->
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
}