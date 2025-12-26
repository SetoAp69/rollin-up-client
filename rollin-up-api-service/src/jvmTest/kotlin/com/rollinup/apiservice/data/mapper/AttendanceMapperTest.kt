package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByStudentResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceSummaryResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetDashboardDataResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetExportAttendanceDataResponse
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.utils.Utils
import com.rollinup.apiservice.utils.Utils.getFileLink
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AttendanceMapperTest {

    private lateinit var mapper: AttendanceMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(Utils)
        every { any<String>().getFileLink() } answers { "http://mock/${firstArg<String>()}" }

        mapper = AttendanceMapper()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    //region mapAttendanceListByStudent
    @Test
    fun `mapAttendanceListByStudent should map correctly when permit is present`() {
        // Arrange
        val response = listOf(
            GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(
                id = "att1",
                status = "ABSENT",
                date = "2024-01-01",
                checkedInAt = "07:00",
                permit = GetAttendanceListByStudentResponse.Data.Permit(
                    id = "p1",
                    reason = "Sick",
                    type = "ABSENCE",
                    startTime = "07:00",
                    endTime = "14:00"
                )
            )
        )

        // Act
        val result = mapper.mapAttendanceListByStudent(response)

        // Assert
        assertEquals(1, result.size)
        val entity = result.first()
        assertEquals("att1", entity.id)
        assertEquals(AttendanceStatus.ABSENT, entity.status)
        assertNotNull(entity.permit)
        assertEquals("p1", entity.permit.id)
        assertEquals(PermitType.ABSENCE, entity.permit.type)
    }

    @Test
    fun `mapAttendanceListByStudent should map correctly when permit is null`() {
        // Arrange
        val response = listOf(
            GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(
                id = "att1",
                status = "Alpa",
                date = "2024-01-01",
                checkedInAt = null,
                permit = null // Testing null branch
            )
        )

        // Act
        val result = mapper.mapAttendanceListByStudent(response)

        // Assert
        val entity = result.first()
        assertEquals("att1", entity.id)
        assertNull(entity.permit)
    }
    //endregion

    //region mapDashboardData
    @Test
    fun `mapDashboardData should map correctly with valid status`() {
        // Arrange
        val response = GetDashboardDataResponse.Data(
            status = "CHECKED_IN",
            summary = GetDashboardDataResponse.Data.Summary(
                checkedIn = 1,
                late = 0,
                excused = 0,
                approvalPending = 0,
                absent = 0,
                sick = 0,
                other = 0
            )
        )

        // Act
        val result = mapper.mapDashboardData(response)

        // Assert
        assertEquals(AttendanceStatus.ON_TIME, result.attendanceStatus)
        assertEquals(1, result.summary.checkedIn)
    }

    @Test
    fun `mapDashboardData should handle null status (Elvis operator)`() {
        // Arrange
        val response = GetDashboardDataResponse.Data(
            status = null, // Testing (data.status ?: "")
            summary = GetDashboardDataResponse.Data.Summary()
        )

        // Act
        val result = mapper.mapDashboardData(response)

        // Assert
        // Assuming fromValue("") returns UNKNOWN or similar default
        assertEquals(AttendanceStatus.NO_DATA, result.attendanceStatus)
    }
    //endregion

    //region mapAttendanceListByClass
    @Test
    fun `mapAttendanceListByClass should map full data`() {
        // Arrange
        val response = listOf(
            GetAttendanceListByClassResponse.Data.Data(
                student = GetAttendanceListByClassResponse.Data.User(
                    id = "s1", name = "John", studentId = "123"
                ),
                attendance = GetAttendanceListByClassResponse.Data.Attendance(
                    id = "a1", checkedInAt = "07:00", status = "Hadir", date = "2024-01-01"
                ),
                permit = GetAttendanceListByClassResponse.Data.Permit(
                    id = "p1",
                    reason = "Sick",
                    type = "Sakit",
                    startTime = "08:00",
                    endTime = "12:00"
                )
            )
        )

        // Act
        val result = mapper.mapAttendanceListByClass(response)

        // Assert
        val entity = result.first()
        assertEquals("s1", entity.student.id)
        assertEquals("123", entity.student.studentId)
        assertNotNull(entity.attendance)
        assertEquals("a1", entity.attendance.id)
        assertNotNull(entity.permit)
        assertEquals("p1", entity.permit.id)
    }

    @Test
    fun `mapAttendanceListByClass should handle nulls (attendance, permit, studentId)`() {
        // Arrange
        val response = listOf(
            GetAttendanceListByClassResponse.Data.Data(
                student = GetAttendanceListByClassResponse.Data.User(
                    id = "s1",
                    name = "John",
                    studentId = null
                ),
                attendance = null,
                permit = null
            )
        )

        // Act
        val result = mapper.mapAttendanceListByClass(response)

        // Assert
        val entity = result.first()

        assertEquals("", entity.student.studentId)
        assertNull(entity.attendance)
        assertNull(entity.permit)
    }
    //endregion

    //region mapAttendanceByClassSummary
    @Test
    fun `mapAttendanceByClassSummary should map all fields`() {
        // Arrange
        val response = GetAttendanceSummaryResponse.Data(
            checkedIn = 10,
            late = 5,
            excused = 2,
            approvalPending = 1,
            absent = 3,
            sick = 4,
            other = 0
        )

        // Act
        val result = mapper.mapAttendanceByClassSummary(response)

        // Assert
        assertEquals(10, result.checkedIn)
        assertEquals(5, result.late)
        assertEquals(2, result.excused)
        assertEquals(1, result.approvalPending)
    }
    //endregion

    //region mapAttendanceById
    @Test
    fun `mapAttendanceById should map correctly with permit and approvedBy`() {
        // Arrange
        val response = GetAttendanceByIdResponse.Data(
            id = "att1",
            student = GetAttendanceByIdResponse.Data.User(
                id = "s1",
                name = "John",
                studentId = "123",
                xClass = "10A"
            ),
            status = "ABSENT",
            checkedInAt = "07:00",
            updatedAt = "08:00",
            createdAt = "06:00",
            permit = GetAttendanceByIdResponse.Data.Permit(
                id = "p1",
                reason = "Reason",
                type = "Izin",
                startTime = "08:00",
                endTime = "10:00",
                note = "Note",
                attachment = "file.jpg",
                approvalNote = "Approved",
                approvedBy = GetAttendanceByIdResponse.Data.User(
                    id = "t1", name = "Teacher", xClass = null
                ),
                approvedAt = "09:00"
            )
        )

        // Act
        val result = mapper.mapAttendanceById(response)

        assertEquals("att1", result.id)
        assertNotNull(result.permit)
        assertEquals("http://mock/file.jpg", result.permit.attachment)
        assertNotNull(result.permit.approvedBy)
        assertEquals("t1", result.permit.approvedBy.id)
    }

    @Test
    fun `mapAttendanceById should handle null permit`() {
        // Arrange
        val response = GetAttendanceByIdResponse.Data(
            id = "att1",
            student = GetAttendanceByIdResponse.Data.User(
                id = "s1",
                name = "John",
                studentId = "123",
                xClass = "10A"
            ),
            status = "Hadir",
            checkedInAt = "07:00",
            updatedAt = "08:00",
            createdAt = "06:00",
            permit = null
        )

        // Act
        val result = mapper.mapAttendanceById(response)

        // Assert
        assertNull(result.permit)
    }

    @Test
    fun `mapAttendanceById should handle null approvedBy inside permit`() {
        // Arrange
        val response = GetAttendanceByIdResponse.Data(
            id = "att1",
            student = GetAttendanceByIdResponse.Data.User(
                id = "s1",
                name = "John",
                studentId = "123",
                xClass = "10A"
            ),
            status = "Hadir",
            checkedInAt = "07:00",
            updatedAt = "08:00",
            createdAt = "06:00",
            permit = GetAttendanceByIdResponse.Data.Permit(
                id = "p1",
                reason = "Reason",
                type = "Izin",
                startTime = "08:00",
                endTime = "10:00",
                note = "Note",
                attachment = "file.jpg",
                approvalNote = null,
                approvedBy = null,
                approvedAt = null
            )
        )

        // Act
        val result = mapper.mapAttendanceById(response)

        // Assert
        assertNotNull(result.permit)
        assertNull(result.permit.approvedBy)
    }
    //endregion

    //region mapExportAttendanceData
    @Test
    fun `mapExportAttendanceData should map correctly`() {
        // Arrange
        val response = GetExportAttendanceDataResponse.Data(
            sDateRange = listOf("2024-01-01"),
            data = listOf(
                GetExportAttendanceDataResponse.Data.Data(
                    fullName = "John Doe",
                    classX = "10A",
                    studentId = "123",
                    dataPerDate = listOf(
                        GetExportAttendanceDataResponse.Data.Data.Data(
                            date = "2024-01-01",
                            status = "ABSENT"
                        )
                    )
                )
            )
        )

        // Act
        val result = mapper.mapExportAttendanceData(response)

        // Assert
        assertEquals(1, result.sDateRange.size)
        assertEquals("2024-01-01", result.sDateRange.first())

        val item = result.data.first()
        assertEquals("John Doe", item.fullName)
        assertEquals(1, item.dataPerDate.size)
        assertEquals("ABSENT", item.dataPerDate.first().status)
    }
    //endregion
}