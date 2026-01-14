@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("UnusedFlow")

package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.viewmodel

import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.domain.attendance.CheckInUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentListUseCase
import com.rollinup.apiservice.domain.attendance.GetDashboardDataUseCase
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.attendance.DashboardDataEntity
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.CoroutineTestRule
import dev.jordond.compass.Coordinates
import dev.jordond.compass.Location
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StudentDashboardViewModelTest {

    private lateinit var viewModel: StudentDashboardViewmodel

    @MockK
    private lateinit var checkInUseCase: CheckInUseCase

    @MockK
    private lateinit var getAttendanceByStudentListUseCase: GetAttendanceByStudentListUseCase

    @MockK
    private lateinit var getAttendanceByIdUseCase: GetAttendanceByIdUseCase

    @MockK
    private lateinit var getDashboardDataUseCase: GetDashboardDataUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    // ---------- Arrange Helpers ----------

    private fun arrangeGetAttendanceList(
        id: String,
        queryParams: GetAttendanceListByStudentQueryParams,
        result: Result<List<AttendanceByStudentEntity>, NetworkError>,
    ) {
        coEvery {
            getAttendanceByStudentListUseCase(id, queryParams)
        } returns flowOf(result)
    }

    private fun arrangeGetDashboardData(
        id: String,
        result: Result<DashboardDataEntity, NetworkError>,
    ) {
        // We match any date here to avoid flaky tests dependent on LocalDate.now()
        coEvery {
            getDashboardDataUseCase(id, any())
        } returns flowOf(result)
    }

    private fun arrangeGetAttendanceDetail(
        id: String,
        result: Result<AttendanceDetailEntity, NetworkError>,
    ) {
        coEvery {
            getAttendanceByIdUseCase(id)
        } returns flowOf(result)
    }

    private fun arrangeCheckIn(
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            checkInUseCase(any())
        } returns flowOf(result)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = StudentDashboardViewmodel(
            checkInUseCase,
            getAttendanceByStudentListUseCase,
            getAttendanceByIdUseCase,
            getDashboardDataUseCase
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
        val user: LoginEntity? = null

        // Act
        viewModel.init(user)

        // Assert
        assertNull(viewModel.uiState.value.user)
        coVerify(exactly = 0) {
            getAttendanceByStudentListUseCase(any(), any())
            getDashboardDataUseCase(any(), any())
        }
    }

    @Test
    fun `init() with valid user should fetch list and summary successfully`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student1")
        val expectedList =
            listOf(AttendanceByStudentEntity(id = "1", status = AttendanceStatus.ON_TIME))
        val expectedSummary = DashboardDataEntity(
            attendanceStatus = AttendanceStatus.ON_TIME,
            summary = AttendanceSummaryEntity(
                checkedIn = 1,
                late = 1,
                excused = 1,
                approvalPending = 1,
                absent = 1,
                sick = 1,
                other = 1
            )
        )

        arrangeGetAttendanceList(
            id = "student1",
            queryParams = GetAttendanceListByStudentQueryParams(),
            result = Result.Success(expectedList)
        )
        arrangeGetDashboardData(
            id = "student1",
            result = Result.Success(expectedSummary)
        )

        // Act
        viewModel.init(user)
        advanceUntilIdle()

        // Assert
        coVerify {
            getAttendanceByStudentListUseCase("student1", GetAttendanceListByStudentQueryParams())
            getDashboardDataUseCase(
                id = "student1",
                date = any()
            )
        }

        val state = viewModel.uiState.value
        assertEquals(user, state.user)
        assertEquals(expectedList, state.attendanceList)
        assertEquals(expectedSummary.summary, state.summary)
        assertEquals(AttendanceStatus.ON_TIME, state.currentStatus)
        assertFalse(state.isLoadingCalendar)
        assertFalse(state.isLoadingHeader)
    }

    @Test
    fun `init() error in fetching list should handle gracefully`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student1")
        val mockkSummary = DashboardDataEntity(
            attendanceStatus = AttendanceStatus.ON_TIME,
            summary = AttendanceSummaryEntity(
                checkedIn = 2,
                late = 2,
                excused = 2,
                approvalPending = 2,
                absent = 2,
                sick = 2,
                other = 2
            )
        )

        arrangeGetAttendanceList(
            id = "student1",
            queryParams = GetAttendanceListByStudentQueryParams(),
            result = Result.Error(NetworkError.RESPONSE_ERROR)
        )
        arrangeGetDashboardData(
            id = "student1",
            result = Result.Success(mockkSummary)
        )

        // Act
        viewModel.init(user)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.attendanceList.isEmpty())
        assertFalse(state.isLoadingCalendar)
    }

    @Test
    fun `init() error in fetching summary should handle gracefully`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student1")

        arrangeGetAttendanceList(
            id = "student1",
            queryParams = GetAttendanceListByStudentQueryParams(dateRange = "[]"),
            result = Result.Success(emptyList())
        )
        arrangeGetDashboardData(
            id = "student1",
            result = Result.Error(NetworkError.RESPONSE_ERROR)
        )

        // Act
        viewModel.init(user)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        // Default summary is usually empty string or null, checking isLoadingHeader is false
        assertFalse(state.isLoadingHeader)
    }

    @Test
    fun `getAttendanceList() should return if user ID is null`() = runTest {
        // Arrange
        // We do NOT call init, so user is null by default in state
        val cb = viewModel.getCallback()

        // Act
        cb.onRefresh() // Refresh calls getAttendanceList internally

        // Assert
        coVerify(exactly = 0) {
            getAttendanceByStudentListUseCase(any(), any())
        }
    }

    @Test
    fun `getSummary() should return if user ID is null`() = runTest {
        // Arrange
        // We do NOT call init, so user is null
        val cb = viewModel.getCallback()

        // Act
        cb.onRefresh() // Refresh calls getSummary internally

        // Assert
        coVerify(exactly = 0) {
            getDashboardDataUseCase(any(), any())
        }
    }

    @Test
    fun `refresh() should call getSummary and getAttendanceList`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student1")
        val mockkSummary = DashboardDataEntity(
            attendanceStatus = AttendanceStatus.ON_TIME,
            summary = AttendanceSummaryEntity(
                checkedIn = 2,
                late = 2,
                excused = 2,
                approvalPending = 2,
                absent = 2,
                sick = 2,
                other = 2
            )
        )
        val queryParams = GetAttendanceListByStudentQueryParams()

        viewModel.init(user) // Setup initial state

        arrangeGetAttendanceList("student1", queryParams, Result.Success(emptyList()))
        arrangeGetDashboardData("student1", Result.Success(mockkSummary))
        val cb = viewModel.getCallback()

        // Act
        cb.onRefresh()
        advanceUntilIdle()

        // Assert
        coVerify(atLeast = 2) {
            // Called once in init, once in refresh
            getAttendanceByStudentListUseCase("student1", any())
            getDashboardDataUseCase("student1", any())
        }
    }

    @Test
    fun `showAttendanceDetail() success should update detail`() = runTest {
        // Arrange
        val id = "att1"
        val expectedDetail = AttendanceDetailEntity(id = id, status = AttendanceStatus.ON_TIME)
        arrangeGetAttendanceDetail(id, Result.Success(expectedDetail))
        val cb = viewModel.getCallback()

        // Act
        cb.onShowAttendanceDetail(id)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoadingDetail)
        assertEquals(expectedDetail, state.attendanceDetail)
    }

    @Test
    fun `showAttendanceDetail() error should stop loading`() = runTest {
        // Arrange
        val id = "att1"
        arrangeGetAttendanceDetail(id, Result.Error(NetworkError.RESPONSE_ERROR))
        val cb = viewModel.getCallback()

        // Act
        cb.onShowAttendanceDetail(id)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoadingDetail)
        // Detail should remain default/null or previous state
        assertEquals(AttendanceDetailEntity(), state.attendanceDetail)
    }

    @Test
    fun `checkIn() success should update checkInState`() = runTest {
        // Arrange
        val attachment = mockk<MultiPlatformFile>()
        val location = mockk<Location>()
        val coordinates = mockk<Coordinates>()
        every { location.coordinates } returns coordinates
        every { coordinates.latitude } returns 10.0
        every { coordinates.longitude } returns 20.0

        arrangeCheckIn(Result.Success(Unit))
        val cb = viewModel.getCallback()

        // Act
        cb.onCheckIn(attachment, location)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoadingOverlay)
        assertTrue(state.checkInState == true)

        coVerify {
            checkInUseCase(match {
                it.latitude == 10.0 && it.longitude == 20.0
            })
        }
    }

    @Test
    fun `checkIn() error should update checkInState to false`() = runTest {
        // Arrange
        val attachment = mockk<MultiPlatformFile>()
        val location = mockk<Location>()
        val coordinates = mockk<Coordinates>()
        every { location.coordinates } returns coordinates
        every { coordinates.latitude } returns 10.0
        every { coordinates.longitude } returns 20.0

        arrangeCheckIn(Result.Error(NetworkError.RESPONSE_ERROR))
        val cb = viewModel.getCallback()

        // Act
        cb.onCheckIn(attachment, location)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoadingOverlay)
        assertTrue(state.checkInState == false)
    }

    @Test
    fun `updateLocation() should update uiState`() {
        // Arrange
        val location = mockk<Location>()
        val cb = viewModel.getCallback()

        // Act
        cb.onUpdateLocation(location, true)

        // Assert
        val state = viewModel.uiState.value
        assertEquals(location, state.currentLocation)
        assertTrue(state.isLocationValid == true)
    }

    @Test
    fun `updateDateRangeSelected() should update date range and implicitly affect next fetch`() {
        // Arrange
        val dateRange = listOf(1000L, 2000L)
        val cb = viewModel.getCallback()

        // Act
        cb.onUpdateDateRangeSelected(dateRange)

        // Assert
        val state = viewModel.uiState.value
        assertEquals(dateRange, state.selectedDateRange)
    }

    @Test
    fun `updateLoginData() should update user in uiState`() {
        // Arrange
        val newUser = LoginEntity(id = "new_user")
        val cb = viewModel.getCallback()

        // Act
        cb.onUpdateLoginData(newUser)

        // Assert
        assertEquals(newUser, viewModel.uiState.value.user)
    }

    @Test
    fun `resetMessageState() should set checkInState to null`() {
        // Arrange
        val cb = viewModel.getCallback()
        val mockfile = mockk<MultiPlatformFile>(relaxed = true)
        val mockLocation = mockk<Location>(relaxed = true)

        cb.onCheckIn(mockfile, mockLocation)

        arrangeCheckIn(Result.Success(Unit))

        // Act
        cb.onResetMessageState()

        // Assert
        val state = viewModel.uiState.value
        assertNull(state.checkInState)
    }

    @Test
    fun `updateTempPhoto() should update tempPhoto in uiState`() {
        // Arrange
        val cb = viewModel.getCallback()
        val mockfile = mockk<MultiPlatformFile>(relaxed = true)

        // Act
        cb.onUpdateTempPhoto(mockfile)

        // Assert
        val state = viewModel.uiState.value
        assertEquals(mockfile, state.tempPhoto)
    }
}