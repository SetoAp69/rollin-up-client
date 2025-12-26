package com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit

import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.common.model.Severity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_edit_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24

class StudentPermitActionTest {

    // 1. Test EDIT Action
    @Test
    fun `EDIT properties and show logic should be correct`() {
        val action = StudentPermitAction.EDIT

        // Properties
        assertEquals("Edit permit", action.label)
        assertEquals(Severity.PRIMARY, action.severity) // Default
        assertEquals(Res.drawable.ic_edit_line_24, action.icon)

        // Show Logic: Only visible if pending
        assertTrue(action.show(ApprovalStatus.APPROVAL_PENDING))

        assertFalse(action.show(ApprovalStatus.APPROVED))
        assertFalse(action.show(ApprovalStatus.DECLINED))
    }

    // 2. Test CANCEL Action
    @Test
    fun `CANCEL properties and show logic should be correct`() {
        val action = StudentPermitAction.CANCEL

        // Properties
        assertEquals("Cancel permit", action.label)
        assertEquals(Severity.PRIMARY, action.severity) // Default
        assertEquals(Res.drawable.ic_edit_line_24, action.icon)

        // Show Logic: Only visible if pending
        assertTrue(action.show(ApprovalStatus.APPROVAL_PENDING))

        assertFalse(action.show(ApprovalStatus.APPROVED))
        assertFalse(action.show(ApprovalStatus.DECLINED))
    }

    // 3. Test DETAIL Action
    @Test
    fun `DETAIL properties and show logic should be correct`() {
        val action = StudentPermitAction.DETAIL

        // Properties
        assertEquals("Detail", action.label)
        assertEquals(Severity.PRIMARY, action.severity) // Default
        assertEquals(Res.drawable.ic_info_line_24, action.icon)

        // Show Logic: Always visible
        assertTrue(action.show(ApprovalStatus.APPROVAL_PENDING))
        assertTrue(action.show(ApprovalStatus.APPROVED))
        assertTrue(action.show(ApprovalStatus.DECLINED))
    }
}