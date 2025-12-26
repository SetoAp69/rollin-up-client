package com.rollinup.apiservice.data.repository.auth

import com.rollinup.apiservice.data.mapper.AuthMapper
import com.rollinup.apiservice.data.source.network.apiservice.AuthApiService
import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.auth.LoginResponse
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.common.Role
import com.rollinup.apiservice.utils.Utils
import io.ktor.http.HttpStatusCode
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRepositoryTest {

    private lateinit var repository: AuthRepository

    @MockK
    private val dataSource: AuthApiService = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ioDispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    // Using real mapper
    private val mapper = AuthMapper()

    @Before
    fun init() {
        MockKAnnotations.init(this)

        // Mock Utils for error handling
        mockkObject(Utils)
        every { Utils.handleApiError(any<Exception>()) } returns Result.Error(NetworkError.RESPONSE_ERROR)

        repository = AuthRepositoryImpl(
            apiDataSource = dataSource,
            ioDispatcher = ioDispatcher,
            mapper = mapper
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    //region Login
    @Test
    fun `login should return Result Success Flow with mapped entity`() = runTest {
        // Arrange
        val body = LoginBody(email = "test@mail.com", password = "password")

        // Constructing complex nested response based on AuthMapper usage
        val mockUser = LoginResponse.Data.UserData(
            id = "u1",
            deviceId = "dev1",
            userName = "john",
            email = "test@mail.com",
            firstName = "John",
            lastName = "Doe",
            role = "student",
            gender = "Laki-laki",
            classX = "10A",
            classId = "c1",
            classKey = 101,
            isVerified = true
        )

        val mockResponseData = LoginResponse.Data(
            data = mockUser,
            refreshToken = "refresh_token",
            accessToken = "access_token"
        )

        val mockApiResponse = LoginResponse(
            status = 200,
            message = "OK",
            data = mockResponseData
        )

        val expectedEntity = LoginEntity(
            id = "u1",
            deviceId = "dev1",
            userName = "john",
            email = "test@mail.com",
            firstName = "John",
            lastName = "Doe",
            role = Role.STUDENT, // Assuming mapped from "student"
            gender = Gender.MALE, // Assuming mapped from "Laki-laki"
            refreshToken = "refresh_token",
            accessToken = "access_token",
            classX = "10A",
            classId = "c1",
            classKey = 101,
            isVerified = true
        )

        coEvery { dataSource.login(body) } returns ApiResponse.Success(
            mockApiResponse,
            HttpStatusCode.OK
        )

        // Act
        val result = repository.login(body).first()

        // Assert
        coVerify { dataSource.login(body) }
        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `login should return Result Error Flow`() = runTest {
        // Arrange
        val body = LoginBody(email = "test@mail.com", password = "wrong")
        coEvery { dataSource.login(body) } returns ApiResponse.Error(Exception("Unauthorized"))

        // Act
        val result = repository.login(body).first()

        // Assert
        coVerify { dataSource.login(body) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `login should return Result Error Flow when exception is thrown`() = runTest {
        // Arrange
        val body = LoginBody(email = "test@mail.com", password = "password")
        coEvery { dataSource.login(body) } throws RuntimeException("Network Error")

        // Act
        val result = repository.login(body).first()

        // Assert
        coVerify { dataSource.login(body) }
        assertTrue(result is Result.Error)
    }
    //endregion

    //region LoginJWT
    @Test
    fun `loginJWT should return Result Success Flow with mapped entity`() = runTest {
        // Arrange
        val token = "valid_token"

        // Reuse similar mock structure
        val mockUser = LoginResponse.Data.UserData(
            id = "u1", deviceId = "dev1", userName = "john", email = "test@mail.com",
            firstName = "John", lastName = "Doe", role = "student", gender = "Laki-laki",
            classX = "10A", classId = "c1", classKey = 101, isVerified = true
        )
        val mockResponseData = LoginResponse.Data(
            data = mockUser, refreshToken = "refresh", accessToken = "access"
        )
        val mockApiResponse = LoginResponse(status = 200, message = "OK", data = mockResponseData)

        val expectedEntity = LoginEntity(
            id = "u1", deviceId = "dev1", userName = "john", email = "test@mail.com",
            firstName = "John", lastName = "Doe", role = Role.STUDENT, gender = Gender.MALE,
            refreshToken = "refresh", accessToken = "access",
            classX = "10A", classId = "c1", classKey = 101, isVerified = true
        )

        coEvery { dataSource.loginJWT(token) } returns ApiResponse.Success(
            mockApiResponse,
            HttpStatusCode.OK
        )

        // Act
        val result = repository.loginJWT(token).first()

        // Assert
        coVerify { dataSource.loginJWT(token) }
        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `loginJWT should return Result Error Flow`() = runTest {
        // Arrange
        val token = "invalid_token"
        coEvery { dataSource.loginJWT(token) } returns ApiResponse.Error(Exception("Invalid Token"))

        // Act
        val result = repository.loginJWT(token).first()

        // Assert
        coVerify { dataSource.loginJWT(token) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `loginJWT should return Result Error Flow when exception is thrown`() = runTest {
        // Arrange
        val token = "token"
        coEvery { dataSource.loginJWT(token) } throws RuntimeException("Crash")

        // Act
        val result = repository.loginJWT(token).first()

        // Assert
        coVerify { dataSource.loginJWT(token) }
        assertTrue(result is Result.Error)
    }
    //endregion

    //region UpdatePasswordAndDevice
    @Test
    fun `updatePasswordAndDevice should return Result Success Unit`() = runTest {
        // Arrange
        val id = "u1"
        val token = "token"
        val body = UpdatePasswordAndVerificationBody(password = "newPass", token = "1234")

        coEvery {
            dataSource.updatePasswordAndDevice(id, token, body)
        } returns ApiResponse.Success(Unit, HttpStatusCode.OK)

        // Act
        val result = repository.updatePasswordAndDevice(id, body, token).first()

        // Assert
        coVerify { dataSource.updatePasswordAndDevice(id, token, body) }
        assertTrue(result is Result.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `updatePasswordAndDevice should return Result Error Flow`() = runTest {
        // Arrange
        val id = "u1"
        val token = "token"
        val body = UpdatePasswordAndVerificationBody(password = "newPass")

        coEvery {
            dataSource.updatePasswordAndDevice(id, token, body)
        } returns ApiResponse.Error(Exception("Failed"))

        // Act
        val result = repository.updatePasswordAndDevice(id, body, token).first()

        // Assert
        assertTrue(result is Result.Error)
    }

    @Test
    fun `updatePasswordAndDevice should return Result Error Flow when exception is thrown`() =
        runTest {
            // Arrange
            val id = "u1"
            val token = "token"
            val body = UpdatePasswordAndVerificationBody(password = "newPass")

            coEvery {
                dataSource.updatePasswordAndDevice(id, token, body)
            } throws RuntimeException("Error")

            // Act
            val result = repository.updatePasswordAndDevice(id, body, token).first()

            // Assert
            assertTrue(result is Result.Error)
        }
    //endregion

    //region ClearClientToken
    @Test
    fun `clearClientToken should call logout on datasource`() = runTest {
        // Arrange
        coEvery { dataSource.logout() } returns Unit

        // Act
        repository.clearClientToken()

        // Assert
        coVerify { dataSource.logout() }
    }
    //endregion
}