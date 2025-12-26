package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.data.source.network.model.response.user.GetUserByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserListResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserOptionsResponse
import com.rollinup.apiservice.model.common.Gender
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserMapperTest {

    private lateinit var mapper: UserMapper

    @Before
    fun setup() {
        mapper = UserMapper()
    }

    //region mapGetUserList
    @Test
    fun `mapGetUserList should map valid data correctly`() {
        // Arrange
        val response = listOf(
            GetUserListResponse.Data.UserData(
                id = "u1",
                userName = "john_d",
                classX = "10A",
                email = "john@mail.com",
                firstName = "John",
                lastName = "Doe",
                studentId = "123",
                address = "Street 1",
                role = "student",
                gender = "Laki-laki"
            )
        )

        // Act
        val result = mapper.mapGetUserList(response)

        // Assert
        val entity = result.first()
        assertEquals("John Doe", entity.fullName)
        assertEquals("10A", entity.classX)
        assertEquals("123", entity.studentId)
        assertEquals(Gender.MALE, entity.gender)
    }

    @Test
    fun `mapGetUserList should handle nulls for classX and studentId`() {
        // Arrange
        val response = listOf(
            GetUserListResponse.Data.UserData(
                id = "u1",
                userName = "john_d",
                classX = null, // Testing null
                email = "john@mail.com",
                firstName = "John",
                lastName = "Doe",
                studentId = null, // Testing null
                address = "Street 1",
                role = "admin",
                gender = "Perempuan"
            )
        )

        // Act
        val result = mapper.mapGetUserList(response)

        // Assert
        val entity = result.first()
        assertEquals("", entity.classX)
        assertEquals("", entity.studentId)
    }
    //endregion

    //region mapGetUserById
    @Test
    fun `mapGetUserById should map full data correctly`() {
        // Arrange
        val response = GetUserByIdResponse.Data(
            id = "u1",
            username = "john_d",
            firstName = "John",
            lastName = "Doe",
            classX = GetUserByIdResponse.Data.Class(id = "c1", name = "10A", key = 101),
            email = "john@mail.com",
            studentId = "123",
            address = "Street",
            gender = "Laki-laki",
            phoneNumber = "08123",
            birthday = "2000-01-01",
            role = GetUserByIdResponse.Data.Role(id = "r1", name = "Student", key = 1)
        )

        // Act
        val result = mapper.mapGetUserById(response)

        // Assert
        assertEquals("John Doe", result.fullName)
        assertEquals("123", result.studentId)
        assertEquals("08123", result.phoneNumber)
        assertEquals("10A", result.classX?.name)
    }

    @Test
    fun `mapGetUserById should handle nulls`() {
        // Arrange
        val response = GetUserByIdResponse.Data(
            id = "u1",
            username = "john_d",
            firstName = "John",
            lastName = "Doe",
            classX = null, // Testing null
            email = "john@mail.com",
            studentId = null, // Testing null
            address = "Street",
            gender = "Laki-laki",
            phoneNumber = null, // Testing null
            birthday = "2000-01-01",
            role = GetUserByIdResponse.Data.Role(id = "r1", name = "Admin", key = 2)
        )

        // Act
        val result = mapper.mapGetUserById(response)

        // Assert
        assertNull(result.classX)
        assertEquals("-", result.studentId)
        assertEquals("", result.phoneNumber)
    }
    //endregion

    //region mapUserOptions
    @Test
    fun `mapUserOptions should map option lists`() {
        // Arrange
        val response = GetUserOptionsResponse.Data(
            rolesOptions = listOf(GetUserOptionsResponse.Data.Data(label = "Student", value = 1)),
            classOptions = listOf(GetUserOptionsResponse.Data.Data(label = "10A", value = 1)),
            rolesIdOptions = listOf(GetUserOptionsResponse.Data.Data(label = "Student", value = "1")),
            classIdOptions = listOf(GetUserOptionsResponse.Data.Data(label = "10A", value = "101"))
        )

        // Act
        val result = mapper.mapUserOptions(response)

        // Assert
        assertEquals(1, result.roleOptions.size)
        assertEquals("Student", result.roleOptions.first().label)
        assertEquals("10A", result.classIdOptions.first().label)
    }
    //endregion
}