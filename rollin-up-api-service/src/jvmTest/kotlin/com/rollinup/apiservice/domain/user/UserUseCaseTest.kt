package com.rollinup.apiservice.domain.user

import androidx.paging.PagingData
import com.rollinup.apiservice.data.repository.user.UserRepository
import com.rollinup.apiservice.data.source.network.model.request.user.CheckEmailOrUsernameQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.DeleteUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitVerificationOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.model.user.UserOptionEntity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@Suppress("UnusedFlow")
class UserUseCaseTest {

    @MockK
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `GetUserListUseCase should call repository`() {
        val useCase = GetUserListUseCase(repository)
        val params = GetUserQueryParams()
        val expected = flowOf(Result.Success(listOf<UserEntity>()))
        every { repository.getUserList(params) } returns expected

        val result = useCase(params)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.getUserList(params) }
    }

    @Test
    fun `GetUserPagingUseCase should call repository`() {
        val useCase = GetUserPagingUseCase(repository)
        val params = GetUserQueryParams()
        val expected = flowOf(PagingData.empty<UserEntity>())
        every { repository.getUserPaging(params) } returns expected

        val result = useCase(params)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.getUserPaging(params) }
    }

    @Test
    fun `GetUserByIdUseCase should call repository`() {
        val useCase = GetUserByIdUseCase(repository)
        val id = "u1"
        val expected = flowOf(Result.Success(mockk<UserDetailEntity>()))
        every { repository.getUserById(id) } returns expected

        val result = useCase(id)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.getUserById(id) }
    }

    @Test
    fun `RegisterUserUseCase should call repository`() {
        val useCase = RegisterUserUseCase(repository)
        val body = CreateEditUserBody(firstName = "test")
        val expected = flowOf(Result.Success(Unit))
        every { repository.registerUser(body) } returns expected

        val result = useCase(body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.registerUser(body) }
    }

    @Test
    fun `EditUserUseCase should call repository`() {
        val useCase = EditUserUseCase(repository)
        val id = "u1"
        val body = CreateEditUserBody(firstName = "edit")
        val expected = flowOf(Result.Success(Unit))
        every { repository.editUser(id, body) } returns expected

        val result = useCase(id, body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.editUser(id, body) }
    }

    @Test
    fun `CreateResetPasswordRequestUseCase should call repository`() {
        val useCase = CreateResetPasswordRequestUseCase(repository)
        val body = CreateResetPasswordRequestBody(email = "test@mail.com")
        val expected = flowOf(Result.Success("email"))
        every { repository.createResetPasswordRequest(body) } returns expected

        val result = useCase(body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.createResetPasswordRequest(body) }
    }

    @Test
    fun `SubmitResetOtpUseCase should call repository`() {
        val useCase = SubmitResetOtpUseCase(repository)
        val body = SubmitResetPasswordOTPBody(otp = "123")
        val expected = flowOf(Result.Success("token"))
        every { repository.submitResetOtp(body) } returns expected

        val result = useCase(body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.submitResetOtp(body) }
    }

    @Test
    fun `SubmitResetPasswordUseCase should call repository`() {
        val useCase = SubmitResetPasswordUseCase(repository)
        val body = SubmitResetPasswordBody(newPassword = "pass", token = "token")
        val expected = flowOf(Result.Success(Unit))
        every { repository.submitResetPassword(body) } returns expected

        val result = useCase(body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.submitResetPassword(body) }
    }

    @Test
    fun `GetUserOptionsUseCase should call repository`() {
        val useCase = GetUserOptionsUseCase(repository)
        val expected = flowOf(Result.Success(mockk<UserOptionEntity>()))
        every { repository.getOptions() } returns expected

        val result = useCase()

        assertEquals(expected, result)
        verify(exactly = 1) { repository.getOptions() }
    }

    @Test
    fun `DeleteUserUseCase should call repository`() {
        val useCase = DeleteUserUseCase(repository)
        val body = DeleteUserBody(listId = listOf("u1"))
        val expected = flowOf(Result.Success(Unit))
        every { repository.deleteUser(body) } returns expected

        val result = useCase(body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.deleteUser(body) }
    }

    @Test
    fun `CheckEmailOrUsernameUseCase should call repository`() {
        val useCase = CheckEmailOrUsernameUseCase(repository)
        val params = CheckEmailOrUsernameQueryParams()
        val expected = flowOf(Result.Success(Unit))
        every { repository.checkEmailOrUsername(params) } returns expected

        val result = useCase(params)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.checkEmailOrUsername(params) }
    }

    @Test
    fun `SubmitVerificationOtpUseCase should call repository`() {
        val useCase = SubmitVerificationOtpUseCase(repository)
        val body = SubmitVerificationOTPBody(otp = "123")
        val expected = flowOf(Result.Success("otp"))
        every { repository.submitVerificationOtp(body) } returns expected

        val result = useCase(body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.submitVerificationOtp(body) }
    }

    @Test
    fun `UpdatePasswordAndVerificationUseCase should call repository`() {
        val useCase = UpdatePasswordAndVerificationUseCase(repository)
        val body = UpdatePasswordAndVerificationBody(password = "new")
        val expected = flowOf(Result.Success(Unit))
        every { repository.updatePasswordAndVerification(body) } returns expected

        val result = useCase(body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.updatePasswordAndVerification(body) }
    }

    @Test
    fun `ResendVerificationOtpUseCase should call repository`() {
        val useCase = ResendVerificationOtpUseCase(repository)
        val expected = flowOf(Result.Success(Unit))
        every { repository.resendVerificationOtp() } returns expected

        val result = useCase()

        assertEquals(expected, result)
        verify(exactly = 1) { repository.resendVerificationOtp() }
    }
}