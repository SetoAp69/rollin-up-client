package com.rollinup.apiservice.model.attendance

import com.rollinup.common.model.Severity
import org.junit.Test
import kotlin.test.assertEquals

class AttendanceStatusTest {

    @Test
    fun `fromValue should return correct status for valid values`() {
        assertEquals(AttendanceStatus.ON_TIME, AttendanceStatus.fromValue("checked_in"))
        assertEquals(AttendanceStatus.LATE, AttendanceStatus.fromValue("late"))
        assertEquals(AttendanceStatus.ABSENT, AttendanceStatus.fromValue("absent"))
        assertEquals(AttendanceStatus.EXCUSED, AttendanceStatus.fromValue("excused"))
        assertEquals(
            AttendanceStatus.APPROVAL_PENDING,
            AttendanceStatus.fromValue("approval_pending")
        )
        assertEquals(AttendanceStatus.NO_DATA, AttendanceStatus.fromValue("no_data"))
    }

    @Test
    fun `fromValue should be case insensitive`() {
        assertEquals(AttendanceStatus.ON_TIME, AttendanceStatus.fromValue("CHECKED_IN"))
        assertEquals(AttendanceStatus.LATE, AttendanceStatus.fromValue("LATE"))
    }

    @Test
    fun `fromValue should return NO_DATA for unknown values`() {
        assertEquals(AttendanceStatus.NO_DATA, AttendanceStatus.fromValue("unknown_status"))
        assertEquals(AttendanceStatus.NO_DATA, AttendanceStatus.fromValue(""))
    }

    @Test
    fun `check properties of enum entries`() {
        val status = AttendanceStatus.ON_TIME
        assertEquals("checked_in", status.value)
        assertEquals("On Time", status.label)
        assertEquals(Severity.SUCCESS, status.severity)
    }
}