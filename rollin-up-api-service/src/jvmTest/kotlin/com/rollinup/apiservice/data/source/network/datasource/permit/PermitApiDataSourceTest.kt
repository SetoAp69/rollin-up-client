package com.rollinup.apiservice.data.source.network.datasource.permit

import com.rollinup.apiservice.CoroutineTestRule
import com.rollinup.apiservice.HttpResponseData
import com.rollinup.apiservice.data.source.network.apiservice.PermitApiService
import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByStudentResponse
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
class PermitApiDataSourceTest {

    @get:Rule
    private val coroutineRule = CoroutineTestRule()

    private lateinit var dataSource: PermitApiService
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
        dataSource = PermitApiDataSource(
            httpClient = httpClient
        )
    }

    @After
    fun tearDown(){
        unmockkAll()
    }


    @Test
    fun `getPermitListByStudent() should return ApiResponse Success`() = runTest {
        // Arrange
        val expected = GetPermitListByStudentResponse()

        response = HttpResponseData(
            content = Json.encodeToString(GetPermitListByStudentResponse.serializer(), expected),
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BY_STUDENT

        // Act
        val result = dataSource.getPermitListByStudent(
            id = "studentId",
            queryParams = mapOf("page" to "1")
        )
        advanceUntilIdle()

        // Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getPermitListByStudent() should return ApiResponse Error`() = runTest {
        // Arrange
        response = HttpResponseData(
            content = "Bad Request",
            status = HttpStatusCode.BadRequest,
            contentType = ContentType.Application.Json
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BY_STUDENT

        // Act
        val result = dataSource.getPermitListByStudent(
            id = "studentId",
            queryParams = emptyMap()
        )
        advanceUntilIdle()

        // Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `getPermitListByClass() should return ApiResponse Success`() = runTest {
        // Arrange
        val expected = GetPermitListByClassResponse(
            status = 200,
            message = "Success",
            data = GetPermitListByClassResponse.Data(
                record = 10,
                page = 1,
                data = listOf(
                    GetPermitListByClassResponse.Data.PermitListDTO(
                        id = "1",
                        name = "John Doe",
                        date = "2024-01-01",
                        startTime = "08:00",
                        reason = "Sick",
                        approvalStatus = "APPROVAL_PENDING",
                        type = "DISPENSATIOn",
                        endTime = "10:00",
                        student = GetPermitListByClassResponse.Data.User(
                            id = "s1",
                            name = "John Doe",
                            xClass = "X A"
                        ),
                        createdAt = "2024-01-01T08:00:00Z"
                    )
                )
            )
        )

        response = HttpResponseData(
            content = Json.encodeToString(GetPermitListByClassResponse.serializer(), expected),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BY_CLASS

        // Act
        val result = dataSource.getPermitListByClass(
            classKey = 1,
            queryParams = mapOf("search" to "abc")
        )

        // Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getPermitListByClass() should return ApiResponse Error`() = runTest {
        // Arrange
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.InternalServerError
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BY_CLASS

        // Act
        val result = dataSource.getPermitListByClass(
            classKey = 1,
            queryParams = emptyMap()
        )

        // Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `getPermitById() should return ApiResponse Success`() = runTest {
        // Arrange
        val expected = GetPermitByIdResponse(
            status = 200,
            message = "Success",
            data = GetPermitByIdResponse.Data(
                id = "permit_123",
                date = "2024-01-01",
                name = "Sick Leave",
                student = GetPermitByIdResponse.Data.User(
                    id = "student_1",
                    name = "John Doe",
                    username = "johndoe",
                    studentId = "STU001",
                    xClass = "X A"
                ),
                startTime = "08:00",
                endTime = "12:00",
                attachment = "path/to/file.jpg",
                note = "Medical checkup",
                reason = "Sick",
                createdAt = "2024-01-01T07:00:00Z",
                updatedAt = "2024-01-01T07:00:00Z",
                approvalStatus = "APPROVAL_PENDING",
                approvalNote = null,
                approvedBy = null,
                approvedAt = null
            )
        )

        response = HttpResponseData(
            content = Json.encodeToString(GetPermitByIdResponse.serializer(), expected),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BY_ID

        // Act
        val result = dataSource.getPermitById(id = "permitId")

        // Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getPermitById() should return ApiResponse Error`() = runTest {
        // Arrange
        response = HttpResponseData(
            content = "Not Found",
            status = HttpStatusCode.NotFound
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.BY_ID

        // Act
        val result = dataSource.getPermitById("permitId")

        // Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `cancelPermitRequest() should return ApiResponse Success`() = runTest {
        // Arrange
        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Put
        baseUrl = MockUrl.CANCEL

        // Act
        val result = dataSource.cancelPermitRequest(id = "permitId")

        // Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `cancelPermitRequest() should return ApiResponse Error`() = runTest {
        // Arrange
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Put
        baseUrl = MockUrl.CANCEL

        // Act
        val result = dataSource.cancelPermitRequest(id = "permitId")

        // Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `doApproval() should return ApiResponse Success`() = runTest {
        // Arrange
        val body = PermitApprovalBody()

        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.APPROVAL

        // Act
        val result = dataSource.doApproval(body)

        // Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `doApproval() should return ApiResponse Error`() = runTest {
        // Arrange
        val body = PermitApprovalBody()

        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.APPROVAL

        // Act
        val result = dataSource.doApproval(body)

        // Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `createPermit() should return ApiResponse Success`() = runTest {
        // Arrange
        val body = CreateEditPermitBody()

        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.Created
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.BASE

        // Act
        val result = dataSource.createPermit(body)

        // Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `createPermit() should return ApiResponse Error`() = runTest {
        // Arrange
        val body = CreateEditPermitBody()

        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.BASE

        // Act
        val result = dataSource.createPermit(body)

        // Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `editPermit() should return ApiResponse Success`() = runTest {
        // Arrange
        val body = CreateEditPermitBody()

        response = HttpResponseData(
            content = Unit.toString(),
            status = HttpStatusCode.OK
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.BY_ID

        // Act
        val result = dataSource.editPermit("permitId", body)

        // Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(Unit, (result as ApiResponse.Success).data)
    }

    @Test
    fun `editPermit() should return ApiResponse Error`() = runTest {
        // Arrange
        val body = CreateEditPermitBody()

        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )
        method = HttpMethod.Post
        baseUrl = MockUrl.BY_ID

        // Act
        val result = dataSource.editPermit("permitId", body)

        // Assert
        assertTrue { result is ApiResponse.Error }
    }
}

private object MockUrl {
    const val BASE = "/permit"
    const val BY_STUDENT = "/permit/by-student/studentId"
    const val BY_CLASS = "/permit/by-class/1"
    const val BY_ID = "/permit/permitId"
    const val CANCEL = "/permit/permitId/cancel"
    const val APPROVAL = "/permit/approval"
}