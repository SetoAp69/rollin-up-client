@file:Suppress("UnusedFlow")
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.viewmodel

import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.domain.auth.ClearClientTokenUseCase
import com.rollinup.apiservice.domain.auth.LoginUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.screen.auth.model.login.LoginCallback
import com.rollinup.rollinup.screen.auth.model.login.LoginFormData
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.uistate.LoginUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginViewModelTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()
    @MockK
    private val loginUseCase: LoginUseCase = mockk()

    @MockK
    private val logoutUseCase: ClearClientTokenUseCase = mockk()

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var callback: LoginCallback

    fun arrangeLoginUseCase(
        body: LoginBody,
        result: Result<LoginEntity, NetworkError>,
    ) {
        coEvery {
            loginUseCase(body)
        } returns flowOf(result)
    }

    fun arrangeLogoutUseCase() {
        coJustRun {
            logoutUseCase()
        }
    }

    @Before
    fun init() {
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            logoutUseCase = logoutUseCase
        )
        callback = loginViewModel.getCallback()
    }

    @Test
    fun `reset() should reset current ui state to default value`() {
        ///Arrange
        val expected = LoginUiState()
        callback.onUpdateForm(
            LoginFormData("email")
        )

        //Act
        loginViewModel.reset()

        //Assert
        val uiSate = loginViewModel.uiState.value
        assertEquals(expected, uiSate)
    }

    @Test
    fun `updateForm() should update login form with given value`() {
        //Arrange
        val expected = LoginFormData(
            email = "my email",
        )

        //Act
        callback.onUpdateForm(expected)

        //Assert
        val state = loginViewModel.uiState.value
        assertEquals(expected, state.loginFormData)
    }

    @Test
    fun `login() should return Result Success`() = runTest {
        //Arrange
        val formData = LoginFormData(
            email = "Email",
            password = "Password"
        )

        val body = LoginBody(
            email = "Email",
            password = "Password"
        )

        val expectedResult = LoginEntity(
            id = "Success"
        )
        arrangeLoginUseCase(
            body = body,
            result = Result.Success(expectedResult)
        )
        arrangeLogoutUseCase()

        //Act
        callback.onLogin(formData)

        //Assert
        coVerify(exactly = 1) {
            logoutUseCase()
        }
        coVerify(exactly = 1) {
            loginUseCase(body)
        }

        val state = loginViewModel.uiState.value
        assertTrue(state.loginState == true)
        assertFalse(state.isLoadingOverlay)
        assertEquals(expectedResult, state.loginData)
    }

    @Test
    fun `login() should do nothing when form data is invalid`() {
        //Arrange
        val formData = LoginFormData()

        //Act
        callback.onLogin(formData)

        //Assert
        coVerify(exactly = 0) {
            loginUseCase(LoginBody())
        }
    }

    @Test
    fun `login() should return error result`() = runTest {
        //Arrange
        val formData = LoginFormData(
            email = "Email",
            password = "Password"
        )

        val body = LoginBody(
            email = "Email",
            password = "Password"
        )

        arrangeLoginUseCase(
            body = body,
            result = Result.Error(NetworkError.RESPONSE_ERROR)
        )

        //Act
        callback.onLogin(formData)

        //Assert
        coVerify(exactly = 1) {
            loginUseCase(body)
        }

        val state = loginViewModel.uiState.value
        assertTrue(state.loginState == false)
        assertFalse(state.isLoadingOverlay)
        assertNull( state.loginData)
    }
}