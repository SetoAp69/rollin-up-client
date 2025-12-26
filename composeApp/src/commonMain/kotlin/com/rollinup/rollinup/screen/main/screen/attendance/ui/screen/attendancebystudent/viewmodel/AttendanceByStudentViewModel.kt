package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentSummaryUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.component.date.DateFormatter
import com.rollinup.rollinup.component.export.FileWriter
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent.AttendanceByStudentCallback
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent.AttendanceByStudentFilterData
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone

class AttendanceByStudentViewModel(
    private val getAttendanceByStudentListUseCase: GetAttendanceByStudentListUseCase,
    private val getAttendanceByStudentPagingUseCase: GetAttendanceByStudentPagingUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getAttendanceByStudentSummaryUseCase: GetAttendanceByStudentSummaryUseCase,
    private val getAttendanceByIdUseCase: GetAttendanceByIdUseCase,
    private val fileWriter: FileWriter,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AttendanceByStudentUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData =
        MutableStateFlow<PagingData<AttendanceByStudentEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    fun init(studentUserId: String, isMobile: Boolean) {
        if (studentUserId.isBlank()) return

        _uiState.update {
            it.copy(studentUserId = studentUserId, isMobile = isMobile)
        }

        if (isMobile) {
            getPaging()
        } else {
            getItemList()
        }
        getProfile()
        getSummary()
    }

    fun reset() {
        _uiState.update { AttendanceByStudentUiState() }
    }

    fun getCallback() = AttendanceByStudentCallback(
        onRefresh = ::refresh,
        onGetDetail = ::getDetail,
        onUpdateFilter = ::updateFilter,
        onExportFile = ::exportFile,
        onResetMessageState = ::resetMessageState
    )

    private fun refresh() {
        if (uiState.value.isMobile) {
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
            getUserByIdUseCase(studentUserId).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingProfile = false,
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

    private fun updateFilter(filterData: AttendanceByStudentFilterData) {
        _uiState.update { it.copy(filterData = filterData) }
        refresh()
    }

    private fun getSummary() {
        val studentUserId = _uiState.value.studentUserId
        val dateRange = _uiState.value.filterData.dateRange
        _uiState.update { it.copy(isLoadingSummary = true) }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSummary = false) }
            getAttendanceByStudentSummaryUseCase(studentUserId, dateRange).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingSummary = false,
                                summary = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoadingSummary = false) }
                    }
                }
            }
        }
    }

    private fun getPaging() {
        val query = uiState.value.queryParams
        val studentUserId = _uiState.value.studentUserId

        viewModelScope.launch {
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

    private fun exportFile(fileName: String) {
        _uiState.update { it.copy(isLoadingOverlay = true, exportState = null) }
        val id = _uiState.value.studentUserId
        val queryParams = _uiState.value.queryParams

        viewModelScope.launch {
            getAttendanceByStudentListUseCase(id, queryParams).collectLatest { result ->
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
            }
        }
    }

    private fun fetchExportData(data: List<AttendanceByStudentEntity>): List<Pair<String, List<*>>> {
        return listOf(
            "Date" to data.map { d -> DateFormatter.formatDateShort(d.localDate, true) },
            "Status" to data.map { d -> d.status.label },
            "Check in at" to data.map { d ->
                d.checkInTime?.let {
                    DateFormatter.formateDateTimeFromString(
                        dateTime = it,
                        timeZone = TimeZone.UTC
                    )
                } ?: "-"
            },
            "Duration" to data.map { d ->
                d.permit?.let {
                    DateFormatter.formatPermitDateRange(
                        type = it.type,
                        start = it.start,
                        end = it.end
                    )
                } ?: "-"
            },
            "Reason" to data.map { d ->
                d.permit?.reason ?: "-"
            }
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