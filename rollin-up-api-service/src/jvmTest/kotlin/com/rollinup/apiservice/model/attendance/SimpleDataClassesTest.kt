package com.rollinup.apiservice.model.attendance

import com.rollinup.apiservice.model.permit.PermitType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SimpleDataClassesTest {

    @Test
    fun `DashboardDataEntity instantiation`() {
        val summary = AttendanceSummaryEntity(checkedIn = 5)
        val entity = DashboardDataEntity(
            attendanceStatus = AttendanceStatus.ON_TIME,
            summary = summary
        )

        assertEquals(AttendanceStatus.ON_TIME, entity.attendanceStatus)
        assertEquals(5L, entity.summary.checkedIn)
    }

    @Test
    fun `AttendanceSummaryEntity defaults`() {
        val entity = AttendanceSummaryEntity()
        assertEquals(0L, entity.checkedIn)
        assertEquals(0L, entity.late)
        assertEquals(0L, entity.absent)
        assertEquals(0L, entity.sick)
    }

    @Test
    fun `AttendanceDetailEntity defaults and nested objects`() {
        val entity = AttendanceDetailEntity()

        // Defaults
        assertEquals("", entity.id)
        assertEquals(AttendanceStatus.NO_DATA, entity.status)
        assertNotNull(entity.student)
        assertNull(entity.permit)

        // Nested User
        assertEquals("", entity.student.name)
    }

    @Test
    fun `AttendanceDetailEntity Permit structure`() {
        val permit = AttendanceDetailEntity.Permit(
            id = "p1",
            type = PermitType.ABSENCE,
            attachment = "link"
        )

        assertEquals("p1", permit.id)
        assertEquals(PermitType.ABSENCE, permit.type)
        assertEquals("link", permit.attachment)
        assertNull(permit.approvedBy)
    }
}