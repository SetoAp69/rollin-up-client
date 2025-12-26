package com.rollinup.apiservice.model.attendance

import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AttendanceEntityTest {

    @Before
    fun setup() {
        mockkObject(Utils)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `AttendanceByClassEntity Permit durationString should format DISPENSATION correctly`() {
        // Arrange
        val startStr = "2003-08-06T09:00:00+07:00"
        val endStr = "2003-08-06T09:03:00+07:00"

        val permit = AttendanceByClassEntity.Permit(
            type = PermitType.DISPENSATION,
            start = startStr,
            end = endStr
        )

        // Act
        val result = permit.durationString

        // Assert
        assertEquals("09:00 - 09:03", result)
    }

    @Test
    fun `AttendanceByClassEntity Permit durationString should format ABSENCE same date correctly`() {
        // Arrange
        val startStr = "2003-04-07T09:00:00+07:00"
        val endStr = "2003-08-06T09:00:00+07:00"

        val permit = AttendanceByClassEntity.Permit(
            type = PermitType.ABSENCE,
            start = startStr,
            end = endStr
        )

        // Act
        val result = permit.durationString

        // Assert
        assertEquals("2003-04-07 - 2003-08-06", result)
    }

    @Test
    fun `AttendanceByStudentEntity Permit durationString should format ABSENCE diff date correctly`() {
        // Arrange
        val startStr = "2003-08-06T09:00:00+07:00"
        val endStr = "2003-08-06T09:00:00+07:00"

        val permit = AttendanceByStudentEntity.Permit(
            type = PermitType.ABSENCE,
            start = startStr,
            end = endStr
        )

        // Act
        val result = permit.durationString

        // Assert
        assertEquals("2003-08-06", result)
    }

    @Test
    fun `AttendanceByStudentEntity localDate property should parse correctly`() {
        // Arrange
        val entity = AttendanceByStudentEntity(date = "2024-12-25")

        // Act
        val result = entity.localDate

        // Assert
        assertEquals(LocalDate(2024, 12, 25), result)
    }
}