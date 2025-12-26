package com.rollinup.apiservice.data.source.network.datasource.auth

import com.rollinup.apiservice.CoroutineTestRule
import com.rollinup.apiservice.HttpResponseData
import com.rollinup.apiservice.data.source.network.apiservice.AuthApiService
import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.auth.LoginResponse
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
class AuthApiDataSourceTest {

    @get:Rule
    private val coroutineRule = CoroutineTestRule()

    private lateinit var dataSource: AuthApiService
    private lateinit var httpClient: HttpClient

    private var baseUrl = MockUrl.LOGIN_JWT
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
        dataSource = AuthApiDataSource(
            httpClient = httpClient
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun `loginJWT() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = LoginResponse(
            status = 200,
            message = "Success",
            data = LoginResponse.Data(
                accessToken = "access_token",
                refreshToken = "refresh_token",
                data = LoginResponse.Data.UserData(
                    id = "user1",
                    deviceId = "device123",
                    userName = "jdoe",
                    email = "jdoe@example.com",
                    firstName = "John",
                    lastName = "Doe",
                    role = "admin",
                    gender = "M",
                    classX = "X A",
                    classId = "class1",
                    classKey = 1,
                    isVerified = true
                )
            )
        )

        response = HttpResponseData(
            content = Json.encodeToString(LoginResponse.serializer(), expected),
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.LOGIN_JWT

        //Act
        val result = dataSource.loginJWT(token = "dummy_token")
        advanceUntilIdle()

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `loginJWT() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = "Unauthorized",
            status = HttpStatusCode.Unauthorized
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.LOGIN_JWT

        //Act
        val result = dataSource.loginJWT(token = "dummy_token")

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `login() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = LoginResponse(
            status = 200,
            message = "Success",
            data = LoginResponse.Data(
                accessToken = "access_token",
                refreshToken = "refresh_token",
                data = LoginResponse.Data.UserData(
                    id = "user1",
                    deviceId = "device123",
                    userName = "jdoe",
                    email = "jdoe@example.com",
                    firstName = "John",
                    lastName = "Doe",
                    role = "admin",
                    gender = "M",
                    classX = "X A",
                    classId = "class1",
                    classKey = 1,
                    isVerified = true
                )
            )
        )
        val body = LoginBody()

        response = HttpResponseData(
            content = Json.encodeToString(LoginResponse.serializer(), expected),
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.LOGIN

        //Act
        val result = dataSource.login(body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `login() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = LoginBody()
        response = HttpResponseData(
            content = "Bad Request",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.LOGIN

        //Act
        val result = dataSource.login(body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `updatePasswordAndDevice() should return ApiResponse Success`() = runTest {
        //Arrange
        val body = UpdatePasswordAndVerificationBody()

        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Patch
        baseUrl = MockUrl.UPDATE_Pass_Device

        //Act
        val result = dataSource.updatePasswordAndDevice(id = "user1", token = "token", body = body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `updatePasswordAndDevice() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = UpdatePasswordAndVerificationBody()

        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Patch
        baseUrl = MockUrl.UPDATE_Pass_Device

        //Act
        val result = dataSource.updatePasswordAndDevice(id = "user1", token = "token", body = body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }
}

private object MockUrl {
    const val LOGIN_JWT = "/auth/login"
    const val LOGIN = "/auth/login"
    const val UPDATE_Pass_Device = "/user/user1/update-password-and-device"
}