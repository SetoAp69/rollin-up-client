package com.rollinup.apiservice.data.repository.token

import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TokenRepositoryTest {

    private lateinit var repository: TokenRepository

    @MockK
    private val localDataStore: LocalDataStore = mockk()

    @Before
    fun init() {
        MockKAnnotations.init(this)
        repository = TokenRepositoryImpl(localDataStore)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    //region Access Token
    @Test
    fun `getToken should return token from local data store`() = runTest {
        // Arrange
        val expectedToken = "access_token_123"
        coEvery { localDataStore.getToken() } returns expectedToken

        // Act
        val result = repository.getToken()

        // Assert
        assertEquals(expectedToken, result)
        coVerify { localDataStore.getToken() }
    }

    @Test
    fun `updateToken should call update on local data store`() = runTest {
        // Arrange
        val newToken = "new_access_token"
        coEvery { localDataStore.updateToken(newToken) } returns Unit

        // Act
        repository.updateToken(newToken)

        // Assert
        coVerify { localDataStore.updateToken(newToken) }
    }

    @Test
    fun `clearToken should call clear on local data store`() = runTest {
        // Arrange
        coEvery { localDataStore.clearToken() } returns Unit

        // Act
        repository.clearToken()

        // Assert
        coVerify { localDataStore.clearToken() }
    }
    //endregion

    //region Refresh Token
    @Test
    fun `getRefreshToken should return token from local data store`() = runTest {
        // Arrange
        val expectedToken = "refresh_token_abc"
        coEvery { localDataStore.getRefreshToken() } returns expectedToken

        // Act
        val result = repository.getRefreshToken()

        // Assert
        assertEquals(expectedToken, result)
        coVerify { localDataStore.getRefreshToken() }
    }

    @Test
    fun `updateRefreshToken should call update on local data store`() = runTest {
        // Arrange
        val newToken = "new_refresh_token"
        coEvery { localDataStore.updateRefreshToken(newToken) } returns Unit

        // Act
        repository.updateRefreshToken(newToken)

        // Assert
        coVerify { localDataStore.updateRefreshToken(newToken) }
    }

    @Test
    fun `clearRefreshToken should call clear on local data store`() = runTest {
        // Arrange
        coEvery { localDataStore.clearRefreshToken() } returns Unit

        // Act
        repository.clearRefreshToken()

        // Assert
        coVerify { localDataStore.clearRefreshToken() }
    }
    //endregion
}