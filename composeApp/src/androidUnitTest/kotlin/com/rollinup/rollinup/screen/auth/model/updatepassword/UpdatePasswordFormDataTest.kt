package com.rollinup.rollinup.screen.auth.model.updatepassword

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdatePasswordFormDataTest {

    // 1. Test Default Values (Happy Path for initialization)
    @Test
    fun `default values should be initialized correctly`() {
        val formData = UpdatePasswordFormData()

        assertEquals("", formData.userId)
        assertEquals("", formData.passwordOne)
        assertEquals("", formData.passwordTwo)
        assertNull(formData.deviceId)
        assertNull(formData.passwordOneError)
        assertNull(formData.passwordTwoError)
        // Default state should be valid as errors are null
        assertTrue(formData.isValid)
    }

    // 2. Test isValid Logic - Branch: All null (Success)
    @Test
    fun `isValid should return true when there are no errors`() {
        // Arrange
        val formData = UpdatePasswordFormData(
            passwordOneError = null,
            passwordTwoError = null
        )

        // Act & Assert
        assertTrue("Should be valid when both errors are null", formData.isValid)
    }

    // 3. Test isValid Logic - Branch: passwordOneError is not null
    @Test
    fun `isValid should return false when passwordOneError is present`() {
        // Arrange
        val formData = UpdatePasswordFormData(
            passwordOneError = "Password cannot be empty",
            passwordTwoError = null
        )

        // Act & Assert
        assertFalse("Should be invalid when passwordOneError is set", formData.isValid)
    }

    // 4. Test isValid Logic - Branch: passwordTwoError is not null
    @Test
    fun `isValid should return false when passwordTwoError is present`() {
        // Arrange
        val formData = UpdatePasswordFormData(
            passwordOneError = null,
            passwordTwoError = "Passwords do not match"
        )

        // Act & Assert
        assertFalse("Should be invalid when passwordTwoError is set", formData.isValid)
    }

    // 5. Test isValid Logic - Branch: Both errors are not null
    @Test
    fun `isValid should return false when both errors are present`() {
        // Arrange
        val formData = UpdatePasswordFormData(
            passwordOneError = "Error 1",
            passwordTwoError = "Error 2"
        )

        // Act & Assert
        assertFalse("Should be invalid when both errors are set", formData.isValid)
    }

    // 6. Test Data Holding (Verify constructor/copy works as expected)
    @Test
    fun `data class should hold provided values correctly`() {
        // Arrange
        val expectedUserId = "user_123"
        val expectedPass1 = "secret"
        val expectedPass2 = "secret"
        val expectedDeviceId = "device_001"

        // Act
        val formData = UpdatePasswordFormData(
            userId = expectedUserId,
            passwordOne = expectedPass1,
            passwordTwo = expectedPass2,
            deviceId = expectedDeviceId
        )

        // Assert
        assertEquals(expectedUserId, formData.userId)
        assertEquals(expectedPass1, formData.passwordOne)
        assertEquals(expectedPass2, formData.passwordTwo)
        assertEquals(expectedDeviceId, formData.deviceId)
    }
}