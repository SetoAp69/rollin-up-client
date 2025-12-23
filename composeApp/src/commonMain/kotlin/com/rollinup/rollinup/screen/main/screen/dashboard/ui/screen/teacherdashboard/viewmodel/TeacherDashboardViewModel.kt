package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.EditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetExportAttendanceDataQueryParams
import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.domain.attendance.CreateAttendanceDataUseCase
import com.rollinup.apiservice.domain.attendance.EditAttendanceDataUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetExportAttendanceDataUseCase
import com.rollinup.apiservice.domain.permit.CreatePermitUseCase
import com.rollinup.apiservice.domain.permit.DoApprovalUseCase
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.ExportAttendanceDataEntity
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.common.utils.Utils.now
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.rollinup.component.export.FileWriter
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.dashboard.getAttendanceByClassDummy
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.EditAttendanceFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardApprovalFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardFilterData
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class TeacherDashboardViewModel(
    private val getAttendanceByClassListUseCase: GetAttendanceByClassListUseCase,
    private val getAttendanceByClassPagingUseCase: GetAttendanceByClassPagingUseCase,
    private val getAttendanceByIdUseCase: GetAttendanceByIdUseCase,
    private val permitDoApprovalUseCase: DoApprovalUseCase,
    private val createPermitUseCase: CreatePermitUseCase,
    private val createAttendanceDataUseCase: CreateAttendanceDataUseCase,
    private val editAttendanceDataUseCase: EditAttendanceDataUseCase,
    private val getExportAttendanceDataUseCase: GetExportAttendanceDataUseCase,
    private val fileWriter: FileWriter,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TeacherDashboardUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData =
        MutableStateFlow<PagingData<AttendanceByClassEntity>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    fun init(
        user: LoginEntity? = null,
        isMobile:Boolean = false
    ) {
        if (user == null) return
        _uiState.update {
            it.copy(user = user, isMobile = isMobile)
        }
        if (isMobile) {
            getPagingData()
        } else {
            getList()
        }
    }

    fun getCallback() = TeacherDashboardCallback(
        onRefresh = ::refresh,
        onResetMessageState = ::resetMessageState,
        onUpdateFilter = ::updateFilter,
        onUpdateApprovalForm = ::updateApprovalForm,
        onSubmitApproval = ::submitApproval,
        onUpdateSelection = ::updateSelection,
        onSelectAll = ::selectAll,
        onGetDetail = ::getDetail,
        onUpdateEditForm = ::updateEditForm,
        onValidateEditForm = ::validateEditForm,
        onSubmitEditAttendance = ::submitEditAttendance,
        onResetEditForm = ::resetEditForm,
        onResetSelection = ::resetSelection,
        onRefreshList = ::refresh,
        onValidateApproval = ::validateApprovalForm,
        onExportFile = ::exportFile,
        onUpdateExportDateRanges = ::updateExportDateRange,
    )

    //Fetch Data
    private fun getList() {
        _uiState.update {
            it.copy(isLoadingList = true)
        }
        val classKey = uiState.value.user.classKey ?: 0
        viewModelScope.launch {
            getAttendanceByClassListUseCase(
                classKey = classKey,
                queryParams = _uiState.value.filterData.toQueryParams()
            ).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                attendanceList = result.data,
                                isLoadingList = false
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingList = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getPagingData() {
        val classKey = uiState.value.user.classKey ?: 0

        viewModelScope.launch {
            _uiState.update { it.copy(itemSelected = emptyList()) }
            _pagingData.value = PagingData.from(
                data = getAttendanceByClassDummy(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true)
                )
            )

            getAttendanceByClassPagingUseCase(
                classKey = classKey,
                queryParams = _uiState.value.filterData.toQueryParams()
            ).collect { result ->
                _pagingData.value = result
            }
        }
    }

    private fun getDetail(data: AttendanceByClassEntity) {
        if (data.attendance == null) {
            _uiState.update {
                it.copy(
                    attendanceDetail = generateDetail(data),
                    isLoadingDetail = false
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetail = true) }
            getAttendanceByIdUseCase(data.attendance!!.id)
                .collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    attendanceDetail = result.data,
                                    isLoadingDetail = false
                                )
                            }
                        }

                        is Result.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoadingDetail = false
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun generateDetail(data: AttendanceByClassEntity) =
        AttendanceDetailEntity(
            id = "",
            student = AttendanceDetailEntity.User(
                id = data.student.id,
                studentId = data.student.studentId,
                name = data.student.name,
            ),
            status = AttendanceStatus.NO_DATA,
        )

    private fun refresh() {
        resetSelection()
        if (uiState.value.isMobile) {
            getPagingData()
        } else {
            getList()
        }
    }

    private fun resetMessageState() {
        _uiState.update {
            it.copy(
                submitApprovalState = null,
                submitEditAttendanceState = null
            )
        }
    }

    //Input update
    private fun updateFilter(filterData: TeacherDashboardFilterData) {
        _uiState.update { it.copy(filterData = filterData) }
        refresh()
    }

    private fun updateApprovalForm(formData: TeacherDashboardApprovalFormData) {
        _uiState.update {
            it.copy(
                approvalFormData = formData
            )
        }
    }

    private fun updateEditForm(formData: EditAttendanceFormData) {
        _uiState.update {
            it.copy(
                editAttendanceFormData = formData
            )
        }
    }

    //FORM VALIDATION
    private fun validateEditForm(
        formData: EditAttendanceFormData,
        initialStatus: AttendanceStatus,
    ): Boolean {
        return when (initialStatus) {
            AttendanceStatus.NO_DATA -> {
                when (formData.status) {
                    AttendanceStatus.ON_TIME, AttendanceStatus.LATE ->
                        validateCheckInForm(formData)

                    AttendanceStatus.EXCUSED, AttendanceStatus.ABSENT ->
                        validateCreatePermitForm(formData)

                    AttendanceStatus.NO_DATA -> true
                    AttendanceStatus.APPROVAL_PENDING -> true
                }
            }

            else -> {
                validateEditAttendanceForm(formData)
            }
        }
    }

    private fun validateEditAttendanceForm(formData: EditAttendanceFormData): Boolean {
        return formData.isValid()
    }

    private fun validateCreatePermitForm(formData: EditAttendanceFormData): Boolean {
        var formData = formData.permitFormData

        if ((formData.duration.isEmpty() || formData.duration.any { it == null }))
            formData = formData.copy(durationError = "Duration can't be empty")

        if (formData.reason.isNullOrBlank() && formData.type == PermitType.ABSENCE)
            formData = formData.copy(reasonError = "Please select a reason")

        _uiState.update {
            it.copy(
                editAttendanceFormData = it.editAttendanceFormData.copy(permitFormData = formData)
            )
        }
        return formData.isValid
    }

    private fun validateCheckInForm(formData: EditAttendanceFormData): Boolean {
        var formData = formData
        if (formData.checkInTime == null) {
            formData = formData.copy(checkInTimeError = "Check in time can't be null")
        }
        _uiState.update {
            it.copy(
                editAttendanceFormData = formData
            )
        }
        return formData.isValid()
    }

    private fun validateApprovalForm(formData: TeacherDashboardApprovalFormData): Boolean {
        var formData = formData
        if (formData.isApproved == null) {
            formData = formData.copy(isApprovedError = "Please select approval status")
        }

        _uiState.update {
            it.copy(
                approvalFormData = formData
            )
        }
        return formData.isApproved != null
    }

    //Data submission
    private fun submitApproval(formData: TeacherDashboardApprovalFormData) {
        if (!validateApprovalForm(formData)) return
        val body = PermitApprovalBody(
            listId = formData.id,
            approvalNote = formData.note,
            isApproved = formData.isApproved!!
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingOverlay = true) }
            permitDoApprovalUseCase(body).collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                        submitApprovalState = result is Result.Success
                    )
                }
            }

        }
    }

    private fun submitEditAttendance(
        initialData: AttendanceDetailEntity,
        formData: EditAttendanceFormData,
    ) {
        val studentId = initialData.student.id
        val formData = formData.copy(
            studentUserId = studentId
        )
        when (initialData.status) {
            AttendanceStatus.NO_DATA -> {
                when (formData.status) {
                    AttendanceStatus.LATE, AttendanceStatus.ON_TIME -> checkIn(formData)
                    AttendanceStatus.EXCUSED, AttendanceStatus.ABSENT -> createPermit(formData)
                    AttendanceStatus.NO_DATA, AttendanceStatus.APPROVAL_PENDING -> {
                        return
                    }
                }
            }

            else -> {
                editAttendance(initialData.id, formData)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun createPermit(formData: EditAttendanceFormData) {
        val permitFormData = formData.permitFormData
        val userId = _uiState.value.user.id
        val body = CreateEditPermitBody(
            studentId = formData.studentUserId,
            reason = permitFormData.reason,
            duration = permitFormData.duration.filter { it != null } as List<Long>,
            type = permitFormData.type,
            note = permitFormData.note,
            attachment = permitFormData.attachment,
            approvalStatus = ApprovalStatus.APPROVED,
            approvedBy = userId,
            approvedAt = LocalDateTime.now().toEpochMillis(),
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingOverlay = true) }
            createPermitUseCase(body).collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                        submitEditAttendanceState = result is Result.Success
                    )
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun editAttendance(id: String, formData: EditAttendanceFormData) {
        val user = _uiState.value.user
        val permitFormData = formData.permitFormData
        val body = EditAttendanceBody(
            studentUserId = formData.studentUserId,
            latitude = formData.location?.latitude,
            longitude = formData.location?.longitude,
            attachment = formData.permitFormData.attachment,
            status = formData.status,
            checkedInAt = formData.checkInTime,
            reason = permitFormData.reason,
            duration = permitFormData.duration.filter { it != null } as List<Long>,
            type = permitFormData.type,
            note = permitFormData.note,
            approvalStatus = ApprovalStatus.APPROVED,
            approvedBy = user.id,
            approvedAt = LocalDateTime.now().toEpochMillis(),
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingOverlay = true) }
            editAttendanceDataUseCase(id, body).collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                        submitEditAttendanceState = result is Result.Success
                    )
                }
            }
        }
    }

    private fun checkIn(
        formData: EditAttendanceFormData,
    ) {
        val body = CreateAttendanceBody(
            id = formData.studentUserId,
            date = LocalDate.now().toString(),
            checkInAt = formData.checkInTime!!
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingOverlay = true) }
            createAttendanceDataUseCase(body).collectLatest { result ->
                _uiState.update {
                    it.copy(
                        submitEditAttendanceState = result is Result.Success,
                        isLoadingOverlay = false
                    )
                }
            }
        }
    }

    //Selection
    private fun updateSelection(data: AttendanceByClassEntity) {
        val newData = _uiState.value.itemSelected.toMutableList()

        if (newData.contains(data)) {
            newData.remove(data)
        } else {
            newData.add(data)
        }

        _uiState.update {
            it.copy(itemSelected = newData)
        }
    }

    private fun selectAll() {
        if (_uiState.value.isAllSelected) {
            _uiState.update {
                it.copy(
                    isAllSelected = false,
                    itemSelected = emptyList()
                )
            }
        } else {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoadingOverlay = true) }

                val queryParams = _uiState.value.filterData.toQueryParams()
                val classKey = _uiState.value.user.classKey ?: return@launch
                getAttendanceByClassListUseCase(classKey, queryParams).collectLatest { result ->
                    _uiState.update { it.copy(isLoadingOverlay = true) }
                    when (result) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isAllSelected = true,
                                    isLoadingOverlay = false,
                                    itemSelected = result.data
                                )
                            }
                        }

                        is Result.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoadingOverlay = false,
                                )
                            }
                        }
                    }

                }
            }
        }
    }

    private fun resetSelection() {
        _uiState.update {
            it.copy(
                itemSelected = emptyList(),
                isAllSelected = false
            )
        }
    }

    private fun resetEditForm() {
        _uiState.update {
            it.copy(
                editAttendanceFormData = EditAttendanceFormData(),
            )
        }
        resetSelection()
    }

    private fun exportFile(fileName: String) {
        _uiState.update { it.copy(isLoadingOverlay = true) }
        val classKey = _uiState.value.user.classKey ?: return
        val dateRange = _uiState.value.exportDateRanges
        val queryPrams = GetExportAttendanceDataQueryParams(
            classKey = classKey,
            dateRange = dateRange.toJsonString()
        )
        viewModelScope.launch {
            getExportAttendanceDataUseCase(queryPrams).collectLatest { result ->
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


}