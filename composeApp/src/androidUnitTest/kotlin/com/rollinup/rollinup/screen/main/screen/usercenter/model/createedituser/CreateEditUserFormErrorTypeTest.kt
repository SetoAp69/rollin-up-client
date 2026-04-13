package com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser

import org.junit.Assert.assertEquals
import org.junit.Test

class CreateEditUserFormErrorTypeTest {

    @Test
    fun `enum should contain all expected values`() {
        val expectedValues = listOf(
            "EMAIL_EMPTY",
            "EMAIL_INVALID",
            "EMAIL_EXIST",
            "FULL_NAME_EMPTY",
            "FULL_NAME_MAX",
            "USERNAME_EMPTY",
            "USERNAME_EXIST",
            "USERNAME_INVALID",
            "STUDENT_ID_EMPTY",
            "STUDENT_ID_INVALID",
            "FIELD_INCOMPLETE"
        )
        val actualValues = CreateEditUserFormErrorType.values().map { it.name }
        
        assertEquals("Enum should have exactly ${expectedValues.size} values", expectedValues.size, actualValues.size)
        expectedValues.forEach { expected ->
            assert(actualValues.contains(expected)) { "Enum should contain $expected" }
        }
    }
}
