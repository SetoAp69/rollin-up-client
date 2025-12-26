package com.rollinup.apiservice.domain.auth

import com.rollinup.apiservice.data.repository.auth.AuthRepository
import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AuthUseCaseTest {

    @MockK
    private lateinit var repository: AuthRepository

    private lateinit var loginJWTUseCase: LoginJWTUseCase
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var updatePasswordAndDeviceUseCase: UpdatePasswordAndDeviceUseCase
    private lateinit var clearClientTokenUseCase: ClearClientTokenUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginJWTUseCase = LoginJWTUseCase(repository)
        loginUseCase = LoginUseCase(repository)
        updatePasswordAndDeviceUseCase = UpdatePasswordAndDeviceUseCase(repository)
        clearClientTokenUseCase = ClearClientTokenUseCase(repository)
    }

    @Test
    fun `LoginJWTUseCase should call repository`() {
        // Arrange
        val token = "token_123"
        val mockEntity = mockk<LoginEntity>()
        val expectedFlow = flowOf(Result.Success(mockEntity))
        every { repository.loginJWT(token) } returns expectedFlow

        // Act
        val result = loginJWTUseCase(token)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.loginJWT(token) }
    }

    @Test
    fun `LoginUseCase should call repository`() {
        // Arrange
        val body = LoginBody(email = "test@mail.com", password = "pass")
        val mockEntity = mockk<LoginEntity>()
        val expectedFlow = flowOf(Result.Success(mockEntity))
        every { repository.login(body) } returns expectedFlow

        // Act
        val result = loginUseCase(body)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.login(body) }
    }

    @Test
    fun `UpdatePasswordAndDeviceUseCase should call repository`() {
        // Arrange
        val id = "user_1"
        val token = "token_abc"
        val body = UpdatePasswordAndVerificationBody(password = "newPass")
        val expectedFlow = flowOf(Result.Success(Unit))
        
        every { repository.updatePasswordAndDevice(id, body, token) } returns expectedFlow

        // Act
        val result = updatePasswordAndDeviceUseCase(id, body, token)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.updatePasswordAndDevice(id, body, token) }
    }

    @Test
    fun `ClearClientTokenUseCase should call repository`() = runTest {
        // Arrange
        coEvery { repository.clearClientToken() } returns Unit

        // Act
        clearClientTokenUseCase()

        // Assert
        coVerify(exactly = 1) { repository.clearClientToken() }
    }
}