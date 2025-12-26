@file:Suppress("UnusedFlow")
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.viewmodel

import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordOTPBody
import com.rollinup.apiservice.domain.user.CreateResetPasswordRequestUseCase
import com.rollinup.apiservice.domain.user.SubmitResetOtpUseCase
import com.rollinup.apiservice.domain.user.SubmitResetPasswordUseCase
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.screen.auth.model.resetpassword.ResetPasswordCallback
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ResetPasswordViewModelTest {
    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var cb: ResetPasswordCallback

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @MockK
    private val createResetPasswordRequestUseCase: CreateResetPasswordRequestUseCase = mockk()

    @MockK
    private val submitResetOtpUseCase: SubmitResetOtpUseCase = mockk()

    @MockK
    private val submitResetPasswordUseCase: SubmitResetPasswordUseCase = mockk()

    private fun arrangeCreateRequestUseCase(
        body: CreateResetPasswordRequestBody,
        result: Result<String, NetworkError>,
    ) {
        coEvery {
            createResetPasswordRequestUseCase(body)
        } returns flowOf(result)
    }


    private fun arrangeSubmitOtpUseCase(
        body: SubmitResetPasswordOTPBody,
        result: Result<String, NetworkError>,
    ) {
        coEvery {
            submitResetOtpUseCase(
                body = body
            )
        } returns flowOf(result)
    }

    fun arrangeResetPasswordUseCase(
        body: SubmitResetPasswordBody,
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            submitResetPasswordUseCase(body)
        } returns flowOf(result)
    }


    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewModel = ResetPasswordViewModel(
            createResetPasswordRequestUseCase = createResetPasswordRequestUseCase,
            submitResetOtpUseCase = submitResetOtpUseCase,
            submitResetPasswordUseCase = submitResetPasswordUseCase
        )
        cb = viewModel.getCallback()
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `submitEmail() should return Result Success`() = runTest {
        //Arrange
        val email = "email"
        val expectedResult = "actualEmail"
        val body = CreateResetPasswordRequestBody(email = email)

        arrangeCreateRequestUseCase(body, Result.Success(expectedResult))

        //Act
        cb.onSubmitEmail(email)

        //Assert
        coVerify(exactly = 1) {
            createResetPasswordRequestUseCase(body)
        }

        val state = viewModel.uiState.value
        assertEquals(state.submitEmailState, true)
        assertEquals(state.actualEmail, expectedResult)
        assertEquals(state.isLoadingOverlay, false)
    }

    @Test
    fun `submitEmail() should return Result Error`() = runTest {
        //Arrange
        val email = "email"
        val body = CreateResetPasswordRequestBody(email = email)

        arrangeCreateRequestUseCase(body, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        cb.onSubmitEmail(email)

        //Assert
        coVerify(exactly = 1) {
            createResetPasswordRequestUseCase(body)
        }

        val state = viewModel.uiState.value
        assertEquals(state.submitEmailState, false)
        assertEquals(state.isLoadingOverlay, false)
    }

    @Test
    fun `submitOtp() should return Result Success`() = runTest {
        //Arrange
        val otp = "12123"
        val body = SubmitResetPasswordOTPBody(email = "", otp)
        val expectedResult = "token"

        arrangeSubmitOtpUseCase(body, Result.Success(expectedResult))

        //Act
        cb.onSubmitOtp(otp)

        //Assert
        coVerify(exactly = 1) {
            submitResetOtpUseCase(body)
        }

        val state = viewModel.uiState.value
        assertEquals(state.submitOtpState, true)
        assertEquals(state.isLoadingOverlay, false)
        assertEquals(state.resetToken, expectedResult)
    }

    @Test
    fun `submitOtp() should return Result Error`() = runTest {
        //Arrange
        val otp = "12123"
        val body = SubmitResetPasswordOTPBody(email = "", otp)

        arrangeSubmitOtpUseCase(body, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        cb.onSubmitOtp(otp)

        //Assert
        coVerify(exactly = 1) {
            submitResetOtpUseCase(body)
        }

        val state = viewModel.uiState.value
        assertEquals(state.submitOtpState, false)
        assertEquals(state.isLoadingOverlay, false)
    }

    @Test
    fun `submitNewPassword() should return Result Success`() = runTest {
        //Arrange
        val password = "password"
        val body = SubmitResetPasswordBody(token = "", newPassword = password)

        arrangeResetPasswordUseCase(body, Result.Success(Unit))

        //Act
        cb.onSubmitNewPassword(password)

        //Assert
        coVerify(exactly = 1) {
            submitResetPasswordUseCase(body)
        }

        val state = viewModel.uiState.value
        assertEquals(state.submitPasswordState, true)
        assertEquals(state.isLoadingOverlay, false)
    }

    @Test
    fun `submitNewPassword() should return Result Error`() = runTest {
        //Arrange
        val password = "password"
        val body = SubmitResetPasswordBody(token = "", newPassword = password)

        arrangeResetPasswordUseCase(body, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        cb.onSubmitNewPassword(password)

        //Assert
        coVerify(exactly = 1) {
            submitResetPasswordUseCase(body)
        }

        val state = viewModel.uiState.value
        assertEquals(state.submitPasswordState, false)
        assertEquals(state.isLoadingOverlay, false)
    }

    @Test
    fun `resetMessageState() should reset message state to null`() = runTest {
        val password = "password"
        val body = SubmitResetPasswordBody(token = "", newPassword = password)

        arrangeResetPasswordUseCase(body, Result.Error(NetworkError.RESPONSE_ERROR))

        cb.onSubmitNewPassword(password)
        //Act
        cb.onResetMessageState()

        //Assert
        val state = viewModel.uiState.value
        assertNull(state.submitPasswordState)
        assertNull(state.submitOtpState)
        assertNull(state.submitEmailState)
    }

    @Test
    fun `updateStep() should update current step to given value`() {
        //Arrange
        val expected = 2

        //Act
        cb.onUpdateStep(2)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expected, state.step)
    }

}