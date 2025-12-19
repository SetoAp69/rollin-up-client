package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.data.source.network.model.request.attendance.CheckInBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.domain.attendance.CheckInUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentListUseCase
import com.rollinup.apiservice.domain.attendance.GetDashboardDataUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.common.utils.Utils.now
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard.StudentDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.uistate.StudentDashboardUiState
import dev.jordond.compass.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class StudentDashboardViewmodel(
    private val checkInUseCase: CheckInUseCase,
    private val getAttendanceListByStudentListUseCase: GetAttendanceByStudentListUseCase,
    private val getAttendanceByIdUseCase: GetAttendanceByIdUseCase,
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudentDashboardUiState())
    val uiState = _uiState.asStateFlow()

    fun getCallback() = StudentDashboardCallback(
        onRefresh = ::refresh,
        onShowAttendanceDetail = ::showAttendanceDetail,
        onUpdateLocation = ::updateLocation,
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
                        L.wtf {
                            result.data.map { "${it.date} - ${it.status}\n" }.toString()
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
        val date = LocalDate.now()

        viewModelScope.launch {
            getDashboardDataUseCase(id, date).collectLatest { result ->
                if (result is Result.Success) {
                    _uiState.update {
                        it.copy(
                            summary = result.data.summary,
                            currentStatus = result.data.attendanceStatus
                        )
                    }
                }
                _uiState.update { it.copy(isLoadingHeader = false) }
            }
        }
        L.w { _uiState.value.summary.toString() }
    }

    private fun refresh() {
        getSummary()
        getAttendanceList()
//                    _globalSetting.value = result.data
    }

    private fun showAttendanceDetail(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetail = true) }
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
        val body = CheckInBody(
            latitude = location.coordinates.latitude,
            longitude = location.coordinates.longitude,
            attachment = attachment,
            date = LocalDate.now().toString(),
            checkedInAt = LocalDateTime.now().toEpochMillis()
        )
        _uiState.update { it.copy(isLoadingOverlay = true) }
        viewModelScope.launch {
            checkInUseCase(body).collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                        checkInState = result is Result.Success
                    )
                }
            }
        }
    }

    private fun updateLocation(
        location: Location?,
        isValid: Boolean,
    ) {
        _uiState.update {
            it.copy(
                currentLocation = location,
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