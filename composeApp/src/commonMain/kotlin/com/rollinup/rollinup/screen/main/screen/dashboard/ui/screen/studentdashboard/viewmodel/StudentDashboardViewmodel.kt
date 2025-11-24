package com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.Utils.toJsonString
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateEditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.domain.attendance.CreateAttendanceDataUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentListUseCase
import com.rollinup.apiservice.domain.attendance.GetDashboardDataUseCase
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.screen.dashboard.getAttendanceDetailDummy
import com.rollinup.rollinup.screen.dashboard.getAttendanceListDummy
import com.rollinup.rollinup.screen.dashboard.model.studentdashboard.StudentDashboardCallback
import com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.uistate.StudentDashboardUiState
import dev.jordond.compass.Location
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentDashboardViewmodel(
    private val createAttendanceDataUseCase: CreateAttendanceDataUseCase,
    private val getAttendanceListByStudentListUseCase: GetAttendanceByStudentListUseCase,
    private val getAttendanceByIdUseCase: GetAttendanceByIdUseCase,
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudentDashboardUiState())
    val uiState = _uiState.asStateFlow()

    fun getCallback() = StudentDashboardCallback(
        onRefresh = ::refresh,
        onShowAttendanceDetail = ::showAttendanceDetail,
        onUpdateLocationValid = ::updateLocationValid,
        onCheckIn = ::checkIn,
        onUpdateDateRangeSelected = ::updateDateRangeSelected,
        onUpdateLoginData = ::updateLoginData
    )

    fun init(userData: LoginEntity?) {
        if (userData == null) return

        _uiState.update {
            it.copy(
                user = userData
            )
        }
        getAttendanceList()
        getSummary()
    }

    private fun getAttendanceList() {
        val queryParams = GetAttendanceListByStudentQueryParams(
            dateRange = uiState.value.selectedDateRange.toJsonString()
        )
        val id = uiState.value.user?.id ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingCalendar = true
                )
            }
            delay(3000)
            _uiState.update {
                it.copy(
                    isLoadingCalendar = false,
                    attendanceList = getAttendanceListDummy()
                )
            }
            if (true) {
                return@launch
            }
            getAttendanceListByStudentListUseCase(
                id = id,
                queryParams = queryParams
            ).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingCalendar = false,
                                attendanceList = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingCalendar = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getSummary() {
        _uiState.update {
            it.copy(
                isLoadingHeader = true
            )
        }

        val id = _uiState.value.user?.id ?: return

        viewModelScope.launch {
            getDashboardDataUseCase(id).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingHeader = false,
                                summary = result.data.summary,
                                currentStatus = result.data.attendanceStatus
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoadingHeader = false)
                        }
                    }
                }
            }
        }
    }

    private fun refresh() {
        getSummary()
        getAttendanceList()
    }

    private fun showAttendanceDetail(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetail = true) }
            delay(3000)
            _uiState.update {
                it.copy(
                    isLoadingDetail = false,
                    attendanceDetail = getAttendanceDetailDummy()
                )
            }

            if (true) return@launch
            getAttendanceByIdUseCase(id).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingDetail = false,
                            )
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingDetail = false,
                                attendanceDetail = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkIn(attachment: MultiPlatformFile, location: Location) {
        val id = uiState.value.user?.id ?: return

        val body = CreateEditAttendanceBody(
            studentUserId = id,
            latitude = location.coordinates.latitude,
            longitude = location.coordinates.longitude,
            attachment = attachment,
            status = AttendanceStatus.CHECKED_IN,
        )

        viewModelScope.launch {
            createAttendanceDataUseCase(body).collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                        checkInState = result is Result.Success
                    )
                }
            }
        }
    }

    private fun updateLocationValid(isValid: Boolean) {
        _uiState.update {
            it.copy(
                isLocationValid = isValid
            )
        }
    }

    private fun updateDateRangeSelected(dateRange: List<Long>) {
        _uiState.update {
            it.copy(
                selectedDateRange = dateRange
            )
        }
    }

    private fun updateLoginData(loginData: LoginEntity) {
        _uiState.update {
            it.copy(
                user = loginData
            )
        }
    }

}