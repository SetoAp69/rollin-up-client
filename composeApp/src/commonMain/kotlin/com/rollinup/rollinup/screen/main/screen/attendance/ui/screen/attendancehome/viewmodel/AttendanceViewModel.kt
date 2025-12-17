package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetExportAttendanceDataQueryParams
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassSummaryUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetExportAttendanceDataUseCase
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.ExportAttendanceDataEntity
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.rollinup.component.export.FileWriter
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceCallback
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceFilterData
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class AttendanceViewModel(
    private val getAttendanceByClassListUseCase: GetAttendanceByClassListUseCase,
    private val getAttendanceByIdUseCase: GetAttendanceByIdUseCase,
    private val getAttendanceByClassSummaryUseCase: GetAttendanceByClassSummaryUseCase,
    private val getAttendanceByClassPagingUseCase: GetAttendanceByClassPagingUseCase,
    private val getExportAttendanceDataUseCase: GetExportAttendanceDataUseCase,
    private val fileWriter: FileWriter,
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
        if (isMobile) {
            getPagingData()
        } else {
            getAttendanceList()
        }
        getSummary()
    }

    fun getCallback() = AttendanceCallback(
        onRefresh = ::refresh,
        onSearch = ::search,
        onFilter = ::filter,
        onGetDetail = ::getDetail,
        onExportFile = ::exportFile,
        onUpdateExportDateRange = ::updateExportDateRange,
        onResetMessageState = ::resetMessageState
    )

    private fun getAttendanceList() {
        val queryParams = _uiState.value.queryParams
        val key = _uiState.value.user.classKey ?: return

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
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
            getAttendanceByClassPagingUseCase(key, queryParams).collectLatest {
                _pagingData.value = it
            }
        }
    }

    private fun exportFile(fileName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingOverlay = true, exportState = null) }
            val classKey = _uiState.value.user.classKey
            val dateRange = _uiState.value.exportDateRanges
            val query = GetExportAttendanceDataQueryParams(classKey, dateRange.toJsonString())
            getExportAttendanceDataUseCase(query).collectLatest { result ->
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
                                exportState = true
                            )
                        }
                    }
                }
            }

        }
    }

    private fun getSummary() {
        _uiState.update { it.copy(isLoadingSummary = true) }
        val date = _uiState.value.filterData.date
        val classKey = _uiState.value.user.classKey ?: 0

        viewModelScope.launch {
            getAttendanceByClassSummaryUseCase(classKey, date).collectLatest { result ->
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
                        _uiState.update {
                            it.copy(
                                isLoadingSummary = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getDetail(data: AttendanceByClassEntity) {
        if (data.attendance?.id == null) {
            _uiState.update { it.copy(attendanceDetail = generateBlankDetail(data)) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetail = true) }
            getAttendanceByIdUseCase(data.attendance!!.id).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingDetail = false,
                                attendanceDetail = result.data,
                                exportDateRanges = emptyList()
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingDetail = false,
                                exportDateRanges = emptyList()
                            )
                        }
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

    private fun updateExportDateRange(dateRange: List<LocalDate>) {
        _uiState.update {
            it.copy(
                exportDateRanges = dateRange
            )
        }
    }

    private fun fetchExportData(data: ExportAttendanceDataEntity): List<Pair<String, List<*>>> {
        val mappedData = listOf(
            "Id" to data.data.map { it.studentId },
            "Name" to data.data.map { it.fullName },
            "Class" to data.data.map { it.classX }
        )
        val attendancePerDate = mapAttendanceDataByDate(data)

        return mappedData + attendancePerDate
    }

    private fun mapAttendanceDataByDate(data: ExportAttendanceDataEntity): List<Pair<String, List<String>>> {
        val dateRange = data.dateRange
        if (dateRange.isEmpty()) return emptyList()

        val studentAttendanceMap =
            data.data.associate { student ->
                student.studentId to student.dataPerDate.associate {
                    it.date to it.status
                }
            }

        return (dateRange.first()..dateRange.last()).map { date ->
            val statusPerDate = data.data.map { student ->
                studentAttendanceMap[student.studentId]?.get(date) ?: "-"
            }
            date.toString() to statusPerDate
        }
    }

    private fun resetMessageState() {
        _uiState.update { it.copy(exportState = null) }
    }
}
