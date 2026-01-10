@file:Suppress("UnusedFlow")
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.viewmodel

import com.rollinup.apiservice.data.source.network.model.request.user.SubmitVerificationOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.domain.user.ResendVerificationOtpUseCase
import com.rollinup.apiservice.domain.user.SubmitVerificationOtpUseCase
import com.rollinup.apiservice.domain.user.UpdatePasswordAndVerificationUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordCallback
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordFormData
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate.VerifyAccountUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UpdatePasswordViewModelTest {

    private lateinit var viewModel: VerifyAccountViewModel
    private lateinit var cb: UpdatePasswordCallback

    @MockK
    private val updatePasswordAndVerificationUseCase: UpdatePasswordAndVerificationUseCase = mockk()

    @MockK
    private val submitVerificationUseCase: SubmitVerificationOtpUseCase = mockk()

    @MockK
    private val resendVerificationUseCase: ResendVerificationOtpUseCase = mockk()

    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewModel = VerifyAccountViewModel(
            updatePasswordAndVerificationUseCase = updatePasswordAndVerificationUseCase,
            submitVerificationOtpUseCase = submitVerificationUseCase,
            resendVerificationOtpUseCase = resendVerificationUseCase
        )
        cb = viewModel.getCallback()
    }

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @After
    fun teardown() {
        unmockkAll()
    }

    private fun arrangeUpdatePasswordAndVerificationUseCase(
        body: UpdatePasswordAndVerificationBody,
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            updatePasswordAndVerificationUseCase(body)
        } returns flowOf(result)
    }

    private fun arrangeSubmitVerificationUseCase(
        body: SubmitVerificationOTPBody,
        result: Result<String, NetworkError>,
    ) {
        coEvery {
            submitVerificationUseCase(body)
        } returns flowOf(result)
    }

    private fun arrangeResendVerificationUseCase(
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            resendVerificationUseCase()
        } returns flowOf(result)
    }

    @Test
    fun `init() should set user data and start timer`() {
        //Arrange
        val expectedUser = LoginEntity(id = "MyUser")
        //Act
        viewModel.init(expectedUser)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expectedUser, state.user)
        assertTrue(state.startTimer)
    }

    @Test
    fun `reset()_should reset ui state with default value`() {
        //Arrange
        val user = LoginEntity(id = "myUser")
        val expected = VerifyAccountUiState()

        viewModel.init(user)

        //Act
        viewModel.reset()

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expected, state)
    }

    @Test
    fun `resetForm() should reset formdata to default value`() {
        //Arrange
        val expectedOtp = ""
        val expectedError = null

        cb.onUpdateOtp("123")

        //Act
        cb.onResetOtp()

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expectedOtp, state.otp)
        assertEquals(expectedError, state.otpError)
    }

    @Test
    fun `updateFormData() should update form data with given value`() {
        //Arrange
        val expected = UpdatePasswordFormData(
            userId = "user",
            passwordOne = "pass1",
            passwordTwo = "pass2",
            deviceId = "device",
            passwordOneError = "sdasd",
            passwordTwoError = "asdsad"
        )

        //Act
        cb.onUpdateFormData(expected)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expected, state.formData)
    }

    @Test
    fun `resendOtp() should return Result Success`() = runTest {
        //Arrange
        arrangeResendVerificationUseCase(Result.Success(Unit))
        //Act
        cb.onResendOtp()

        //Assert
        coVerify(exactly = 1) {
            resendVerificationUseCase()
        }
        val state = viewModel.uiState.value
        assertFalse(state.isLoadingOverlay)
        assertTrue(state.requestOtpState == true)
    }

    @Test
    fun `resendOtp() should return Result Error`() = runTest {
        //Arrange
        arrangeResendVerificationUseCase(Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        cb.onResendOtp()

        //Assert
        coVerify(exactly = 1) {
            resendVerificationUseCase()
        }
        val state = viewModel.uiState.value
        assertFalse(state.isLoadingOverlay)
        assertTrue(state.requestOtpState == false)
    }

    @Test
    fun `submitOtp() should do nothing when otp is empty `() {
        //Arrange
        val otp = ""
        val expected = "OTP cannot be empty"

        //Act
        cb.onSubmitOtp(otp)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expected, state.otpError)
    }

    @Test
    fun `submitOtp() should do nothing when otp is less than 5`() {
        //Arrange
        val otp = "123"
        val expected = "You need to fills all the digits"

        //Act
        cb.onSubmitOtp(otp)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expected, state.otpError)
    }

    @Test
    fun `submitOtp should return Result Success`() = runTest {
        //Arrange
        val otp = "12345"
        val body = SubmitVerificationOTPBody(otp)
        arrangeSubmitVerificationUseCase(body, Result.Success("token"))

        //Act
        cb.onSubmitOtp(otp)

        //Act
        coVerify(exactly = 1) {
            submitVerificationUseCase(body)
        }

        val state = viewModel.uiState.value
        assertTrue(state.submitOtpState == true)
        assertEquals("token", state.token)
        assertFalse(state.isLoadingOverlay)
    }

    @Test
    fun `submitOtp should return Result Error`() = runTest {
        //Arrange
        val otp = "12345"
        val body = SubmitVerificationOTPBody(otp)
        arrangeSubmitVerificationUseCase(body, Result.Error(NetworkError.RESPONSE_ERROR))


        //TODO: RECHECK THE RUN TEST IF IT'S ACTUALLY WOWKING553
        //Act
        cb.onSubmitOtp(otp)

        //Act
        coVerify(exactly = 1) {
            submitVerificationUseCase(body)
        }

        val state = viewModel.uiState.value
        assertTrue(state.submitOtpState == false)
        assertEquals("", state.token)
        assertFalse(state.isLoadingOverlay)
    }

    @Test
    fun `submitUpdatePassword() should return Result Success`() = runTest {
        //Arrange
        val formData = UpdatePasswordFormData(
            passwordOne = "password",
            passwordTwo = "password",
        )

        val body = UpdatePasswordAndVerificationBody(
            password = "password"
        )

        arrangeUpdatePasswordAndVerificationUseCase(
            body = body,
            result = Result.Success(Unit)
        )

        //Act
        cb.onSubmit(formData)

        //Arrange
        coVerify(exactly = 1) {
            updatePasswordAndVerificationUseCase(body)
        }

        val state = viewModel.uiState.value
        assertEquals(true, state.updatePasswordState)
        assertFalse(state.isLoadingOverlay)
    }

    @Test
    fun `submitUpdatePassword() should return Result Error`() = runTest {
        //Arrange
        val formData = UpdatePasswordFormData(
            passwordOne = "password",
            passwordTwo = "password",
        )

        val body = UpdatePasswordAndVerificationBody(
            password = "password"
        )

        arrangeUpdatePasswordAndVerificationUseCase(
            body = body,
            result = Result.Error(NetworkError.RESPONSE_ERROR)
        )

        //Act
        cb.onSubmit(formData)
        advanceUntilIdle()

        //Arrange
        coVerify(exactly = 1) {
            updatePasswordAndVerificationUseCase(body)
        }

        val state = viewModel.uiState.value
        assertEquals(false, state.updatePasswordState)
        assertFalse(state.isLoadingOverlay)
    }

//    @Test
//    fun `resetMessageState() should reset message states to null`() {
//        //Arrange
//        val formData = UpdatePasswordFormData(
//            passwordOne = "password",
//            passwordTwo = "password",
//        )
//
//        val body = UpdatePasswordAndVerificationBody(
//            password = "password"
//        )
//
//        arrangeUpdatePasswordAndVerificationUseCase(
//            body = body,
//            result = Result.Error(NetworkError.RESPONSE_ERROR)
//        )
//        cb.onSubmit(formData)
//
//        //Act
//        cb.onResetMessageState()
//
//        //Assert
//        val state = viewModel.uiState.value
//        assertNull(state.updatePasswordState)
//    }
}