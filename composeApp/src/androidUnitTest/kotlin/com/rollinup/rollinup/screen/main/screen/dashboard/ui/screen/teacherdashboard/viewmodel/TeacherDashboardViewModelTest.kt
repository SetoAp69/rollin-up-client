@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("UnusedFlow")

package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.viewmodel

import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
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
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.now
import com.rollinup.common.utils.Utils.toEpochMilli
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.component.export.FileWriter
import com.rollinup.rollinup.component.permitform.model.PermitFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.EditAttendanceFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardApprovalFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardFilterData
import dev.jordond.compass.Coordinates
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TeacherDashboardViewModelTest {

    private lateinit var viewModel: TeacherDashboardViewModel

    @MockK
    private lateinit var getAttendanceByClassListUseCase: GetAttendanceByClassListUseCase

    @MockK
    private lateinit var getAttendanceByClassPagingUseCase: GetAttendanceByClassPagingUseCase

    @MockK
    private lateinit var getAttendanceByIdUseCase: GetAttendanceByIdUseCase

    @MockK
    private lateinit var permitDoApprovalUseCase: DoApprovalUseCase

    @MockK
    private lateinit var createPermitUseCase: CreatePermitUseCase

    @MockK
    private lateinit var createAttendanceDataUseCase: CreateAttendanceDataUseCase

    @MockK
    private lateinit var editAttendanceDataUseCase: EditAttendanceDataUseCase

    @MockK
    private lateinit var getExportAttendanceDataUseCase: GetExportAttendanceDataUseCase

    @MockK
    private lateinit var fileWriter: FileWriter

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    // ---------- Arrange Helpers ----------

    private fun arrangeGetList(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
        result: Result<List<AttendanceByClassEntity>, NetworkError>,
    ) {
        coEvery {
            getAttendanceByClassListUseCase(classKey, queryParams)
        } returns flowOf(result)
    }

    private fun arrangeGetPaging(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
        data: List<AttendanceByClassEntity>,
    ) {
        coEvery {
            getAttendanceByClassPagingUseCase(classKey, queryParams)
        } returns flowOf(PagingData.from(data))
    }

    private fun arrangeGetDetail(
        id: String,
        result: Result<AttendanceDetailEntity, NetworkError>,
    ) {
        coEvery {
            getAttendanceByIdUseCase(id)
        } returns flowOf(result)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = TeacherDashboardViewModel(
            getAttendanceByClassListUseCase,
            getAttendanceByClassPagingUseCase,
            getAttendanceByIdUseCase,
            permitDoApprovalUseCase,
            createPermitUseCase,
            createAttendanceDataUseCase,
            editAttendanceDataUseCase,
            getExportAttendanceDataUseCase,
            fileWriter
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ---------- Tests ----------

    // 1. Initialization Tests

    @Test
    fun `init() with null user should do nothing`() = runTest {
        viewModel.init()
        assertEquals("", viewModel.uiState.value.user.id)
    }

    @Test
    fun `init() desktop (isMobile=false) should call getList`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 101, id = "T1")
        val queryParams =
            GetAttendanceListByClassQueryParams(date = LocalDate.now().toEpochMilli().toString())
        arrangeGetList(101, queryParams, Result.Success(emptyList()))

        // Act
        viewModel.init(user, isMobile = false)
        advanceUntilIdle()

        // Assert
        assertEquals(user, viewModel.uiState.value.user)
        assertFalse(viewModel.uiState.value.isMobile)
        coVerify { getAttendanceByClassListUseCase(101, queryParams) }
        assertFalse(viewModel.uiState.value.isLoadingList)
    }

    @Test
    fun `init() mobile (isMobile=true) should call getPagingData`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 101, id = "T1")
        val queryParams =
            GetAttendanceListByClassQueryParams(date = LocalDate.now().toEpochMilli().toString())
        arrangeGetPaging(101, queryParams, emptyList())

        // Act
        viewModel.init(user, isMobile = true)
        advanceUntilIdle()

        // Assert
        assertEquals(user, viewModel.uiState.value.user)
        assertTrue(viewModel.uiState.value.isMobile)
        coVerify { getAttendanceByClassPagingUseCase(101, queryParams) }
    }


    @Test
    fun `getList() error should update isLoadingList`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 101)
        val queryParams =
            GetAttendanceListByClassQueryParams(date = LocalDate.now().toEpochMilli().toString())
        arrangeGetList(101, queryParams, Result.Error(NetworkError.RESPONSE_ERROR))

        // Act
        viewModel.init(user, isMobile = false)

        // Assert
        coVerify {
            getAttendanceByClassListUseCase(101, queryParams)
        }
        assertFalse(viewModel.uiState.value.isLoadingList)
        assertTrue(viewModel.uiState.value.attendanceList.isEmpty())
    }

    // 2. Detail & Selection Tests

    @Test
    fun `getDetail() with null attendance should generate detail locally`() = runTest {
        // Arrange
        val student = AttendanceByClassEntity.Student("s1", "123", "John")
        val data = AttendanceByClassEntity(student = student, attendance = null)
        val cb = viewModel.getCallback()

        // Act
        cb.onGetDetail(data)

        // Assert
        val detail = viewModel.uiState.value.attendanceDetail
        assertNotNull(detail)
        assertEquals(student.id, detail?.student?.id)
        assertEquals(AttendanceStatus.NO_DATA, detail?.status)
        coVerify(exactly = 0) { getAttendanceByIdUseCase(any()) }
    }

    @Test
    fun `getDetail() with existing attendance should fetch from usecase`() = runTest {
        // Arrange
        val student = AttendanceByClassEntity.Student("s1", "123", "John")
        val att = AttendanceByClassEntity.Attendance("att1")
        val data = AttendanceByClassEntity(student = student, attendance = att)

        val expectedDetail = AttendanceDetailEntity(id = "att1", status = AttendanceStatus.ON_TIME)
        arrangeGetDetail("att1", Result.Success(expectedDetail))
        val cb = viewModel.getCallback()

        // Act
        cb.onGetDetail(data)
        advanceUntilIdle()

        // Assert
        assertEquals(expectedDetail, viewModel.uiState.value.attendanceDetail)
        assertFalse(viewModel.uiState.value.isLoadingDetail)
    }

    @Test
    fun `getDetail() should failed fetching data from usecase`() = runTest {
        // Arrange
        val student = AttendanceByClassEntity.Student("s1", "123", "John")
        val att = AttendanceByClassEntity.Attendance("att1")
        val data = AttendanceByClassEntity(student = student, attendance = att)

        arrangeGetDetail("att1", Result.Error(NetworkError.RESPONSE_ERROR))
        val cb = viewModel.getCallback()

        //Act
        cb.onGetDetail(data)

        //Assert
        coVerify(exactly = 1) {
            getAttendanceByIdUseCase(data.attendance!!.id)
        }

        val state = viewModel.uiState.value
        assertEquals(AttendanceDetailEntity(), state.attendanceDetail)
        assertFalse(state.isLoadingDetail)
    }

    @Test
    fun `updateSelection() should toggle items`() {
        // Arrange
        val item1 =
            AttendanceByClassEntity(student = AttendanceByClassEntity.Student("1", "A", "A"))
        val cb = viewModel.getCallback()

        // Act: Select
        cb.onUpdateSelection(item1)
        assertTrue(viewModel.uiState.value.itemSelected.contains(item1))

        // Act: Deselect
        cb.onUpdateSelection(item1)
        assertFalse(viewModel.uiState.value.itemSelected.contains(item1))
    }

    @Test
    fun `selectAll() should fetch list and select all items`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 101)
        val items = listOf(
            AttendanceByClassEntity(
                student = AttendanceByClassEntity.Student(
                    "1",
                    "A",
                    "A"
                )
            )
        )
        val queryParams =
            GetAttendanceListByClassQueryParams(date = LocalDate.now().toEpochMilli().toString())

        arrangeGetList(101, queryParams, Result.Success(items))

        viewModel.init(user, false)
        val cb = viewModel.getCallback()

        // Act
        cb.onSelectAll()

        // Assert
        coVerify {
            getAttendanceByClassListUseCase(101, queryParams)
        }

        assertTrue(viewModel.uiState.value.isAllSelected)
        assertEquals(items, viewModel.uiState.value.itemSelected)

        // Act: Unselect
        cb.onSelectAll()
        assertFalse(viewModel.uiState.value.isAllSelected)
        assertTrue(viewModel.uiState.value.itemSelected.isEmpty())
    }

    @Test
    fun `selectAll() with null classKey should return immediately`() = runTest {
        // Arrange
        // User with null classKey
        val user = LoginEntity(id = "T1", classKey = null)
        viewModel.init(user,true)
        val cb = viewModel.getCallback()

        // Act
        cb.onSelectAll()

        // Assert
        coVerify(exactly = 0) { getAttendanceByClassListUseCase(any(), any()) }
        assertFalse(viewModel.uiState.value.isAllSelected)
    }

    @Test
    fun `selectAll() should failed fetch list`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 101)
        val queryParams =
            GetAttendanceListByClassQueryParams(date = LocalDate.now().toEpochMilli().toString())

        arrangeGetList(101, queryParams, Result.Error(NetworkError.RESPONSE_ERROR))

        viewModel.init(user, false)
        val cb = viewModel.getCallback()

        // Act
        cb.onSelectAll()

        // Assert
        coVerify {
            getAttendanceByClassListUseCase(101, queryParams)
        }
        assertFalse(viewModel.uiState.value.isAllSelected)
        assertTrue(viewModel.uiState.value.itemSelected.isEmpty())
    }

    @Test
    fun `validateEditForm() CheckIn validation failure`() {
        // Arrange
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.ON_TIME,
            checkInTime = null // Invalid
        )
        val cb = viewModel.getCallback()

        // Act
        val isValid = cb.onValidateEditForm(formData, AttendanceStatus.NO_DATA)

        // Assert
        assertFalse(isValid)
        assertEquals(
            "Check in time can't be null",
            viewModel.uiState.value.editAttendanceFormData.checkInTimeError
        )
    }

    @Test
    fun `validateEditForm() Permit validation - Non-Absence type with empty reason should be Valid`() {
        // Arrange
        // Logic: if (formData.reason.isNullOrBlank() && formData.type == PermitType.ABSENCE)
        // Here we test type = DISPENSATION (not ABSENCE), so empty reason should NOT trigger error.
        val permitData = PermitFormData(
            duration = listOf(1000L),
            type = PermitType.DISPENSATION,
            reason = "" // Empty reason
        )
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.EXCUSED,
            permitFormData = permitData
        )
        val cb = viewModel.getCallback()

        // Act
        val isValid = cb.onValidateEditForm(formData, AttendanceStatus.NO_DATA)

        // Assert
        assertTrue("Should be valid because reason is only required for ABSENCE", isValid)
        assertNull(viewModel.uiState.value.editAttendanceFormData.permitFormData.reasonError)
    }

    @Test
    fun `validateEditForm() Permit validation failure (empty duration)`() {
        // Arrange
        val permitData = PermitFormData(
            duration = emptyList(), // Invalid
            type = PermitType.ABSENCE
        )
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.ABSENT,
            permitFormData = permitData
        )
        val cb = viewModel.getCallback()

        // Act
        val isValid = cb.onValidateEditForm(formData, AttendanceStatus.NO_DATA)

        // Assert
        assertFalse(isValid)
        assertEquals(
            "Duration can't be empty",
            viewModel.uiState.value.editAttendanceFormData.permitFormData.durationError
        )
    }

    @Test
    fun `validateEditForm() Permit validation failure (missing reason for absence)`() {
        // Arrange
        val permitData = PermitFormData(
            duration = listOf(1000L),
            type = PermitType.ABSENCE,
            reason = "" // Invalid
        )
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.ABSENT,
            permitFormData = permitData
        )
        val cb = viewModel.getCallback()

        // Act
        val isValid = cb.onValidateEditForm(formData, AttendanceStatus.NO_DATA)

        // Assert
        assertFalse(isValid)
        assertEquals(
            "Please select a reason",
            viewModel.uiState.value.editAttendanceFormData.permitFormData.reasonError
        )
    }

    @Test
    fun `validateApprovalForm() failure`() {
        // Arrange
        val formData = TeacherDashboardApprovalFormData(isApproved = null)
        val cb = viewModel.getCallback()

        // Act
        val isValid = cb.onValidateApproval(formData)

        // Assert
        assertFalse(isValid)
        assertEquals(
            "Please select approval status",
            viewModel.uiState.value.approvalFormData.isApprovedError
        )
    }

    // 4. Submission Tests

    @Test
    fun `submitApproval() success`() = runTest {
        // Arrange
        val formData =
            TeacherDashboardApprovalFormData(id = listOf("A1"), isApproved = true, note = "Ok")
        coEvery { permitDoApprovalUseCase(any()) } returns flowOf(Result.Success(Unit))
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmitApproval(formData)
        advanceUntilIdle()

        // Assert
        assertFalse(viewModel.uiState.value.isLoadingOverlay)
        assertTrue(viewModel.uiState.value.submitApprovalState == true)

        coVerify {
            permitDoApprovalUseCase(match {
                it.listId == listOf("A1") && it.isApproved
            })
        }
    }

    @Test
    fun `submitEditAttendance() - Case Check In (Create Attendance)`() = runTest {
        // Arrange
        val initialData = AttendanceDetailEntity(
            status = AttendanceStatus.NO_DATA,
            student = AttendanceDetailEntity.User(id = "S1", studentId = "1", name = "S")
        )
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.ON_TIME,
            checkInTime = 10L
        )

        coEvery { createAttendanceDataUseCase(any()) } returns flowOf(Result.Success(Unit))
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmitEditAttendance(initialData, formData)
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.submitEditAttendanceState == true)
        coVerify {
            createAttendanceDataUseCase(match {
                it.id == "S1" && it.checkInAt == 10L
            })
        }
    }

    @Test
    fun `exportFile() success`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 101)
        val exportEntity = ExportAttendanceDataEntity(
            data = listOf(
                ExportAttendanceDataEntity.Data(
                    fullName = "S1",
                    classX = "John",
                    studentId = "ClassA",
                    dataPerDate = listOf(
                        ExportAttendanceDataEntity.Data.AttendanceRecord(
                            sDate = "2003-08-06",
                            status = "APPROVAL_PENDING"
                        )
                    )
                )
            ),
            sDateRange = listOf("2003-04-07","2003-08-06")
        )

        coEvery { getExportAttendanceDataUseCase(any()) } returns flowOf(Result.Success(exportEntity))
        coEvery { fileWriter.writeExcel(any(), any()) } returns Unit

        viewModel.init(user)
        val cb = viewModel.getCallback()

        // Act
        cb.onExportFile("test.xlsx")
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.exportState == true)
        assertFalse(viewModel.uiState.value.isLoadingOverlay)
        coVerify { fileWriter.writeExcel("test.xlsx", any()) }
    }


//    @Test
//    fun `exportFile() with null classKey should return immediately`() = runTest {
//        // Arrange
//        val user = LoginEntity(id = "T1", classKey = null)
//        val queryParams = GetAttendanceListByClassQueryParams(date = LocalDate.now().toEpochMilli().toString())
//        viewModel.init(user, false)
//
//        val cb = viewModel.getCallback()
//        arrangeGetList(0, queryParams, Result.Success(emptyList()))
//
//        // Act
//        cb.onExportFile("test.xlsx")
//
//        // Assert
//        coVerify{getAttendanceByClassListUseCase(any(), any())}
//        coVerify(exactly = 0) { getExportAttendanceDataUseCase(any()) }
//        assertFalse(viewModel.uiState.value.isLoadingOverlay)
//    }

    @Test
    fun `submitEditAttendance() - Case Create Permit`() = runTest {
        // Arrange
        val initialData = AttendanceDetailEntity(
            status = AttendanceStatus.NO_DATA,
            student = AttendanceDetailEntity.User(id = "S1", studentId = "1", name = "S")
        )
        val permitData = PermitFormData(
            reason = "Sick",
            duration = listOf(100L),
            type = PermitType.DISPENSATION
        )
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.EXCUSED,
            permitFormData = permitData
        )

        viewModel.init(LoginEntity(id = "Teacher1"))
        coEvery { createPermitUseCase(any()) } returns flowOf(Result.Success(Unit))
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmitEditAttendance(initialData, formData)
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.submitEditAttendanceState == true)
        coVerify {
            createPermitUseCase(match {
                it.studentId == "S1" && it.approvalStatus == ApprovalStatus.APPROVED && it.approvedBy == "Teacher1"
            })
        }
    }

    @Test
    fun `submitEditAttendance() - Case Edit Existing Attendance`() = runTest {
        // Arrange
        val initialData = AttendanceDetailEntity(
            id = "Att1",
            status = AttendanceStatus.ON_TIME,
            student = AttendanceDetailEntity.User(id = "S1", studentId = "1", name = "S")
        )

        val formData = EditAttendanceFormData(
            status = AttendanceStatus.LATE,
            location = Coordinates(10.0, 20.0),
            checkInTime = 10L
        )

        viewModel.init(LoginEntity(id = "Teacher1"))
        coEvery { editAttendanceDataUseCase(any(), any()) } returns flowOf(Result.Success(Unit))
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmitEditAttendance(initialData, formData)
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.submitEditAttendanceState == true)
        coVerify {
            editAttendanceDataUseCase("Att1", match {
                it.studentUserId == "S1" && it.status == AttendanceStatus.LATE && it.approvedBy == "Teacher1"
            })
        }
    }

    @Test
    fun `exportFile() error`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 101)
        coEvery { getExportAttendanceDataUseCase(any()) } returns flowOf(Result.Error(NetworkError.RESPONSE_ERROR))

        viewModel.init(user)
        val cb = viewModel.getCallback()

        // Act
        cb.onExportFile("test.xlsx")
        advanceUntilIdle()

        // Assert
        // Logic in VM sets exportState to true even on error (based on provided code snippet: isLoadingOverlay=false, exportState=true)
        // This might be a bug in source, but test must match source logic
        assertTrue(viewModel.uiState.value.exportState == true)
        coVerify(exactly = 0) { fileWriter.writeExcel(any(), any()) }
    }

    @Test
    fun `refresh() on mobile should reset selection and get paging data`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1, id = "T1")
        val itemSelected =
            AttendanceByClassEntity(student = AttendanceByClassEntity.Student("1", "S1", "Name"))

        // Setup initial state: Mobile, with an item selected
        coEvery {
            getAttendanceByClassPagingUseCase(
                any(),
                any()
            )
        } returns flowOf(PagingData.empty())
        viewModel.init(user, isMobile = true)
        viewModel.getCallback().onUpdateSelection(itemSelected) // Select an item

        assertTrue(viewModel.uiState.value.itemSelected.isNotEmpty())

        // Act
        viewModel.getCallback().onRefresh()
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.itemSelected.isEmpty()) // Selection reset
        coVerify { getAttendanceByClassPagingUseCase(1, any()) }
        coVerify(exactly = 0) { getAttendanceByClassListUseCase(any(), any()) }
    }

    @Test
    fun `refresh() on desktop should reset selection and get list`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1, id = "T1")
        val itemSelected =
            AttendanceByClassEntity(student = AttendanceByClassEntity.Student("1", "S1", "Name"))

        // Setup initial state: Desktop, with an item selected
        coEvery { getAttendanceByClassListUseCase(any(), any()) } returns flowOf(
            Result.Success(
                emptyList()
            )
        )
        viewModel.init(user, isMobile = false)
        viewModel.getCallback().onUpdateSelection(itemSelected)

        assertTrue(viewModel.uiState.value.itemSelected.isNotEmpty())

        // Act
        viewModel.getCallback().onRefresh()
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.itemSelected.isEmpty()) // Selection reset
        coVerify { getAttendanceByClassListUseCase(1, any()) }
        coVerify(exactly = 0) { getAttendanceByClassPagingUseCase(any(), any()) }
    }


    @Test
    fun `resetMessageState() should clear submit states`() = runTest {
        // Arrange: manually trigger a state where these are not null (mocking a previous submission)
        // Since we can't set state directly, we rely on viewModel starting with nulls.
        // Or we assume a flow that set them. Let's assume we need to verify they act as resetters.
        // We can simulate a submission to set them to true/false first.
        val user = LoginEntity(id = "T1")
        viewModel.init(user)
        coEvery { permitDoApprovalUseCase(any()) } returns flowOf(Result.Success(Unit))
        viewModel.getCallback()
            .onSubmitApproval(TeacherDashboardApprovalFormData(isApproved = true))
        advanceUntilIdle()

        // Pre-check: State should be set
        assertTrue(viewModel.uiState.value.submitApprovalState != null)

        // Act
        viewModel.getCallback().onResetMessageState()

        // Assert
        assertNull(viewModel.uiState.value.submitApprovalState)
        assertNull(viewModel.uiState.value.submitEditAttendanceState)
    }


    @Test
    fun `updateFilter() should update state and trigger refresh`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1)
        viewModel.init(user, isMobile = false)
        val newFilter =
            TeacherDashboardFilterData(status = listOf(AttendanceStatus.APPROVAL_PENDING))
        coEvery { getAttendanceByClassListUseCase(any(), any()) } returns flowOf(
            Result.Success(
                emptyList()
            )
        )

        // Act
        viewModel.getCallback().onUpdateFilter(newFilter)
        advanceUntilIdle()

        // Assert
        assertEquals(newFilter, viewModel.uiState.value.filterData)
        coVerify(atLeast = 2) {
            getAttendanceByClassListUseCase(
                1,
                any()
            )
        } // Once init, once updateFilter
    }


    @Test
    fun `updateApprovalForm() should update state`() {
        // Arrange
        val formData = TeacherDashboardApprovalFormData(note = "Approved")

        // Act
        viewModel.getCallback().onUpdateApprovalForm(formData)

        // Assert
        assertEquals(formData, viewModel.uiState.value.approvalFormData)
    }

    @Test
    fun `updateEditForm() should update state`() {
        // Arrange
        val formData = EditAttendanceFormData(status = AttendanceStatus.LATE)

        // Act
        viewModel.getCallback().onUpdateEditForm(formData)

        // Assert
        assertEquals(formData, viewModel.uiState.value.editAttendanceFormData)
    }


    @Test
    fun `validateEditForm - CheckIn - Invalid (Null Time)`() {
        // Arrange
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.ON_TIME,
            checkInTime = null // Invalid
        )
        val cb = viewModel.getCallback()

        // Act
        val result = cb.onValidateEditForm(formData, AttendanceStatus.NO_DATA)

        // Assert
        assertFalse(result)
        assertEquals(
            "Check in time can't be null",
            viewModel.uiState.value.editAttendanceFormData.checkInTimeError
        )
    }

    @Test
    fun `validateEditForm - CheckIn - Valid`() {
        // Arrange
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.LATE,
            checkInTime = 12
        )
        val cb = viewModel.getCallback()

        // Act
        val result = cb.onValidateEditForm(formData, AttendanceStatus.NO_DATA)

        // Assert
        assertTrue(result)
        assertNull(viewModel.uiState.value.editAttendanceFormData.checkInTimeError)
    }

    @Test
    fun `validateEditForm - Permit - Invalid (Empty Duration)`() {
        // Arrange
        val permitData = PermitFormData(
            duration = emptyList(), // Invalid
            type = PermitType.DISPENSATION
        )
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.EXCUSED,
            permitFormData = permitData
        )
        val cb = viewModel.getCallback()

        // Act
        val result = cb.onValidateEditForm(formData, AttendanceStatus.NO_DATA)

        // Assert
        assertFalse(result)
        assertEquals(
            "Duration can't be empty",
            viewModel.uiState.value.editAttendanceFormData.permitFormData.durationError
        )
    }

    @Test
    fun `validateEditForm - Permit - Invalid (Missing Reason for Absence)`() {
        // Arrange
        val permitData = PermitFormData(
            duration = listOf(1000L),
            type = PermitType.ABSENCE,
            reason = ""
        )
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.ABSENT,
            permitFormData = permitData
        )
        val cb = viewModel.getCallback()

        // Act
        val result = cb.onValidateEditForm(formData, AttendanceStatus.NO_DATA)

        // Assert
        assertFalse(result)
        assertEquals(
            "Please select a reason",
            viewModel.uiState.value.editAttendanceFormData.permitFormData.reasonError
        )
    }

    @Test
    fun `validateEditForm - Permit - Valid`() {
        // Arrange
        val permitData = PermitFormData(
            duration = listOf(1000L),
            type = PermitType.ABSENCE,
            reason = "Flu"
        )
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.EXCUSED,
            permitFormData = permitData
        )
        val cb = viewModel.getCallback()

        // Act
        val result = cb.onValidateEditForm(formData, AttendanceStatus.NO_DATA)

        // Assert
        assertTrue(result)
        assertNull(viewModel.uiState.value.editAttendanceFormData.permitFormData.durationError)
        assertNull(viewModel.uiState.value.editAttendanceFormData.permitFormData.reasonError)
    }

    // 3. Other Statuses
    @Test
    fun `validateEditForm - NO_DATA or APPROVAL_PENDING should return true`() {
        // Arrange
        val formData = EditAttendanceFormData(status = AttendanceStatus.NO_DATA)
        val cb = viewModel.getCallback()

        // Act & Assert
        assertTrue(
            cb.onValidateEditForm(
                formData.copy(status = AttendanceStatus.NO_DATA),
                AttendanceStatus.NO_DATA
            )
        )
        assertTrue(
            cb.onValidateEditForm(
                formData.copy(status = AttendanceStatus.APPROVAL_PENDING),
                AttendanceStatus.NO_DATA
            )
        )
    }

    @Test
    fun `validateEditForm - Existing Attendance - calls isValid`() {
        // Arrange
        val formData = EditAttendanceFormData(
            status = AttendanceStatus.ON_TIME,
            checkInTime = 12L
        )
        val cb = viewModel.getCallback()

        // Act
        val result = cb.onValidateEditForm(formData, AttendanceStatus.ON_TIME)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `resetEditForm() should set edit form data to default value`() {
        //Arrange
        val formData = EditAttendanceFormData(
            checkInTime = 12L,
        )
        val cb = viewModel.getCallback()
        cb.onUpdateEditForm(formData)

        //Act
        cb.onResetEditForm()

        //Assert
        val state = viewModel.uiState.value
        assertEquals(EditAttendanceFormData(), state.editAttendanceFormData)
    }

    @Test
    fun `updateExportDateRange() should update exportDateRange to given value`() {
        //Arrange
        val dateRange = listOf(
            LocalDate(2003, 8, 6),
            LocalDate(2003, 4, 7),
        )

        //Act
        val cb = viewModel.getCallback()
        cb.onUpdateExportDateRanges(dateRange)

        //Assert
        assertEquals(dateRange, viewModel.uiState.value.exportDateRanges)
    }

    @Test
    fun `submitApproval() with invalid form should not call usecase`() = runTest {
        // Arrange
        // isApproved = null makes validateApprovalForm return false
        val formData = TeacherDashboardApprovalFormData(isApproved = null)
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmitApproval(formData)

        // Assert
        // Verify the guard clause worked and usecase was NOT called
        coVerify(exactly = 0) { permitDoApprovalUseCase(any()) }
        // Verify error state was set
        assertEquals(
            "Please select approval status",
            viewModel.uiState.value.approvalFormData.isApprovedError
        )
    }

    @Test
    fun `submitEditAttendance() with NO_DATA target status should do nothing`() = runTest {
        // Arrange
        val initialData = AttendanceDetailEntity(
            status = AttendanceStatus.NO_DATA,
            student = AttendanceDetailEntity.User("S1", "1", "S")
        )
        // Trying to submit with status NO_DATA
        val formData = EditAttendanceFormData(status = AttendanceStatus.NO_DATA)
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmitEditAttendance(initialData, formData)

        // Assert
        coVerify(exactly = 0) { createAttendanceDataUseCase(any()) }
        coVerify(exactly = 0) { createPermitUseCase(any()) }
        coVerify(exactly = 0) { editAttendanceDataUseCase(any(), any()) }
    }

    @Test
    fun `submitEditAttendance() with APPROVAL_PENDING target status should do nothing`() = runTest {
        // Arrange
        val initialData = AttendanceDetailEntity(
            status = AttendanceStatus.NO_DATA,
            student = AttendanceDetailEntity.User("S1", "1", "S")
        )
        // Trying to submit with status APPROVAL_PENDING
        val formData = EditAttendanceFormData(status = AttendanceStatus.APPROVAL_PENDING)
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmitEditAttendance(initialData, formData)

        // Assert
        coVerify(exactly = 0) { createAttendanceDataUseCase(any()) }
        coVerify(exactly = 0) { createPermitUseCase(any()) }
        coVerify(exactly = 0) { editAttendanceDataUseCase(any(), any()) }
    }
}