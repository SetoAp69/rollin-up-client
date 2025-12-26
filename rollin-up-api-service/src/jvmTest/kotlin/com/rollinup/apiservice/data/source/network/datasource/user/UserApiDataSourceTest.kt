package com.rollinup.apiservice.data.source.network.datasource.user

import com.rollinup.apiservice.CoroutineTestRule
import com.rollinup.apiservice.HttpResponseData
import com.rollinup.apiservice.data.source.network.apiservice.UserApiService
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
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class UserApiDataSourceTest {

    @get:Rule
    private val coroutineRule = CoroutineTestRule()

    private lateinit var dataSource: UserApiService
    private lateinit var httpClient: HttpClient

    private var baseUrl = MockUrl.BASE
    private var method = HttpMethod.Get
    private var response = HttpResponseData()

    private fun HttpRequestData.verifyUrl(method: HttpMethod, path: String): Boolean {
        return url.encodedPath == path && this.method == method
    }

    @Before
    fun init() {
        httpClient = HttpClient(
            engine = MockEngine.create {
                dispatcher = UnconfinedTestDispatcher()
                addHandler { req ->
                    when {
                        req.verifyUrl(method, baseUrl) -> {
                            respond(
                                content = response.content,
                                status = response.status,
                                headers = headers {
                                    set(
                                        name = "Content-Type",
                                        value = response.sContentType
                                    )
                                }
                            )
                        }

                        else -> {
                            respond(content = "Not found", status = HttpStatusCode.NotFound)
                        }
                    }
                }
            }
        ) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        encodeDefaults = true
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    }
                )
            }
            expectSuccess = true
        }
        dataSource = UserApiDataSource(
            httpClient = httpClient
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getUsersList() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = GetUserListResponse(
            status = 200,
            message = "Success",
            data = GetUserListResponse.Data(
                page = 1,
                record = 10,
                data = listOf(
                    GetUserListResponse.Data.UserData(
                        id = "user1",
                        userName = "jdoe",
                        firstName = "John",
                        lastName = "Doe",
                        email = "john@example.com",
                        role = "student",
                        gender = "M"
                    )
                )
            )
        )
        response = HttpResponseData(
            content = Json.encodeToString(GetUserListResponse.serializer(), expected),
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BASE

        //Act
        val result = dataSource.getUsersList(GetUserQueryParams())
        advanceUntilIdle()

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getUsersList() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = "Bad Request",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BASE

        //Act
        val result = dataSource.getUsersList(GetUserQueryParams())

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `getUserById() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = GetUserByIdResponse(
            status = 200,
            message = "Success",
            data = GetUserByIdResponse.Data(
                id = "user1",
                username = "jdoe",
                firstName = "John",
                lastName = "Doe",
                email = "john@example.com",
                role = GetUserByIdResponse.Data.Role(id = "1", name = "Student", key = 1),
                gender = "M",
                phoneNumber = "123456789",
                birthday = "2000-01-01",
                address = "123 Street",
                classX = GetUserByIdResponse.Data.Class(id = "c1", name = "X A", key = 1),
                studentId = "STU001"
            )
        )
        response = HttpResponseData(
            content = Json.encodeToString(GetUserByIdResponse.serializer(), expected),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BY_ID

        //Act
        val result = dataSource.getUserById("user1")

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getUserById() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = "Not Found",
            status = HttpStatusCode.NotFound
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BY_ID

        //Act
        val result = dataSource.getUserById("user1")

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `registerUser() should return ApiResponse Success`() = runTest {
        //Arrange
        val body = CreateEditUserBody()
        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.Created
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.BASE

        //Act
        val result = dataSource.registerUser(body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `registerUser() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = CreateEditUserBody()
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.BASE

        //Act
        val result = dataSource.registerUser(body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `editUser() should return ApiResponse Success`() = runTest {
        //Arrange
        val body = CreateEditUserBody()
        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Patch
        baseUrl = MockUrl.BY_ID

        //Act
        val result = dataSource.editUser("user1", body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `editUser() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = CreateEditUserBody()
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Patch
        baseUrl = MockUrl.BY_ID

        //Act
        val result = dataSource.editUser("user1", body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `createResetPasswordRequest() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = ResetPasswordRequestResponse(
            status = 200,
            message = "Success",
            data = ResetPasswordRequestResponse.Data(email = "token123")
        )
        val body = CreateResetPasswordRequestBody()
        response = HttpResponseData(
            content = Json.encodeToString(ResetPasswordRequestResponse.serializer(), expected),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.RESET_REQ

        //Act
        val result = dataSource.createResetPasswordRequest(body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `createResetPasswordRequest() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = CreateResetPasswordRequestBody()
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.RESET_REQ

        //Act
        val result = dataSource.createResetPasswordRequest(body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `submitResetOtp() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = SubmitResetOtpResponse(
            status = 200,
            message = "Success",
            data = SubmitResetOtpResponse.Data(resetToken = "123")
        )
        val body = SubmitResetPasswordOTPBody()
        response = HttpResponseData(
            content = Json.encodeToString(SubmitResetOtpResponse.serializer(), expected),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.RESET_VALIDATE

        //Act
        val result = dataSource.submitResetOtp(body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `submitResetOtp() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = SubmitResetPasswordOTPBody()
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.RESET_VALIDATE

        //Act
        val result = dataSource.submitResetOtp(body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `submitResetPassword() should return ApiResponse Success`() = runTest {
        //Arrange
        val body = SubmitResetPasswordBody()
        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Put
        baseUrl = MockUrl.RESET_PASSWORD

        //Act
        val result = dataSource.submitResetPassword(body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `submitResetPassword() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = SubmitResetPasswordBody()
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Put
        baseUrl = MockUrl.RESET_PASSWORD

        //Act
        val result = dataSource.submitResetPassword(body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `getUserOptions() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = GetUserOptionsResponse(
            status = 200,
            message = "Success",
            data = GetUserOptionsResponse.Data(
                rolesOptions = listOf(),
                classOptions = listOf(),
                rolesIdOptions = listOf(),
                classIdOptions = listOf()
            )
        )
        response = HttpResponseData(
            content = Json.encodeToString(GetUserOptionsResponse.serializer(), expected),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.OPTIONS

        //Act
        val result = dataSource.getUserOptions()

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getUserOptions() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.OPTIONS

        //Act
        val result = dataSource.getUserOptions()

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `deleteUser() should return ApiResponse Success`() = runTest {
        //Arrange
        val body = DeleteUserBody()
        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Delete
        baseUrl = MockUrl.BASE

        //Act
        val result = dataSource.deleteUser(body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `deleteUser() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = DeleteUserBody()
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Delete
        baseUrl = MockUrl.BASE

        //Act
        val result = dataSource.deleteUser(body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `checkEmailOrUsername() should return ApiResponse Success`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.CHECK_EMAIL

        //Act
        val result = dataSource.checkEmailOrUsername(mapOf("email" to "test"))

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `checkEmailOrUsername() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.CHECK_EMAIL

        //Act
        val result = dataSource.checkEmailOrUsername(emptyMap())

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `submitVerificationOtp() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = ValidateVerificationOtpResponse(
            status = 200,
            message = "Success",
            data = ValidateVerificationOtpResponse.Data(otp = "otp")
        )
        val body = SubmitVerificationOTPBody()
        response = HttpResponseData(
            content = Json.encodeToString(ValidateVerificationOtpResponse.serializer(), expected),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.VERIFY_OTP

        //Act
        val result = dataSource.submitVerificationOtp(body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `submitVerificationOtp() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = SubmitVerificationOTPBody()
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.VERIFY_OTP

        //Act
        val result = dataSource.submitVerificationOtp(body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `updatePasswordAndVerification() should return ApiResponse Success`() = runTest {
        //Arrange
        val body = UpdatePasswordAndVerificationBody()
        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Patch
        baseUrl = MockUrl.UPDATE_PASS_VERIFY

        //Act
        val result = dataSource.updatePasswordAndVerification(body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `updatePasswordAndVerification() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = UpdatePasswordAndVerificationBody()
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Patch
        baseUrl = MockUrl.UPDATE_PASS_VERIFY

        //Act
        val result = dataSource.updatePasswordAndVerification(body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `resetVerificationOtp() should return ApiResponse Success`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.RESEND_OTP

        //Act
        val result = dataSource.resetVerificationOtp()

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `resetVerificationOtp() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.RESEND_OTP

        //Act
        val result = dataSource.resetVerificationOtp()

        //Assert
        assertTrue { result is ApiResponse.Error }
    }
}

private object MockUrl {
    const val BASE = "/user"
    const val BY_ID = "/user/user1"
    const val RESET_REQ = "/user/reset-password/request"
    const val RESET_VALIDATE = "/user/reset-password/validate"
    const val RESET_PASSWORD = "/user/reset-password"
    const val OPTIONS = "/user/options"
    const val CHECK_EMAIL = "/user/check-email-username"
    const val VERIFY_OTP = "/user/update-password-and-verification/validate"
    const val UPDATE_PASS_VERIFY = "/user/update-password-and-verification"
    const val RESEND_OTP = "/user/resend-verification-otp"
}