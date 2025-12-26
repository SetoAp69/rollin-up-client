package com.rollinup.apiservice.data.source.network.datasource.globalsetting

import com.rollinup.apiservice.CoroutineTestRule
import com.rollinup.apiservice.HttpResponseData
import com.rollinup.apiservice.data.source.network.apiservice.GlobalSettingApiService
import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.globalsetting.GetGlobalSettingResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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
class GlobalSettingApiDataSourceTest {

    @get:Rule
    private val coroutineRule = CoroutineTestRule()

    @MockK
    private val sseClient: HttpClient = mockk()

    private lateinit var dataSource: GlobalSettingApiService
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
            // Install SSE plugin to support the sse() call in the data source
            install(SSE)
            expectSuccess = true
        }
        dataSource = GlobalSettingApiDataSource(
            httpClient = httpClient,
            sseClient = httpClient
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getGlobalSetting() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = GetGlobalSettingResponse(
            status = 200,
            message = "Success",
            data = GetGlobalSettingResponse.Data(
                semesterStart = "2024-01-01",
                semesterEnd = "2024-06-01",
                updatedAt = "2024-01-01T10:00:00Z",
                schoolPeriodStart = "07:00",
                schoolPeriodEnd = "15:00",
                checkInPeriodStart = "06:00",
                checkInPeriodEnd = "08:00",
                latitude = -7.0,
                longitude = 110.0,
                radius = 100.0
            )
        )
        response = HttpResponseData(
            content = Json.encodeToString(GetGlobalSettingResponse.serializer(), expected),
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BASE

        //Act
        val result = dataSource.getGlobalSetting()
        advanceUntilIdle()

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getGlobalSetting() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = "Bad Request",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BASE

        //Act
        val result = dataSource.getGlobalSetting()

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `editGlobalSetting() should return ApiResponse Success`() = runTest {
        //Arrange
        val body = EditGlobalSettingBody()
        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Patch
        baseUrl = MockUrl.EDIT

        //Act
        val result = dataSource.editGlobalSetting(body)

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `editGlobalSetting() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = EditGlobalSettingBody()
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Patch
        baseUrl = MockUrl.EDIT

        //Act
        val result = dataSource.editGlobalSetting(body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }
}

private object MockUrl {
    const val BASE = "/global-setting"
    const val SSE = "/global-setting/sse"
    const val EDIT = "/global-setting/edit"
}