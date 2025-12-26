package com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard

import com.rollinup.apiservice.model.attendance.AttendanceStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_check_line_24
import rollin_up.composeapp.generated.resources.ic_document_line_24
import rollin_up.composeapp.generated.resources.ic_edit_line_24

class TeacherDashboardActionTest {

    // 1. Test Properties (Label & Icon)
    @Test
    fun `EDIT_DATA properties should be correct`() {
        val action = TeacherDashboardAction.EDIT_DATA
        assertEquals("Edit data", action.label)
        assertEquals(Res.drawable.ic_edit_line_24, action.icon)
    }

    @Test
    fun `APPROVAL properties should be correct`() {
        val action = TeacherDashboardAction.APPROVAL
        assertEquals("Approval", action.label)
        assertEquals(Res.drawable.ic_check_line_24, action.icon)
    }

    @Test
    fun `DETAIL properties should be correct`() {
        val action = TeacherDashboardAction.DETAIL
        assertEquals("Detail", action.label)
        assertEquals(Res.drawable.ic_document_line_24, action.icon)
    }

    // 2. Test Logic: EDIT_DATA
    // Logic: status.size == 1 && status.first() != AttendanceStatus.APPROVAL_PENDING

    @Test
    fun `EDIT_DATA show returns true when single item is NOT Approval Pending`() {
        val action = TeacherDashboardAction.EDIT_DATA

        assertTrue(action.show(listOf(AttendanceStatus.LATE)))
        assertTrue(action.show(listOf(AttendanceStatus.ON_TIME)))
        assertTrue(action.show(listOf(AttendanceStatus.ABSENT)))
        assertTrue(action.show(listOf(AttendanceStatus.EXCUSED)))
        assertTrue(action.show(listOf(AttendanceStatus.NO_DATA)))
    }

    @Test
    fun `EDIT_DATA show returns false when single item IS Approval Pending`() {
        val action = TeacherDashboardAction.EDIT_DATA

        // Should hide because we can't edit pending approvals (likely handled by Approval action)
        assertFalse(action.show(listOf(AttendanceStatus.APPROVAL_PENDING)))
    }

    @Test
    fun `EDIT_DATA show returns false when list is empty`() {
        val action = TeacherDashboardAction.EDIT_DATA

        // Size != 1 (Short-circuits before calling .first(), preventing crash)
        assertFalse(action.show(emptyList()))
    }

    @Test
    fun `EDIT_DATA show returns false when multiple items selected`() {
        val action = TeacherDashboardAction.EDIT_DATA

        // Size > 1
        assertFalse(action.show(listOf(AttendanceStatus.LATE, AttendanceStatus.LATE)))
    }

    // 3. Test Logic: APPROVAL
    // Logic: status.all { it == AttendanceStatus.APPROVAL_PENDING }

    @Test
    fun `APPROVAL show returns true when ALL items are Approval Pending`() {
        val action = TeacherDashboardAction.APPROVAL

        // Single item
        assertTrue(action.show(listOf(AttendanceStatus.APPROVAL_PENDING)))

        // Multiple items (Batch approval)
        assertTrue(
            action.show(
                listOf(
                    AttendanceStatus.APPROVAL_PENDING,
                    AttendanceStatus.APPROVAL_PENDING
                )
            )
        )
    }

    @Test
    fun `APPROVAL show returns false when ANY item is NOT Approval Pending`() {
        val action = TeacherDashboardAction.APPROVAL

        // Single valid item
        assertFalse(action.show(listOf(AttendanceStatus.LATE)))

        // Mixed list (One pending, one not)
        assertFalse(action.show(listOf(AttendanceStatus.APPROVAL_PENDING, AttendanceStatus.LATE)))
    }

    @Test
    fun `APPROVAL show returns true for empty list`() {
        val action = TeacherDashboardAction.APPROVAL

        // Standard Kotlin behavior: emptyList().all { predicate } is true.
        // This validates the code behavior "as written".
        assertTrue(action.show(emptyList()))
    }

    // 4. Test Logic: DETAIL
    // Logic: status.size == 1

    @Test
    fun `DETAIL show returns true when exactly one item is selected`() {
        val action = TeacherDashboardAction.DETAIL

        assertTrue(action.show(listOf(AttendanceStatus.LATE)))
        assertTrue(action.show(listOf(AttendanceStatus.APPROVAL_PENDING)))
    }

    @Test
    fun `DETAIL show returns false when list is empty`() {
        val action = TeacherDashboardAction.DETAIL

        assertFalse(action.show(emptyList()))
    }

    @Test
    fun `DETAIL show returns false when multiple items are selected`() {
        val action = TeacherDashboardAction.DETAIL

        assertFalse(action.show(listOf(AttendanceStatus.LATE, AttendanceStatus.ABSENT)))
    }
}