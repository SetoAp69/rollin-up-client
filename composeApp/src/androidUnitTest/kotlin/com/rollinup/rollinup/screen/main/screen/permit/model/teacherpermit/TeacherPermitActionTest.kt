package com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit

import com.rollinup.apiservice.model.permit.PermitByClassEntity
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.ic_user_check_line_24

class TeacherPermitActionTest {

    // 1. APPROVAL Action Tests
    @Test
    fun `APPROVAL properties should be correct`() {
        val action = TeacherPermitAction.APPROVAL
        assertEquals("Approval", action.label)
        assertEquals(Res.drawable.ic_user_check_line_24, action.icon)
    }

    @Test
    fun `APPROVAL show returns true when isActive is true`() {
        val action = TeacherPermitAction.APPROVAL
        val list = listOf(mockk<PermitByClassEntity>())

        assertTrue(action.show(list, true))
        assertTrue(action.show(emptyList(), true))
    }

    @Test
    fun `APPROVAL show returns false when isActive is false`() {
        val action = TeacherPermitAction.APPROVAL
        val list = listOf(mockk<PermitByClassEntity>())

        assertFalse(action.show(list, false))
        assertFalse(action.show(emptyList(), false))
    }

    // 2. DETAIL Action Tests
    @Test
    fun `DETAIL properties should be correct`() {
        val action = TeacherPermitAction.DETAIL
        assertEquals("Detail", action.label)
        assertEquals(Res.drawable.ic_info_line_24, action.icon)
    }

    @Test
    fun `DETAIL show returns true when list size is exactly 1`() {
        val action = TeacherPermitAction.DETAIL
        val singleItemList = listOf(mockk<PermitByClassEntity>())

        // Logic: { list, _ -> list.size == 1 }
        // Should ignore isActive state
        assertTrue(action.show(singleItemList, true))
        assertTrue(action.show(singleItemList, false))
    }

    @Test
    fun `DETAIL show returns false when list size is empty or multiple`() {
        val action = TeacherPermitAction.DETAIL
        val emptyList = emptyList<PermitByClassEntity>()
        val multiList = listOf(mockk<PermitByClassEntity>(), mockk<PermitByClassEntity>())

        // Size 0
        assertFalse(action.show(emptyList, true))
        
        // Size 2
        assertFalse(action.show(multiList, true))
    }
}