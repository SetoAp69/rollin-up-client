package com.rollinup.apiservice.data.repository.user

import com.rollinup.apiservice.data.mapper.UserMapper
import com.rollinup.apiservice.data.source.network.apiservice.UserApiService
import com.rollinup.apiservice.data.source.network.model.request.user.CheckEmailOrUsernameQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.DeleteUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitVerificationOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserListResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserOptionsResponse
import com.rollinup.apiservice.data.source.network.model.response.user.ResetPasswordRequestResponse
import com.rollinup.apiservice.data.source.network.model.response.user.SubmitResetOtpResponse
import com.rollinup.apiservice.data.source.network.model.response.user.ValidateVerificationOtpResponse
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.model.user.UserOptionEntity
import com.rollinup.apiservice.utils.Utils
import com.rollinup.common.model.OptionData
import io.ktor.http.HttpStatusCode
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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

class UserRepositoryTest {

    private lateinit var repository: UserRepository

    @MockK
    private val dataSource: UserApiService = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ioDispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    // Real mapper to test transformation logic
    private val mapper = UserMapper()

    @Before
    fun init() {
        MockKAnnotations.init(this)

        // Mock Utils to handle static methods and error handling
        mockkObject(Utils)

        repository = UserRepositoryImpl(
            userApiService = dataSource,
            mapper = mapper,
            ioDispatcher = ioDispatcher
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    //region GetUserList
    @Test
    fun `getUserList should return Result Success Flow with mapped data`() = runTest {
        // Arrange
        val queryParams = GetUserQueryParams()
        val mockResponse = GetUserListResponse(
            status = 200, message = "OK",
            data = GetUserListResponse.Data(
                record = 1, page = 1,
                data = listOf(
                    GetUserListResponse.Data.UserData(
                        id = "u1",
                        firstName = "John",
                        userName = "johndoe",
                        email = "john@mail.com",
                        role = "student",
                        studentId = "123",
                        classX = "10A"
                    )
                )
            )
        )
        val expectedEntity = listOf(
            UserEntity(
                id = "u1", fullName = "John ", userName = "johndoe", email = "john@mail.com",
                role = "student", studentId = "123", classX = "10A"
            )
        )

        coEvery { dataSource.getUsersList(queryParams) } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        // Act
        val result = repository.getUserList(queryParams).first()

        // Assert
        coVerify { dataSource.getUsersList(queryParams) }
        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `getUserList should return Result Error Flow`() = runTest {
        // Arrange
        val queryParams = GetUserQueryParams()
        coEvery { dataSource.getUsersList(queryParams) } returns ApiResponse.Error(Exception("Network"))

        // Act
        val result = repository.getUserList(queryParams).first()

        // Assert
        assertTrue(result is Result.Error)
    }

    @Test
    fun `getUserList should return Result Error Flow when exception thrown`() = runTest {
        // Arrange
        val queryParams = GetUserQueryParams()
        coEvery { dataSource.getUsersList(queryParams) } throws RuntimeException("Error")

        // Act
        val result = repository.getUserList(queryParams).first()

        // Assert
        assertTrue(result is Result.Error)
    }
    //endregion

    //region GetUserById
    @Test
    fun `getUserById should return Result Success Flow`() = runTest {
        // Arrange
        val id = "u1"
        val mockResponse = GetUserByIdResponse(
            status = 200, message = "OK",
            data = GetUserByIdResponse.Data(
                id = "u1", firstName = "John", username = "johndoe", email = "john@mail.com",
                role = GetUserByIdResponse.Data.Role("123"), studentId = "123",
            )
        )
        val expectedEntity = UserDetailEntity(
            id = "u1",
            firstName = "John",
            userName = "johndoe",
            email = "john@mail.com",
            fullName = "John ",
            role = UserDetailEntity.Data(id = "123"),
            studentId = "123",
        )

        coEvery { dataSource.getUserById(id) } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        // Act
        val result = repository.getUserById(id).first()

        // Assert
        coVerify { dataSource.getUserById(id) }
        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `getUserById should return Result Error Flow`() = runTest {
        val id = "u1"
        coEvery { dataSource.getUserById(id) } returns ApiResponse.Error(Exception("Not Found"))
        val result = repository.getUserById(id).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `getUserById should return Result Error Flow on exception`() = runTest {
        val id = "u1"
        coEvery { dataSource.getUserById(id) } coAnswers { throw RuntimeException("Error") }
        val result = repository.getUserById(id).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region RegisterUser
    @Test
    fun `registerUser should return Result Success Unit`() = runTest {
        // Arrange
        val body = CreateEditUserBody(
            firstName = "New",
            email = "new@mail.com",
            role = "student",
        )
        coEvery { dataSource.registerUser(body) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.Created
        )

        // Act
        val result = repository.registerUser(body).first()

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `registerUser should return Result Error Flow`() = runTest {
        val body = CreateEditUserBody(firstName = "New", email = "new@mail.com", role = "student")
        coEvery { dataSource.registerUser(body) } returns ApiResponse.Error(Exception("Exists"))
        val result = repository.registerUser(body).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `registerUser should return Result Error Flow on exception`() = runTest {
        val body = CreateEditUserBody(firstName = "New", email = "new@mail.com", role = "student")
        coEvery { dataSource.registerUser(body) } coAnswers { throw RuntimeException("Error") }
        val result = repository.registerUser(body).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region EditUser
    @Test
    fun `editUser should return Result Success Unit`() = runTest {
        val id = "u1"
        val body = CreateEditUserBody(firstName = "Updated")
        coEvery { dataSource.editUser(id, body) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.Accepted
        )

        val result = repository.editUser(id, body).first()

        assertTrue(result is Result.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `editUser should return Result Error Flow`() = runTest {
        val id = "u1"
        val body = CreateEditUserBody(firstName = "Updated")
        coEvery { dataSource.editUser(id, body) } returns ApiResponse.Error(Exception("Failed"))
        val result = repository.editUser(id, body).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `editUser should return Result Error Flow on exception`() = runTest {
        val id = "u1"
        val body = CreateEditUserBody(firstName = "Updated")
        coEvery { dataSource.editUser(id, body) } throws RuntimeException("Error")
        val result = repository.editUser(id, body).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region CreateResetPasswordRequest
    @Test
    fun `createResetPasswordRequest should return Success with email`() = runTest {
        val body = CreateResetPasswordRequestBody(email = "test@mail.com")
        val mockResponse = ResetPasswordRequestResponse(
            status = 200, message = "OK",
            data = ResetPasswordRequestResponse.Data(email = "test@mail.com")
        )
        coEvery { dataSource.createResetPasswordRequest(body) } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        val result = repository.createResetPasswordRequest(body).first()

        assertTrue(result is Result.Success)
        assertEquals("test@mail.com", result.data)
    }

    @Test
    fun `createResetPasswordRequest should return Error`() = runTest {
        val body = CreateResetPasswordRequestBody(email = "test@mail.com")
        coEvery { dataSource.createResetPasswordRequest(body) } returns ApiResponse.Error(
            Exception(
                "Fail"
            )
        )
        val result = repository.createResetPasswordRequest(body).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `createResetPasswordRequest should return Error on exception`() = runTest {
        val body = CreateResetPasswordRequestBody(email = "test@mail.com")
        coEvery { dataSource.createResetPasswordRequest(body) } coAnswers { throw RuntimeException("Error") }
        val result = repository.createResetPasswordRequest(body).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region SubmitResetOtp
    @Test
    fun `submitResetOtp should return Success with reset token`() = runTest {
        val body = SubmitResetPasswordOTPBody(email = "e", otp = "123")
        val mockResponse = SubmitResetOtpResponse(
            status = 200, message = "OK",
            data = SubmitResetOtpResponse.Data(resetToken = "token_xyz")
        )
        coEvery { dataSource.submitResetOtp(body) } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        val result = repository.submitResetOtp(body).first()

        assertTrue(result is Result.Success)
        assertEquals("token_xyz", result.data)
    }

    @Test
    fun `submitResetOtp should return Error`() = runTest {
        val body = SubmitResetPasswordOTPBody(email = "e", otp = "123")
        coEvery { dataSource.submitResetOtp(body) } returns ApiResponse.Error(Exception("Invalid OTP"))
        val result = repository.submitResetOtp(body).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `submitResetOtp should return Error on exception`() = runTest {
        val body = SubmitResetPasswordOTPBody(email = "e", otp = "123")
        coEvery { dataSource.submitResetOtp(body) } coAnswers { throw RuntimeException("Error") }
        val result = repository.submitResetOtp(body).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region SubmitResetPassword
    @Test
    fun `submitResetPassword should return Success Unit`() = runTest {
        val body = SubmitResetPasswordBody(token = "t", newPassword = "p")
        coEvery { dataSource.submitResetPassword(body) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.OK
        )
        val result = repository.submitResetPassword(body).first()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `submitResetPassword should return Error`() = runTest {
        val body = SubmitResetPasswordBody(token = "t", newPassword = "p")
        coEvery { dataSource.submitResetPassword(body) } returns ApiResponse.Error(Exception("Fail"))
        val result = repository.submitResetPassword(body).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `submitResetPassword should return Error on exception`() = runTest {
        val body = SubmitResetPasswordBody(token = "t", newPassword = "p")
        coEvery { dataSource.submitResetPassword(body) } coAnswers { throw RuntimeException("Error") }
        val result = repository.submitResetPassword(body).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region GetOptions
    @Test
    fun `getOptions should return Success with mapped options`() = runTest {
        val mockResponse = GetUserOptionsResponse(
            status = 200, message = "OK",
            data = GetUserOptionsResponse.Data(
                rolesIdOptions = listOf(GetUserOptionsResponse.Data.Data("student", "s")),
                classIdOptions = listOf(GetUserOptionsResponse.Data.Data("A1", "a1"))
            )
        )
        val expectedEntity = UserOptionEntity(
            roleIdOptions = listOf(OptionData("student", "s")),
            classIdOptions = listOf(OptionData("A1", "a1"))
        )

        coEvery { dataSource.getUserOptions() } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        val result = repository.getOptions().first()

        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `getOptions should return Error`() = runTest {
        coEvery { dataSource.getUserOptions() } returns ApiResponse.Error(Exception("Fail"))
        val result = repository.getOptions().first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `getOptions should return Error on exception`() = runTest {
        coEvery { dataSource.getUserOptions() } coAnswers { throw RuntimeException("Error") }
        val result = repository.getOptions().first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region DeleteUser
    @Test
    fun `deleteUser should return Success Unit`() = runTest {
        val body = DeleteUserBody(listId = listOf("id"))
        coEvery { dataSource.deleteUser(body) } returns ApiResponse.Success(Unit, HttpStatusCode.OK)
        val result = repository.deleteUser(body).first()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `deleteUser should return Error`() = runTest {
        val body = DeleteUserBody(listOf("id"))
        coEvery { dataSource.deleteUser(body) } returns ApiResponse.Error(Exception("Fail"))
        val result = repository.deleteUser(body).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `deleteUser should return Error on exception`() = runTest {
        val body = DeleteUserBody(listId = listOf("u1"))
        coEvery { dataSource.deleteUser(body) } coAnswers { throw RuntimeException("Error") }
        val result = repository.deleteUser(body).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region CheckEmailOrUsername
    @Test
    fun `checkEmailOrUsername should return Success Unit`() = runTest {
        val query = CheckEmailOrUsernameQueryParams(email = "e")
        coEvery { dataSource.checkEmailOrUsername(query.toQueryMap()) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.OK
        )
        val result = repository.checkEmailOrUsername(query).first()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `checkEmailOrUsername should return Error`() = runTest {
        val query = CheckEmailOrUsernameQueryParams(email = "e")
        coEvery { dataSource.checkEmailOrUsername(query.toQueryMap()) } returns ApiResponse.Error(
            Exception("Taken")
        )
        val result = repository.checkEmailOrUsername(query).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `checkEmailOrUsername should return Error on exception`() = runTest {
        val query = CheckEmailOrUsernameQueryParams(email = "e")
        coEvery { dataSource.checkEmailOrUsername(query.toQueryMap()) } coAnswers {
            throw RuntimeException(
                "Error"
            )
        }
        val result = repository.checkEmailOrUsername(query).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region SubmitVerificationOtp
    @Test
    fun `submitVerificationOtp should return Success with otp string`() = runTest {
        val body = SubmitVerificationOTPBody(otp = "123")
        val mockResponse = ValidateVerificationOtpResponse(
            status = 200, message = "OK",
            data = ValidateVerificationOtpResponse.Data(otp = "valid_otp")
        )
        coEvery { dataSource.submitVerificationOtp(body) } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        val result = repository.submitVerificationOtp(body).first()

        assertTrue(result is Result.Success)
        assertEquals("valid_otp", result.data)
    }

    @Test
    fun `submitVerificationOtp should return Error`() = runTest {
        val body = SubmitVerificationOTPBody(otp = "123")
        coEvery { dataSource.submitVerificationOtp(body) } returns ApiResponse.Error(Exception("Invalid"))
        val result = repository.submitVerificationOtp(body).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `submitVerificationOtp should return Error on exception`() = runTest {
        val body = SubmitVerificationOTPBody(otp = "123")
        coEvery { dataSource.submitVerificationOtp(body) } throws RuntimeException("Error")
        val result = repository.submitVerificationOtp(body).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region UpdatePasswordAndVerification
    @Test
    fun `updatePasswordAndVerification should return Success Unit`() = runTest {
        val body = UpdatePasswordAndVerificationBody(password = "p", token = "o")
        coEvery { dataSource.updatePasswordAndVerification(body) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.OK
        )
        val result = repository.updatePasswordAndVerification(body).first()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `updatePasswordAndVerification should return Error`() = runTest {
        val body = UpdatePasswordAndVerificationBody(password = "p", token = "o")
        coEvery { dataSource.updatePasswordAndVerification(body) } returns ApiResponse.Error(
            Exception("Fail")
        )
        val result = repository.updatePasswordAndVerification(body).first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `updatePasswordAndVerification should return Error on exception`() = runTest {
        val body = UpdatePasswordAndVerificationBody(password = "p", token = "o")
        coEvery { dataSource.updatePasswordAndVerification(body) } coAnswers {
            throw RuntimeException(
                "Error"
            )
        }
        val result = repository.updatePasswordAndVerification(body).first()
        assertTrue(result is Result.Error)
    }
    //endregion

    //region ResendVerificationOtp
    @Test
    fun `resendVerificationOtp should return Success Unit`() = runTest {
        coEvery { dataSource.resetVerificationOtp() } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.OK
        )
        val result = repository.resendVerificationOtp().first()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `resendVerificationOtp should return Error`() = runTest {
        coEvery { dataSource.resetVerificationOtp() } returns ApiResponse.Error(Exception("Fail"))
        val result = repository.resendVerificationOtp().first()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `resendVerificationOtp should return Error on exception`() = runTest {
        coEvery { dataSource.resetVerificationOtp() } coAnswers { throw RuntimeException("Error") }
        val result = repository.resendVerificationOtp().first()
        assertTrue(result is Result.Error)
    }
    //endregion
}