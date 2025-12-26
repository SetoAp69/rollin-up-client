@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("UnusedFlow")

package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.viewmodel

import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetExportAttendanceDataQueryParams
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassSummaryUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetExportAttendanceDataUseCase
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.attendance.ExportAttendanceDataEntity
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.common.utils.Utils.toEpochMilli
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.component.export.FileWriter
import com.rollinup.rollinup.component.utils.getCurrentDateAsList
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceFilterData
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

class AttendanceViewModelTest {

    private lateinit var viewModel: AttendanceViewModel

    @MockK
    private lateinit var getAttendanceByClassListUseCase: GetAttendanceByClassListUseCase

    @MockK
    private lateinit var getAttendanceByIdUseCase: GetAttendanceByIdUseCase

    @MockK
    private lateinit var getAttendanceByClassSummaryUseCase: GetAttendanceByClassSummaryUseCase

    @MockK
    private lateinit var getAttendanceByClassPagingUseCase: GetAttendanceByClassPagingUseCase

    @MockK
    private lateinit var getExportAttendanceDataUseCase: GetExportAttendanceDataUseCase

    @MockK
    private lateinit var fileWriter: FileWriter

    @get:Rule
    val coroutineRule = CoroutineTestRule() // UnconfinedTestDispatcher

    // ---------- Arrange Helpers ----------

    private fun arrangeAttendanceList(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
        result: Result<List<AttendanceByClassEntity>, NetworkError>,
    ) {
        coEvery {
            getAttendanceByClassListUseCase(classKey, queryParams)
        } returns flowOf(result)
    }

    private fun arrangeAttendancePaging(
        classKey: Int,
        queryParams: GetAttendanceListByClassQueryParams,
        pagingData: List<AttendanceByClassEntity>,
    ) {
        coEvery {
            getAttendanceByClassPagingUseCase(classKey, queryParams)
        } returns flowOf(PagingData.from(pagingData))
    }

    private fun arrangeAttendanceSummary(
        classKey: Int,
        date: Long?,
        result: Result<AttendanceSummaryEntity, NetworkError>,
    ) {
        coEvery {
            getAttendanceByClassSummaryUseCase(classKey, date)
        } returns flowOf(result)
    }

    private fun arrangeAttendanceDetail(
        id: String,
        result: Result<AttendanceDetailEntity, NetworkError>,
    ) {
        coEvery {
            getAttendanceByIdUseCase(id)
        } returns flowOf(result)
    }

    private fun arrangeExportData(
        query: GetExportAttendanceDataQueryParams,
        result: Result<ExportAttendanceDataEntity, NetworkError>,
    ) {
        coEvery {
            getExportAttendanceDataUseCase(query)
        } returns flowOf(result)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = AttendanceViewModel(
            getAttendanceByClassListUseCase,
            getAttendanceByIdUseCase,
            getAttendanceByClassSummaryUseCase,
            getAttendanceByClassPagingUseCase,
            getExportAttendanceDataUseCase,
            fileWriter
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ---------- Tests ----------

    @Test
    fun `init() with null user should do nothing`() = runTest {
        // Arrange

        // Act
        viewModel.init(null, false)

        // Assert
        assertEquals(LoginEntity(), viewModel.uiState.value.user)
    }

    @Test
    fun `init() desktop should load list and summary`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1)
        val date = getCurrentDateAsList().firstOrNull()?.toEpochMillis()
        val queryParams = GetAttendanceListByClassQueryParams(date = date.toString())

        arrangeAttendanceList(1, queryParams, Result.Success(emptyList()))
        arrangeAttendanceSummary(1, null, Result.Success(AttendanceSummaryEntity()))

        // Act
        viewModel.init(user, false)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.summary)

        coVerify {
            getAttendanceByClassListUseCase(1, queryParams)
            getAttendanceByClassSummaryUseCase(1, date)
        }
    }

    @Test
    fun `init() should return Result Error on getAttendanceList and getSummary`() = runTest {
        val date = getCurrentDateAsList().firstOrNull()?.toEpochMillis()
        val queryParams = GetAttendanceListByClassQueryParams(date = date.toString())
        val user = LoginEntity(classKey = 1)

        arrangeAttendanceList(1, queryParams, Result.Error(NetworkError.RESPONSE_ERROR))
        arrangeAttendanceSummary(1, date, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        viewModel.init(user, false)

        //Assert
        coVerify {
            getAttendanceByClassListUseCase(1, queryParams)
            getAttendanceByClassSummaryUseCase(1, date)
        }

        val state = viewModel.uiState.value
        assertEquals(emptyList<AttendanceByClassEntity>(), state.attendanceList)
        assertEquals(AttendanceSummaryEntity(), state.summary)
    }

    @Test
    fun `init() with classKey null should do nothing on desktop`() {
        val user = LoginEntity(classKey = null)

        //Act
        viewModel.init(user, false)

        //Assert
        coVerify(exactly = 0) {
            getAttendanceByClassSummaryUseCase(any(), any())
            getAttendanceByClassListUseCase(any(), any())
        }

        val state = viewModel.uiState.value
        assertEquals(AttendanceSummaryEntity(), state.summary)
        assertEquals(emptyList<AttendanceByClassEntity>(), state.attendanceList)
    }


    @Test
    fun `init() with classKey null should do nothing on mobile`() {
        val user = LoginEntity(classKey = null)

        //Act
        viewModel.init(user, true)

        //Assert
        coVerify(exactly = 0) {
            getAttendanceByClassSummaryUseCase(any(), any())
            getAttendanceByClassPagingUseCase(any(), any())
        }

        val state = viewModel.uiState.value
        assertEquals(AttendanceSummaryEntity(), state.summary)
    }

    @Test
    fun `init() should return Success getting paging data and summary`() = runTest {
        //Arrange
        val classKey = 2
        val user = LoginEntity(
            id = "123",
            classKey = classKey
        )
        val date = getCurrentDateAsList().firstOrNull()?.toEpochMillis()

        val mockList =
            listOf(AttendanceByClassEntity(attendance = AttendanceByClassEntity.Attendance(id = "123")))
        val queryParams = GetAttendanceListByClassQueryParams(date = date.toString())
        val mockkSummary =
            AttendanceSummaryEntity(
                checkedIn = 1,
                late = 1,
                excused = 1,
                approvalPending = 1,
                absent = 1,
                sick = 1,
                other = 1
            )

        arrangeAttendancePaging(classKey, queryParams, mockList)
        arrangeAttendanceSummary(classKey, date, Result.Success(mockkSummary))

        //Act
        viewModel.init(user, true)

        //Arrange
        coVerify(exactly = 1) {
            getAttendanceByClassPagingUseCase(classKey, queryParams)
            getAttendanceByClassSummaryUseCase(classKey, date)
        }

        val state = viewModel.uiState.value
        assertEquals(mockkSummary, state.summary)
    }


    @Test
    fun `init() should return Error getting paging data and summary`() = runTest {
        //Arrange
        val classKey = 2
        val user = LoginEntity(
            id = "123",
            classKey = classKey
        )
        val date = getCurrentDateAsList().firstOrNull()?.toEpochMillis()

        val mockList =
            listOf(AttendanceByClassEntity(attendance = AttendanceByClassEntity.Attendance(id = "123")))
        val queryParams = GetAttendanceListByClassQueryParams(date = date.toString())

        arrangeAttendancePaging(classKey, queryParams, mockList)
        arrangeAttendanceSummary(classKey, date, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        viewModel.init(user, true)

        //Arrange
        coVerify(exactly = 1) {
            getAttendanceByClassPagingUseCase(classKey, queryParams)
            getAttendanceByClassSummaryUseCase(classKey, date)
        }

        val state = viewModel.uiState.value
        assertEquals(AttendanceSummaryEntity(), state.summary)
    }

    @Test
    fun `search() should update query and refresh`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1)
        val cb = viewModel.getCallback()
        val queryParams = GetAttendanceListByClassQueryParams()
        arrangeAttendanceList(1, queryParams, Result.Success(emptyList()))
        arrangeAttendanceSummary(1, null, Result.Success(AttendanceSummaryEntity()))
        viewModel.init(user, false)

        // Act
        cb.onSearch("john")
        advanceUntilIdle()

        // Assert
        assertEquals("john", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `search() should update query and refresh paging`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1)
        val cb = viewModel.getCallback()
        val date = getCurrentDateAsList().firstOrNull()?.toEpochMillis()
        val queryParams =
            GetAttendanceListByClassQueryParams(date = date.toString())
        val mockkList =
            listOf(AttendanceByClassEntity(attendance = AttendanceByClassEntity.Attendance(id = "123")))

        arrangeAttendancePaging(1, queryParams, mockkList)
        arrangeAttendancePaging(1, queryParams.copy(search = "john"), emptyList())
        arrangeAttendanceSummary(
            1,
            date,
            Result.Success(AttendanceSummaryEntity())
        )
        viewModel.init(user, true)
        advanceUntilIdle()

        // Act
        cb.onSearch("john")

        // Assert
        coVerify {
            getAttendanceByClassPagingUseCase(1, queryParams.copy(search = "john"))
        }

        val state = viewModel.uiState.value
        assertEquals("john", state.searchQuery)
    }


    @Test
    fun `filter() should update filter and refresh`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1)
        val filter = AttendanceFilterData(date = LocalDate(2024, 1, 1).toEpochMilli())
        val cb = viewModel.getCallback()
        val queryParams = GetAttendanceListByClassQueryParams(
            date = LocalDate(2024, 1, 1).toEpochMilli().toString()
        )
        arrangeAttendanceList(1, queryParams, Result.Success(emptyList()))
        arrangeAttendanceSummary(1, filter.date, Result.Success(AttendanceSummaryEntity()))
        viewModel.init(user, false)

        // Act
        cb.onFilter(filter)
        advanceUntilIdle()

        // Assert
        assertEquals(filter, viewModel.uiState.value.filterData)
    }

    @Test
    fun `getDetail() with null attendance id should generate blank detail`() = runTest {
        // Arrange
        val student = AttendanceByClassEntity.Student("1", "S1", "John")
        val entity = AttendanceByClassEntity(student = student, attendance = null)
        val cb = viewModel.getCallback()

        // Act
        cb.onGetDetail(entity)

        // Assert
        val detail = viewModel.uiState.value.attendanceDetail
        assertEquals(AttendanceStatus.NO_DATA, detail.status)
    }

    @Test
    fun `getDetail() should return Result Error`() = runTest {
        //Arrange
        val id = "123"
        val expected = AttendanceDetailEntity()
        val attendanceEntity =
            AttendanceByClassEntity(attendance = AttendanceByClassEntity.Attendance(id = id))

        arrangeAttendanceDetail(id, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        val cb = viewModel.getCallback()
        cb.onGetDetail(attendanceEntity)

        //Assert
        coVerify(exactly = 1) {
            getAttendanceByIdUseCase(id)
        }

        val state = viewModel.uiState.value
        assertEquals(expected, state.attendanceDetail)
    }

    @Test
    fun `getDetail() success should load detail`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1)
        val student = AttendanceByClassEntity.Student("1", "S1", "John")
        val entity = AttendanceByClassEntity(
            student = student,
            attendance = AttendanceByClassEntity.Attendance("A1")
        )
        val detail = AttendanceDetailEntity(id = "A1")
        arrangeAttendanceDetail("A1", Result.Success(detail))
        viewModel.init(user, false)
        val cb = viewModel.getCallback()

        // Act
        cb.onGetDetail(entity)
        advanceUntilIdle()

        // Assert
        assertEquals(detail, viewModel.uiState.value.attendanceDetail)
    }

    @Test
    fun `exportFile() success should write excel and update state`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1)
        val cb = viewModel.getCallback()
        val exportData = ExportAttendanceDataEntity(
            sDateRange = listOf("2002-08-06", "2003-08-06"),
            data = listOf(
                ExportAttendanceDataEntity.Data(
                    fullName = "name1",
                    classX = "classX",
                    studentId = "123",
                    dataPerDate = listOf(
                        ExportAttendanceDataEntity.Data.AttendanceRecord(
                            sDate = "2003-08-07",
                            status = ""
                        ),
                        ExportAttendanceDataEntity.Data.AttendanceRecord(
                            sDate = "2003-08-07",
                            status = "late"
                        )
                    )
                ),
                ExportAttendanceDataEntity.Data(
                    fullName = "name2",
                    classX = "classX",
                    studentId = "124",
                    dataPerDate = listOf(
                        ExportAttendanceDataEntity.Data.AttendanceRecord(
                            sDate = "2003-08-07",
                            status = ""
                        ),
                        ExportAttendanceDataEntity.Data.AttendanceRecord(
                            sDate = "2003-08-07",
                            status = "late"
                        )
                    )
                ),
            )
        )
        val query = GetExportAttendanceDataQueryParams(1, null)

        arrangeExportData(query, Result.Success(exportData))
        coEvery { fileWriter.writeExcel(any(), any()) } returns Unit
        viewModel.init(user, false)

        // Act
        cb.onExportFile("attendance.xlsx")
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.exportState == true)
        assertFalse(viewModel.uiState.value.isLoadingOverlay)

        coVerify {
            fileWriter.writeExcel("attendance.xlsx", any())
        }
    }

    @Test
    fun `exportFile() success should write empty list when date range is empty`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1)
        val cb = viewModel.getCallback()
        val exportData = ExportAttendanceDataEntity()
        val query = GetExportAttendanceDataQueryParams(1, null)

        arrangeExportData(query, Result.Success(exportData))
        coEvery { fileWriter.writeExcel(any(), any()) } returns Unit
        viewModel.init(user, false)

        // Act
        cb.onExportFile("attendance.xlsx")
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.exportState == true)
        assertFalse(viewModel.uiState.value.isLoadingOverlay)

        coVerify {
            fileWriter.writeExcel("attendance.xlsx", any())
        }
    }


    @Test
    fun `exportFile() error should update state`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 1)
        val cb = viewModel.getCallback()
        val query = GetExportAttendanceDataQueryParams(1, null)

        arrangeExportData(query, Result.Error(NetworkError.RESPONSE_ERROR))
        viewModel.init(user, false)

        // Act
        cb.onExportFile("attendance.xlsx")
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.exportState == false)
        assertFalse(viewModel.uiState.value.isLoadingOverlay)

        coVerify(exactly = 0) {
            fileWriter.writeExcel("attendance.xlsx", any())
        }
    }

    @Test
    fun `resetMessageState() should reset exportState`() = runTest {
        // Arrange
        val cb = viewModel.getCallback()

        // Act
        cb.onResetMessageState()

        // Assert
        assertNull(viewModel.uiState.value.exportState)
    }

    @Test
    fun `updateExportDateRange() should update exportDateRange to given value`() {
        //Arrange
        val expected = listOf(
            LocalDate(2003, 8, 6),
            LocalDate(2002, 8, 6)
        )
        val cb = viewModel.getCallback()

        //Act
        cb.onUpdateExportDateRange(expected)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expected, state.exportDateRanges)
    }
}
