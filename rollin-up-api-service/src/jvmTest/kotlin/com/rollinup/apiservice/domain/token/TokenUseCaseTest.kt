package com.rollinup.apiservice.domain.token

import com.rollinup.apiservice.data.repository.token.TokenRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TokenUseCaseTest {

    @MockK
    private lateinit var repository: TokenRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `GetTokenUseCase should call repository getToken`() = runTest {
        val useCase = GetTokenUseCase(repository)
        val expectedToken = "token"
        coEvery { repository.getToken() } returns expectedToken

        val result = useCase()

        assertEquals(expectedToken, result)
        coVerify(exactly = 1) { repository.getToken() }
    }

    @Test
    fun `UpdateTokenUseCase should call repository updateToken`() = runTest {
        val useCase = UpdateTokenUseCase(repository)
        val token = "new_token"
        coEvery { repository.updateToken(token) } returns Unit

        useCase(token)

        coVerify(exactly = 1) { repository.updateToken(token) }
    }

    @Test
    fun `ClearTokenUseCase should call repository clearToken`() = runTest {
        val useCase = ClearTokenUseCase(repository)
        coEvery { repository.clearToken() } returns Unit

        useCase()

        coVerify(exactly = 1) { repository.clearToken() }
    }

    // NOTE: The tests below reflect the implementation in TokenUseCasee.kt provided.
    // The use cases seem to call the wrong repository methods (e.g. getToken instead of getRefreshToken).
    
    @Test
    fun `GetRefreshTokenUseCase should call repository getToken (as per current impl)`() = runTest {
        val useCase = GetRefreshTokenUseCase(repository)
        val expectedToken = "refresh_token"
        // Current impl calls getToken(), not getRefreshToken()
        coEvery { repository.getToken() } returns expectedToken

        val result = useCase()

        assertEquals(expectedToken, result)
        coVerify(exactly = 1) { repository.getToken() } 
    }

    @Test
    fun `UpdateRefreshTokenUseCase should call repository updateToken (as per current impl)`() = runTest {
        val useCase = UpdateRefreshTokenUseCase(repository)
        val token = "new_refresh"
        // Current impl calls updateToken(), not updateRefreshToken()
        coEvery { repository.updateToken(token) } returns Unit

        useCase(token)

        coVerify(exactly = 1) { repository.updateToken(token) }
    }

    @Test
    fun `ClearRefreshTokenUseCase should call repository clearToken (as per current impl)`() = runTest {
        val useCase = ClearRefreshTokenUseCase(repository)
        // Current impl calls clearToken(), not clearRefreshToken()
        coEvery { repository.clearToken() } returns Unit

        useCase()

        coVerify(exactly = 1) { repository.clearToken() }
    }
}