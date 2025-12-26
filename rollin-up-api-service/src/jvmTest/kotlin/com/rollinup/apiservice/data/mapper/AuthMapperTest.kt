package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.data.source.network.model.response.auth.LoginResponse
import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.common.Role
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthMapperTest {

    private lateinit var mapper: AuthMapper

    @Before
    fun setup() {
        mapper = AuthMapper()
    }

    @Test
    fun `mapLoginResponse should map all fields correctly`() {
        // Arrange
        val mockUser = LoginResponse.Data.UserData(
            id = "user123",
            deviceId = "device_001",
            userName = "jdoe",
            email = "jdoe@example.com",
            firstName = "John",
            lastName = "Doe",
            role = "student",
            gender = "Laki-laki",
            classX = "10A",
            classId = "class_1",
            classKey = 101,
            isVerified = true
        )

        val responseData = LoginResponse.Data(
            data = mockUser,
            refreshToken = "refresh_token_xyz",
            accessToken = "access_token_abc"
        )

        // Act
        val result = mapper.mapLoginResponse(responseData)

        // Assert
        assertEquals("user123", result.id)
        assertEquals("device_001", result.deviceId)
        assertEquals("jdoe", result.userName)
        assertEquals("jdoe@example.com", result.email)
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        assertEquals(Role.STUDENT, result.role) 
        assertEquals(Gender.MALE, result.gender)
        assertEquals("refresh_token_xyz", result.refreshToken)
        assertEquals("access_token_abc", result.accessToken)
        assertEquals("10A", result.classX)
        assertEquals("class_1", result.classId)
        assertEquals(101, result.classKey)
        assertEquals(true, result.isVerified)
    }

    @Test
    fun `mapLoginResponse should handle null deviceId with empty string default`() {
        // Arrange
        val mockUser = LoginResponse.Data.UserData(
            id = "user123",
            deviceId = null,
            userName = "jdoe",
            email = "jdoe@example.com",
            firstName = "John",
            lastName = "Doe",
            role = "student",
            gender = "Laki-laki",
            classX = null,
            classId = null,
            classKey = null,
            isVerified = false
        )

        val responseData = LoginResponse.Data(
            data = mockUser,
            refreshToken = "refresh",
            accessToken = "access"
        )

        // Act
        val result = mapper.mapLoginResponse(responseData)

        // Assert
        assertEquals("", result.deviceId)
    }

    @Test
    fun `mapLoginResponse should handle null class properties`() {
        // Arrange
        val mockUser = LoginResponse.Data.UserData(
            id = "user123",
            deviceId = "dev1",
            userName = "jdoe",
            email = "jdoe@example.com",
            firstName = "John",
            lastName = "Doe",
            role = "admin", 
            gender = "Perempuan",
            classX = null, // Testing nulls
            classId = null,
            classKey = null,
            isVerified = true
        )

        val responseData = LoginResponse.Data(
            data = mockUser,
            refreshToken = "refresh",
            accessToken = "access"
        )

        // Act
        val result = mapper.mapLoginResponse(responseData)

        // Assert
        assertNull(result.classX)
        assertNull(result.classId)
        assertNull(result.classKey)
    }
}