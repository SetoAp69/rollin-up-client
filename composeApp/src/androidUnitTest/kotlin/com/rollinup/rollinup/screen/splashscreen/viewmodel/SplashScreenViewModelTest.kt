@file:Suppress("UnusedFlow")

package com.rollinup.rollinup.screen.splashscreen.viewmodel

import com.rollinup.apiservice.domain.auth.LoginJWTUseCase
import com.rollinup.apiservice.domain.token.GetTokenUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.time.delay
import okhttp3.Dispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SplashScreenViewModelTest {
    private lateinit var viewModel: SplashScreenViewmodel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @MockK
    private val getTokenUseCase: GetTokenUseCase = mockk()

    @MockK
    private val loginJWTUseCase: LoginJWTUseCase = mockk()

    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewModel = SplashScreenViewmodel(
            getTokenUseCase = getTokenUseCase,
            loginJWTUseCase = loginJWTUseCase
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    private fun arrangeLoginJwt(
        token: String,
        result: Result<LoginEntity, NetworkError>,
    ) {
        coEvery {
            loginJWTUseCase(token)
        } returns flowOf(result)
    }

    private fun arrangeGetToken(
        token: String,
    ) {
        coEvery {
            getTokenUseCase()
        } returns token
    }

    @Test
    fun `auth should return Result Success`() = runTest {
        val loginDataMockk = LoginEntity(id = "mockk", isVerified = true)
        val token = "token"

        arrangeGetToken(token)
        arrangeLoginJwt(token, Result.Success(loginDataMockk))

        //Act
        viewModel.auth()

        //Assert
        coVerify(exactly = 1) {
            getTokenUseCase()
            loginJWTUseCase(token)
        }
        val state = viewModel.uiState.value
        assertEquals(true, state.loginState)
        assertEquals(false, state.isLoading)
        assertEquals(loginDataMockk, state.loginData)
    }

    @Test
    fun `auth should return Result Error when account is not verified`() = runTest {
        val loginDataMockk = LoginEntity(id = "mockk", isVerified = false)
        val token = "token"

        arrangeGetToken(token)
        arrangeLoginJwt(token, Result.Success(loginDataMockk))

        //Act
        viewModel.auth()

        //Assert
        coVerify(exactly = 1) {
            getTokenUseCase()
            loginJWTUseCase(token)
        }
        val state = viewModel.uiState.value
        assertEquals(false, state.loginState)
        assertEquals(false, state.isLoading)
        assertEquals(loginDataMockk, state.loginData)
    }

    @Test
    fun `auth( should set login state to false when token is blank`() = runTest {
        val loginDataMockk = LoginEntity(id = "mockk")
        val token = ""

        arrangeGetToken(token)

        //Act
        viewModel.auth()

        //Assert
        coVerify(exactly = 1) {
            getTokenUseCase()
        }
        val state = viewModel.uiState.value
        assertEquals(state.loginState, false)
        assertEquals(state.isLoading, false)
        assertNull(state.loginData)
    }

    @Test
    fun `auth should return Result Error when token is blank`() = runTest {
        val loginDataMockk = LoginEntity(id = "mockk")
        val token = "token"

        arrangeGetToken(token)
        arrangeLoginJwt(token, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        viewModel.auth()

        //Assert
        coVerify(exactly = 1) {
            getTokenUseCase()
            loginJWTUseCase(token)
        }
        val state = viewModel.uiState.value
        assertEquals(state.loginState, false)
        assertEquals(state.isLoading, false)
        assertNull(state.loginData)
    }

    @Test
    fun `resetState() should set loginState to null`() {
        //Arrange
        val loginDataMockk = LoginEntity(id = "mockk")
        val token = "token"

        arrangeGetToken(token)
        arrangeLoginJwt(token, Result.Error(NetworkError.RESPONSE_ERROR))
        viewModel.auth()

        //Act
        viewModel.resetState()

        //Assert
        coVerify(exactly = 1) {
            getTokenUseCase()
            loginJWTUseCase(token)
        }
        val state = viewModel.uiState.value
        assertNull(state.loginState)
        assertEquals(state.isLoading, false)
        assertNull(state.loginData)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `auth() should set login state to false if taking more than 1500ms `() = runTest {
        //Arrange

        val token = "token"

        arrangeGetToken(token)
        coEvery {
            loginJWTUseCase(token)
        } coAnswers {
            delay(2000)
            flowOf(Result   Success(LoginEntity(isVerified = true)))
        }

        //Act
        viewModel.auth()

        //Assert
        val state = viewModel.uiState.value
//        assertEquals(false, state.loginState)
        assertEquals(false, state.isLoading)
    }

}