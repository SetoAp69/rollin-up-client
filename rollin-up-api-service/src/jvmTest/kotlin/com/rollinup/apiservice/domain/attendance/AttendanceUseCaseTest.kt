package com.rollinup.apiservice.domain.attendance

import androidx.paging.PagingData
import com.rollinup.apiservice.data.repository.attendance.AttendanceRepository
import com.rollinup.apiservice.data.source.network.model.request.attendance.CheckInBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.EditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetExportAttendanceDataQueryParams
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.attendance.DashboardDataEntity
import com.rollinup.apiservice.model.attendance.ExportAttendanceDataEntity
import com.rollinup.apiservice.model.common.Result
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@Suppress("UnusedFlow")
class AttendanceUseCaseTest {

    @MockK
    lateinit var repository: AttendanceRepository

    // Use Cases
    private lateinit var getAttendanceByStudentPagingUseCase: GetAttendanceByStudentPagingUseCase
    private lateinit var getAttendanceByClassPagingUseCase: GetAttendanceByClassPagingUseCase
    private lateinit var getAttendanceByStudentSummaryUseCase: GetAttendanceByStudentSummaryUseCase
    private lateinit var getAttendanceByClassSummaryUseCase: GetAttendanceByClassSummaryUseCase
    private lateinit var getAttendanceByStudentListUseCase: GetAttendanceByStudentListUseCase
    private lateinit var getAttendanceByClassListUseCase: GetAttendanceByClassListUseCase
    private lateinit var getAttendanceByIdUseCase: GetAttendanceByIdUseCase
    private lateinit var createAttendanceDataUseCase: CreateAttendanceDataUseCase
    private lateinit var checkInUseCase: CheckInUseCase
    private lateinit var editAttendanceDataUseCase: EditAttendanceDataUseCase
    private lateinit var getDashboardDataUseCase: GetDashboardDataUseCase
    private lateinit var getExportAttendanceDataUseCase: GetExportAttendanceDataUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getAttendanceByStudentPagingUseCase = GetAttendanceByStudentPagingUseCase(repository)
        getAttendanceByClassPagingUseCase = GetAttendanceByClassPagingUseCase(repository)
        getAttendanceByStudentSummaryUseCase = GetAttendanceByStudentSummaryUseCase(repository)
        getAttendanceByClassSummaryUseCase = GetAttendanceByClassSummaryUseCase(repository)
        getAttendanceByStudentListUseCase = GetAttendanceByStudentListUseCase(repository)
        getAttendanceByClassListUseCase = GetAttendanceByClassListUseCase(repository)
        getAttendanceByIdUseCase = GetAttendanceByIdUseCase(repository)
        createAttendanceDataUseCase = CreateAttendanceDataUseCase(repository)
        checkInUseCase = CheckInUseCase(repository)
        editAttendanceDataUseCase = EditAttendanceDataUseCase(repository)
        getDashboardDataUseCase = GetDashboardDataUseCase(repository)
        getExportAttendanceDataUseCase = GetExportAttendanceDataUseCase(repository)
    }

    //region Paging Use Cases
    @Test
    fun `GetAttendanceByStudentPagingUseCase should call repository`() {
        // Arrange
        val id = "student1"
        val params = GetAttendanceListByStudentQueryParams()
        val expectedFlow = flowOf(PagingData.empty<AttendanceByStudentEntity>())
        every { repository.getAttendanceByStudentPaging(id, params) } returns expectedFlow

        // Act
        val result = getAttendanceByStudentPagingUseCase(id, params)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getAttendanceByStudentPaging(id, params) }
    }

    @Test
    fun `GetAttendanceByClassPagingUseCase should call repository`() {
        // Arrange
        val classKey = 101
        val params = GetAttendanceListByClassQueryParams()
        val expectedFlow = flowOf(PagingData.empty<AttendanceByClassEntity>())
        every { repository.getAttendanceByClassPaging(classKey, params) } returns expectedFlow

        // Act
        val result = getAttendanceByClassPagingUseCase(classKey, params)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getAttendanceByClassPaging(classKey, params) }
    }
    //endregion

    //region Summary Use Cases
    @Test
    fun `GetAttendanceByStudentSummaryUseCase should call repository`() {
        // Arrange
        val studentId = "student1"
        val dates = listOf(100L, 200L)
        val expectedFlow = flowOf(Result.Success(AttendanceSummaryEntity(0, 0, 0, 0, 0, 0, 0)))
        every { repository.getAttendanceByStudentSummary(studentId, dates) } returns expectedFlow

        // Act
        val result = getAttendanceByStudentSummaryUseCase(studentId, dates)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getAttendanceByStudentSummary(studentId, dates) }
    }

    @Test
    fun `GetAttendanceByClassSummaryUseCase should call repository`() {
        // Arrange
        val classKey = 101
        val date = 100L
        val expectedFlow = flowOf(Result.Success(AttendanceSummaryEntity(0, 0, 0, 0, 0, 0, 0)))
        every { repository.getAttendanceByClassSummary(classKey, date) } returns expectedFlow

        // Act
        val result = getAttendanceByClassSummaryUseCase(classKey, date)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getAttendanceByClassSummary(classKey, date) }
    }
    //endregion

    //region List Use Cases
    @Test
    fun `GetAttendanceByStudentListUseCase should call repository`() {
        // Arrange
        val id = "student1"
        val params = GetAttendanceListByStudentQueryParams()
        val expectedFlow = flowOf(Result.Success(listOf<AttendanceByStudentEntity>()))
        every { repository.getAttendanceByStudentList(id, params) } returns expectedFlow

        // Act
        val result = getAttendanceByStudentListUseCase(id, params)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getAttendanceByStudentList(id, params) }
    }

    @Test
    fun `GetAttendanceByClassListUseCase should call repository`() {
        // Arrange
        val classKey = 101
        val params = GetAttendanceListByClassQueryParams()
        val expectedFlow = flowOf(Result.Success(listOf<AttendanceByClassEntity>()))
        every { repository.getAttendanceByClassList(classKey, params) } returns expectedFlow

        // Act
        val result = getAttendanceByClassListUseCase(classKey, params)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getAttendanceByClassList(classKey, params) }
    }
    //endregion

    //region Detail & Actions Use Cases
    @Test
    fun `GetAttendanceByIdUseCase should call repository`() {
        // Arrange
        val id = "att1"
        val mockDetail = io.mockk.mockk<AttendanceDetailEntity>()
        val expectedFlow = flowOf(Result.Success(mockDetail))
        every { repository.getAttendanceById(id) } returns expectedFlow

        // Act
        val result = getAttendanceByIdUseCase(id)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getAttendanceById(id) }
    }

    @Test
    fun `CreateAttendanceDataUseCase should call repository`() {
        // Arrange
        val body = CreateAttendanceBody(id = "s1")
        val expectedFlow = flowOf(Result.Success(Unit))
        every { repository.createAttendanceData(body) } returns expectedFlow

        // Act
        val result = createAttendanceDataUseCase(body)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.createAttendanceData(body) }
    }

    @Test
    fun `CheckInUseCase should call repository`() {
        // Arrange
        val body = CheckInBody(latitude = 1.0, longitude = 1.0)
        val expectedFlow = flowOf(Result.Success(Unit))
        every { repository.checkIn(body) } returns expectedFlow

        // Act
        val result = checkInUseCase(body)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.checkIn(body) }
    }

    @Test
    fun `EditAttendanceDataUseCase should call repository`() {
        // Arrange
        val id = "att1"
        val body = EditAttendanceBody()
        val expectedFlow = flowOf(Result.Success(Unit))
        every { repository.editAttendanceData(id, body) } returns expectedFlow

        // Act
        val result = editAttendanceDataUseCase(id, body)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.editAttendanceData(id, body) }
    }
    //endregion

    //region Dashboard & Export
    @Test
    fun `GetDashboardDataUseCase should call repository`() {
        // Arrange
        val id = "s1"
        val date = LocalDate(2024, 1, 1)
        val mockDashboard = io.mockk.mockk<DashboardDataEntity>()
        val expectedFlow = flowOf(Result.Success(mockDashboard))
        every { repository.getDashboardData(id, date) } returns expectedFlow

        // Act
        val result = getDashboardDataUseCase(id, date)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getDashboardData(id, date) }
    }

    @Test
    fun `GetExportAttendanceDataUseCase should call repository`() {
        // Arrange
        val params = GetExportAttendanceDataQueryParams()
        val mockExport = io.mockk.mockk<ExportAttendanceDataEntity>()
        val expectedFlow = flowOf(Result.Success(mockExport))
        every { repository.getAttendanceExportData(params) } returns expectedFlow

        // Act
        val result = getExportAttendanceDataUseCase(params)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getAttendanceExportData(params) }
    }
    //endregion
}