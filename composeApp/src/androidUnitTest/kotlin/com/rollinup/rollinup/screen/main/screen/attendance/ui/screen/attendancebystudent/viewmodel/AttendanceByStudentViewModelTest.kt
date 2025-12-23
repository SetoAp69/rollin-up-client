package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.viewmodel

import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentSummaryUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.component.export.FileWriter
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent.AttendanceByStudentFilterData
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@Suppress("UnusedFlow")
class AttendanceByStudentViewModelTest {

    private lateinit var viewModel: AttendanceByStudentViewModel

    @MockK
    private lateinit var getAttendanceByStudentListUseCase: GetAttendanceByStudentListUseCase

    @MockK
    private lateinit var getAttendanceByStudentPagingUseCase: GetAttendanceByStudentPagingUseCase

    @MockK
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase

    @MockK
    private lateinit var getAttendanceByStudentSummaryUseCase: GetAttendanceByStudentSummaryUseCase

    @MockK
    private lateinit var getAttendanceByIdUseCase: GetAttendanceByIdUseCase

    @MockK
    private lateinit var fileWriter: FileWriter

    @get:Rule
    val coroutineRule = CoroutineTestRule()


    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewModel = AttendanceByStudentViewModel(
            getAttendanceByStudentListUseCase = getAttendanceByStudentListUseCase,
            getAttendanceByStudentPagingUseCase = getAttendanceByStudentPagingUseCase,
            getUserByIdUseCase = getUserByIdUseCase,
            getAttendanceByStudentSummaryUseCase = getAttendanceByStudentSummaryUseCase,
            getAttendanceByIdUseCase = getAttendanceByIdUseCase,
            fileWriter = fileWriter
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }


    private fun arrangeGetAttendanceByStudentList(
        id: String,
        queryParams: GetAttendanceListByStudentQueryParams,
        result: Result<List<AttendanceByStudentEntity>, NetworkError>,
    ) {
        coEvery {
            getAttendanceByStudentListUseCase(id, queryParams)
        } returns flowOf(result)
    }

    private fun arrangeGetSummary(
        id: String,
        date: List<Long>,
        result: Result<AttendanceSummaryEntity, NetworkError>,
    ) {

        coEvery {
            getAttendanceByStudentSummaryUseCase(id, date)
        } returns flowOf(result)
    }

    private fun arrangeGetUserByIdUseCase(
        id: String,
        result: Result<UserDetailEntity, NetworkError>,
    ) {
        coEvery {
            getUserByIdUseCase(id)
        } returns flowOf(result)
    }

    private fun arrangeGetAttendancePagingUseCase(
        id: String,
        queryParams: GetAttendanceListByStudentQueryParams,
        result: List<AttendanceByStudentEntity>,
    ) {
        coEvery {
            getAttendanceByStudentPagingUseCase(id, queryParams)
        } returns flowOf(PagingData.from(result))
    }

    private fun arrangeGetAttendanceDetail(
        id: String,
        result: Result<AttendanceDetailEntity, NetworkError>,
    ) {
        coEvery {
            getAttendanceByIdUseCase(id)
        } returns flowOf(result)
    }

    private fun arrangeInitialData(
        isMobile: Boolean,
        id: String,
    ) {
        val queryParams = GetAttendanceListByStudentQueryParams()

        val summary = AttendanceSummaryEntity(
            checkedIn = 12,
        )

        val listData = listOf(
            AttendanceByStudentEntity(id = "123"),
            AttendanceByStudentEntity(id = "124")
        )

        val mockProfile = UserDetailEntity(id = id, firstName = "Jane", lastName = "Doe")

        if (isMobile) {
            arrangeGetAttendancePagingUseCase(id, queryParams, listData)
        } else {
            arrangeGetAttendanceByStudentList(id, queryParams, Result.Success(listData))
        }

        arrangeGetSummary(id, emptyList(), Result.Success(summary))

        arrangeGetUserByIdUseCase(id, Result.Success(mockProfile))
        viewModel.init(id, isMobile)
    }

    @Test
    fun `init() should do nothing when studentUserId is blank`() = runTest {
        //Arrange
        val id = ""

        //Act
        viewModel.init(id, false)

        //Assert
        coVerify(exactly = 0) {
            getUserByIdUseCase(id)
            getAttendanceByStudentListUseCase(id, any())
            getAttendanceByStudentSummaryUseCase(id, any())
        }

        val state = viewModel.uiState.value
        assertEquals(UserDetailEntity(), state.student)
        assertEquals(emptyList(), state.attendanceList)
        assertEquals(AttendanceSummaryEntity(), state.summary)
    }

    @Test
    fun `int() on non mobile should return Result Success `() = runTest {
        ///Arrange
        val id = "123123"
        val queryParams = GetAttendanceListByStudentQueryParams()

        val summary = AttendanceSummaryEntity(
            checkedIn = 12,
        )

        val listData = listOf(
            AttendanceByStudentEntity(id = "123"),
            AttendanceByStudentEntity(id = "124")
        )

        val mockProfile = UserDetailEntity(id = "id", firstName = "Jane", lastName = "Doe")

        arrangeGetAttendanceByStudentList(id, queryParams, Result.Success(listData))

        arrangeGetSummary(id, emptyList(), Result.Success(summary))

        arrangeGetUserByIdUseCase(id, Result.Success(mockProfile))

        //Act
        viewModel.init(id, false)

        //Assert
        coVerify(exactly = 1) {
            getUserByIdUseCase(id)
            getAttendanceByStudentListUseCase(id, queryParams)
            getAttendanceByStudentSummaryUseCase(id, emptyList())
        }

        val state = viewModel.uiState.value
        assertEquals(listData, state.attendanceList)
        assertEquals(summary, state.summary)
        assertEquals(mockProfile, state.student)
    }

    @Test
    fun `init() on mobile should return Result Success `() = runTest {
        ///Arrange
        val id = "123123"
        val queryParams = GetAttendanceListByStudentQueryParams()

        val summary = AttendanceSummaryEntity(
            checkedIn = 12,
        )

        val listData = listOf(
            AttendanceByStudentEntity(id = "123"),
            AttendanceByStudentEntity(id = "124")
        )

        val mockProfile = UserDetailEntity(id = "id", firstName = "Jane", lastName = "Doe")

        arrangeGetAttendancePagingUseCase(id, queryParams, listData)

        arrangeGetSummary(id, emptyList(), Result.Success(summary))

        arrangeGetUserByIdUseCase(id, Result.Success(mockProfile))

        //Act
        viewModel.init(id, true)

        //Assert
        coVerify(exactly = 1) {
            getUserByIdUseCase(id)
            getAttendanceByStudentPagingUseCase(id, queryParams)
            getAttendanceByStudentSummaryUseCase(id, emptyList())
        }

        val state = viewModel.uiState.value
        assertEquals(summary, state.summary)
        assertEquals(mockProfile, state.student)
    }

    @Test
    fun `init() should return Result Error`() = runTest {
        //Arrange
        val id = "123123"
        val queryParams = GetAttendanceListByStudentQueryParams()

        arrangeGetAttendanceByStudentList(
            id,
            queryParams,
            Result.Error(NetworkError.RESPONSE_ERROR)
        )
        arrangeGetUserByIdUseCase(id, Result.Error(NetworkError.RESPONSE_ERROR))
        arrangeGetSummary(id, emptyList(), Result.Error(NetworkError.RESPONSE_ERROR))
        arrangeGetAttendanceByStudentList(
            id,
            queryParams,
            Result.Error(NetworkError.RESPONSE_ERROR)
        )

        //Act
        viewModel.init(id, false)

        //Assert
        coVerify(exactly = 1) {
            getUserByIdUseCase(id)
            getAttendanceByStudentListUseCase(id, queryParams)
            getAttendanceByStudentSummaryUseCase(id, emptyList())
        }
        val state = viewModel.uiState.value
        assertEquals(UserDetailEntity(), state.student)
        assertEquals(emptyList(), state.attendanceList)
        assertEquals(AttendanceSummaryEntity(), state.summary)
    }

    @Test
    fun `reset() should reset uistate to default value`() = runTest {
        //Arrange
        val id = "123"
        arrangeInitialData(false, id)

        //Act
        viewModel.reset()

        //Assert
        coVerify(exactly = 1) {
            getUserByIdUseCase(id)
            getAttendanceByStudentListUseCase(id, any())
            getAttendanceByStudentSummaryUseCase(id, emptyList())
        }
        assertEquals(AttendanceByStudentUiState(), viewModel.uiState.value)
    }

    @Test
    fun `updateFilter() should update filter data and refresh`() = runTest {
        //Arrange
        val id = "123"
        arrangeInitialData(false, id)

        val filterData = AttendanceByStudentFilterData(
            dateRange = listOf(123123, 1231444),
            status = listOf("APPROVAL_PENDING")
        )

        val queryParams = GetAttendanceListByStudentQueryParams(
            dateRange = listOf(123123, 1231444).toJsonString(),
            status = listOf("APPROVAL_PENDING").toJsonString()
        )

        val mockList = listOf(
            AttendanceByStudentEntity(id = "123", status = AttendanceStatus.APPROVAL_PENDING),
            AttendanceByStudentEntity(id = "124", status = AttendanceStatus.APPROVAL_PENDING)
        )

        val mockSummary = AttendanceSummaryEntity(
            approvalPending = 2
        )

        arrangeGetAttendanceByStudentList(id, queryParams, Result.Success(mockList))
        arrangeGetSummary(id, listOf(123123, 1231444), Result.Success(mockSummary))

        //Act
        val cb = viewModel.getCallback()
        cb.onUpdateFilter(filterData)

        //Assert
        coVerify(exactly = 1) {
            getAttendanceByStudentListUseCase(id, queryParams)
            getAttendanceByStudentSummaryUseCase(id, listOf(123123, 1231444))
        }

        val state = viewModel.uiState.value
        assertEquals(mockSummary, state.summary)
        assertEquals(mockList, state.attendanceList)
    }

    @Test
    fun `getDetail() should return Result Success`() = runTest {
        //Arrange
        val id = "stringId"
        val mockDetail = AttendanceDetailEntity(
            id = "123"
        )

        arrangeGetAttendanceDetail(id, Result.Success(mockDetail))

        //Act
        val cb = viewModel.getCallback()
        cb.onGetDetail(id)

        //Assert
        coVerify(exactly = 1) {
            getAttendanceByIdUseCase(id)
        }

        val state = viewModel.uiState.value
        assertEquals(mockDetail, state.attendanceDetail)
        assertFalse(state.isLoadingDetail)
    }

    @Test
    fun `getDetail() should return Result Error`() = runTest {
        //Arrange
        val id = "stringId"

        arrangeGetAttendanceDetail(id, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        val cb = viewModel.getCallback()
        cb.onGetDetail(id)

        //Assert
        coVerify(exactly = 1) {
            getAttendanceByIdUseCase(id)
        }

        val state = viewModel.uiState.value
        assertEquals(AttendanceDetailEntity(), state.attendanceDetail)
        assertFalse(state.isLoadingDetail)
    }

    @Test
    fun `exportFile() should return Result Success`() = runTest {
        //Arrange
        val fileName = "filename"
        val id = "studentId"
        val mockList = listOf(
            AttendanceByStudentEntity(
                date = "2003-08-06",
                status = AttendanceStatus.ON_TIME,
                checkInTime = "2003-08-06T09:00:00+07:00",
            ),
            AttendanceByStudentEntity(
                date = "2004-08-06",
                status = AttendanceStatus.ABSENT,
                permit = AttendanceByStudentEntity.Permit(
                    id = "permitId",
                    reason = "Sick",
                    type = PermitType.ABSENCE,
                    start = "2003-08-06T09:00:00+07:00",
                    end = "2003-08-06T09:00:00+07:00"
                )
            ),
        )

        arrangeInitialData(false, id)

        arrangeGetAttendanceByStudentList(
            id,
            GetAttendanceListByStudentQueryParams(),
            Result.Success(mockList)
        )

        coEvery { fileWriter.writeExcel(fileName, any()) } returns Unit

        //Act
        val cb = viewModel.getCallback()
        cb.onExportFile(fileName)

        //Assert
        coVerify{
            getAttendanceByStudentListUseCase(id, GetAttendanceListByStudentQueryParams())
            fileWriter.writeExcel(fileName, any())
        }

        val state = viewModel.uiState.value
        assertEquals(true, state.exportState)
        assertFalse(state.isLoadingOverlay)
    }

    @Test
    fun `exportFile() should return Result Error`() = runTest {
        //Arrange
        val fileName = "filename"
        val id = "studentId"

        arrangeInitialData(false, id)

        arrangeGetAttendanceByStudentList(
            id,
            GetAttendanceListByStudentQueryParams(),
            Result.Error(NetworkError.RESPONSE_ERROR)
        )

        coEvery { fileWriter.writeExcel(fileName, any()) } returns Unit

        //Act
        val cb = viewModel.getCallback()
        cb.onExportFile(fileName)

        //Assert
        coVerify {
            getAttendanceByStudentListUseCase(id, GetAttendanceListByStudentQueryParams())
        }

        val state = viewModel.uiState.value
        assertEquals(false, state.exportState)
        assertFalse(state.isLoadingOverlay)
    }
}
