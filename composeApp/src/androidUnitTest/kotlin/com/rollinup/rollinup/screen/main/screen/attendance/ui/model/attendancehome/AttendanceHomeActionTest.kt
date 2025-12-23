package com.rollinup.rollinup.screen.main.screen.attendance.ui.model.attendancehome

import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceHomeAction
import org.junit.Assert.assertEquals
import org.junit.Test
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_document_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24

class AttendanceHomeActionTest {

    @Test
    fun `HISTORY_BY_STUDENT properties should be correct`() {
        // Arrange
        val action = AttendanceHomeAction.HISTORY_BY_STUDENT

        // Act & Assert
        assertEquals("History by student", action.label)
        assertEquals(Res.drawable.ic_document_line_24, action.icon)
    }

    @Test
    fun `DETAIL properties should be correct`() {
        // Arrange
        val action = AttendanceHomeAction.DETAIL

        // Act & Assert
        assertEquals("Detail", action.label)
        assertEquals(Res.drawable.ic_info_line_24, action.icon)
    }

    @Test
    fun `enum should have exactly 2 entries`() {
        // Act
        val values = AttendanceHomeAction.entries

        // Assert
        assertEquals(2, values.size)
    }
}