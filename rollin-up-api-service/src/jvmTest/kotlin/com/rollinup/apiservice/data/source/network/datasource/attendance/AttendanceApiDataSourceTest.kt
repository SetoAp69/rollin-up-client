package com.rollinup.apiservice.data.source.network.datasource.attendance

import com.rollinup.apiservice.CoroutineTestRule
import com.rollinup.apiservice.HttpResponseData
import com.rollinup.apiservice.data.source.network.apiservice.AttendanceApiService
import com.rollinup.apiservice.data.source.network.model.request.attendance.CheckInBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.CreateAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.EditAttendanceBody
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetExportAttendanceDataQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByStudentResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceSummaryResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetDashboardDataResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetExportAttendanceDataResponse
import com.rollinup.apiservice.model.common.MultiPlatformFile
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
class AttendanceApiDataSourceTest {
    @get:Rule
    private val coroutineRule = CoroutineTestRule()

    private lateinit var dataSource: AttendanceApiService

    private lateinit var httpClient: HttpClient

    private var baseUrl = MockUrl.BASE

    private var method = HttpMethod.Get

    private fun HttpRequestData.verifyUrl(method: HttpMethod, path: String): Boolean {
        return url.encodedPath == path && this.method == method
    }

    private var response = HttpResponseData()

    @Before
    fun init() {
        httpClient = HttpClient(
            engine = MockEngine.create {
                dispatcher = UnconfinedTestDispatcher()
                addHandler { req ->
                    when {
                        req.verifyUrl(method, baseUrl) -> {
                            println("Method = $method")
                            println("Url = $baseUrl")

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
                        this.prettyPrint = true
                    }
                )
            }
            expectSuccess = true
        }
        dataSource = AttendanceApiDataSource(
            httpClient = httpClient
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun `getAttendanceListByStudent() should return ApiResponse Success`() = runTest {
        //Arrange
        val expected = GetAttendanceListByStudentResponse()
        response = HttpResponseData(
            content = Json.encodeToString(GetAttendanceListByStudentResponse()),
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.GET_ATTENDANCE_BY_STUDENT

        //Act
        val result = dataSource.getAttendanceListByStudent(
            id = "id", query = GetAttendanceListByStudentQueryParams().toQueryMap()
        )
        advanceUntilIdle()

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getAttendanceListByStudent() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = Json.encodeToString(GetAttendanceListByStudentResponse()),
            status = HttpStatusCode.BadRequest,
            contentType = ContentType.Application.Json
        )
        method = HttpMethod.Get
        baseUrl = MockUrl.GET_ATTENDANCE_BY_STUDENT

        //Act
        val result = dataSource.getAttendanceListByStudent(
            id = "id", query = GetAttendanceListByStudentQueryParams().toQueryMap()
        )
        advanceUntilIdle()

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `getAttendanceListByClass() should return ApiResponse Success with correct response data`() =
        runTest {
            //Arrange
            val expected = GetAttendanceListByClassResponse(
                status = 200,
                message = "Success",
                data = GetAttendanceListByClassResponse.Data(
                    record = 12,
                    page = 1,
                    summary = GetAttendanceListByClassResponse.Data.Summary(),
                    data = listOf(
                        GetAttendanceListByClassResponse.Data.Data(
                            attendance = GetAttendanceListByClassResponse.Data.Attendance(
                                id = "123",
                                status = "ABSENT"
                            )
                        )
                    )
                )
            )

            response = HttpResponseData(
                content = Json.encodeToString(expected),
                status = HttpStatusCode.OK
            )

            method = HttpMethod.Get
            baseUrl = MockUrl.GET_ATTENDANCE_BY_CLASS

            //Act
            val result = dataSource.getAttendanceListByClass(
                classKey = 0,
                query = GetAttendanceListByClassQueryParams().toQueryMap()
            )

            //Assert
            assertTrue { result is ApiResponse.Success }
            assertEquals(expected, (result as ApiResponse.Success).data)
        }

    @Test
    fun `getAttendanceListByClass() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )

        method = HttpMethod.Get
        baseUrl = MockUrl.GET_ATTENDANCE_BY_CLASS

        //Act
        val result = dataSource.getAttendanceListByClass(
            classKey = 0,
            query = GetAttendanceListByClassQueryParams().toQueryMap()
        )

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `getAttendanceByStudentSummary should return ApiResponse Success with correct data`() =
        runTest {
            ///Arrange
            val expected = GetAttendanceSummaryResponse(
                status = 200,
                message = "Success",
                data = GetAttendanceSummaryResponse.Data(
                    checkedIn = 67,
                    late = 67,
                    excused = 67,
                    approvalPending = 67,
                    absent = 67,
                    sick = 67,
                    other = 67
                )
            )

            response = HttpResponseData(
                status = HttpStatusCode.OK,
                content = Json.encodeToString(expected)
            )

            method = HttpMethod.Get
            baseUrl = MockUrl.GET_ATTENDANCE_BY_STUDENT_SUMMARY

            //Act
            val result = dataSource.getAttendanceByStudentSummary(
                studentId = "id",
                query = GetAttendanceListByStudentQueryParams(search = "search").toQueryMap()
            )

            //Assert
            assertTrue { result is ApiResponse.Success }
            assertEquals(expected, (result as ApiResponse.Success).data)
        }

    @Test
    fun `getAttendanceByStudentSummary should return ApiResponse Error`() =
        runTest {
            ///Arrange
            response = HttpResponseData(
                status = HttpStatusCode.BadRequest,
                content = "Error"
            )

            method = HttpMethod.Get
            baseUrl = MockUrl.GET_ATTENDANCE_BY_STUDENT_SUMMARY

            //Act
            val result = dataSource.getAttendanceByStudentSummary(
                studentId = "id",
                query = GetAttendanceListByStudentQueryParams(search = "search").toQueryMap()
            )

            //Assert
            assertTrue { result is ApiResponse.Error }
        }

    @Test
    fun `getAttendanceByClassSummary should return ApiResponse Success with correct data`() =
        runTest {
            ///Arrange
            val expected = GetAttendanceSummaryResponse(
                status = 200,
                message = "Success",
                data = GetAttendanceSummaryResponse.Data(
                    checkedIn = 67,
                    late = 67,
                    excused = 67,
                    approvalPending = 67,
                    absent = 67,
                    sick = 67,
                    other = 67
                )
            )

            response = HttpResponseData(
                status = HttpStatusCode.OK,
                content = Json.encodeToString(expected)
            )

            method = HttpMethod.Get
            baseUrl = MockUrl.GET_ATTENDANCE_BY_CLASS_SUMMARY

            //Act
            val result = dataSource.getAttendanceByClassSummary(
                classKey = 0,
                query = GetAttendanceListByClassQueryParams(search = "search").toQueryMap()
            )

            //Assert
            assertTrue { result is ApiResponse.Success }
            assertEquals(expected, (result as ApiResponse.Success).data)
        }

    @Test
    fun `getAttendanceByClassSummary should return ApiResponse Error`() =
        runTest {
            ///Arrange
            response = HttpResponseData(
                status = HttpStatusCode.BadRequest,
                content = "Error"
            )

            method = HttpMethod.Get
            baseUrl = MockUrl.GET_ATTENDANCE_BY_CLASS_SUMMARY

            //Act
            val result = dataSource.getAttendanceByClassSummary(
                classKey = 0,
                query = GetAttendanceListByClassQueryParams(search = "search").toQueryMap()
            )

            //Assert
            assertTrue { result is ApiResponse.Error }
        }

    @Test
    fun `getAttendanceById should return ApiResponse Success with correct data`() =
        runTest {
            ///Arrange
            val expected = GetAttendanceByIdResponse(
                status = 200,
                message = "Success",
                data = GetAttendanceByIdResponse.Data(
                    id = "id",
                )
            )

            response = HttpResponseData(
                status = HttpStatusCode.OK,
                content = Json.encodeToString(expected)
            )

            method = HttpMethod.Get
            baseUrl = MockUrl.GET_ATTENDANCE_BY_ID

            //Act
            val result = dataSource.getAttendanceById("id")

            //Assert
            assertTrue { result is ApiResponse.Success }
            assertEquals(expected, (result as ApiResponse.Success).data)
        }

    @Test
    fun `getAttendanceById should return ApiResponse Error`() =
        runTest {
            ///Arrange
            response = HttpResponseData(
                status = HttpStatusCode.BadRequest,
                content = "Error"
            )

            method = HttpMethod.Get
            baseUrl = MockUrl.GET_ATTENDANCE_BY_ID

            //Act
            val result = dataSource.getAttendanceById("id")

            //Assert
            assertTrue { result is ApiResponse.Error }
        }

    @Test
    fun `createAttendanceData should return ApiResponse Success`() =
        runTest {
            ///Arrange
            val body = CreateAttendanceBody(
                id = "id",
                date = "2003-08-06",
                checkInAt = 100L
            )

            response = HttpResponseData(
                status = HttpStatusCode.Created,
                content = Unit.toString(),
                contentType = ContentType.Any
            )

            method = HttpMethod.Post
            baseUrl = MockUrl.BASE

            //Act
            val result = dataSource.createAttendanceData(body)

            //Assert
            assertTrue { result is ApiResponse.Success }
        }

    @Test
    fun `createAttendanceData should return ApiResponse Error`() =
        runTest {
            ///Arrange
            val body = CreateAttendanceBody(
                id = "id",
                date = "2003-08-06",
                checkInAt = 100L
            )

            response = HttpResponseData(
                status = HttpStatusCode.BadRequest,
                content = Unit.toString(),
            )

            method = HttpMethod.Post
            baseUrl = MockUrl.BASE

            //Act
            val result = dataSource.createAttendanceData(body)

            //Assert
            assertTrue { result is ApiResponse.Error }
        }

    @Test
    fun `checkIn should return ApiResponse Success`() =
        runTest {
            //Arrange
            val body = CheckInBody(
                latitude = 100.0,
                longitude = 100.0,
                attachment = mockk<MultiPlatformFile>(relaxed = true),
                checkedInAt = 0L,
                date = "2003-08-06"
            )

            response = HttpResponseData(
                status = HttpStatusCode.Created,
                content = Unit.toString(),
            )

            method = HttpMethod.Post
            baseUrl = MockUrl.CHECK_IN

            //Act
            val result = dataSource.checkIn(body)

            //Assert
            assertTrue { result is ApiResponse.Success }
        }


    @Test
    fun `checkIn should return ApiResponse Error`() =
        runTest {
            //Arrange
            val body = CheckInBody(
                latitude = 100.0,
                longitude = 100.0,
                attachment = mockk<MultiPlatformFile>(relaxed = true),
                checkedInAt = 0L,
                date = "2003-08-06"
            )

            response = HttpResponseData(
                status = HttpStatusCode.BadRequest,
                content = Unit.toString(),
            )

            method = HttpMethod.Post
            baseUrl = MockUrl.CHECK_IN

            //Act
            val result = dataSource.checkIn(body)

            //Assert
            assertTrue { result is ApiResponse.Error }
        }

    @Test
    fun `getDashboardData() should return ApiResponse Success with correct data`() =
        runTest {
            //Arrange
            val expected = GetDashboardDataResponse(
                status = 200,
                message = "Success",
                data = GetDashboardDataResponse.Data(
                    status = "ABSENT"
                )
            )

            response = HttpResponseData(
                content = Json.encodeToString(expected),
                status = HttpStatusCode.OK
            )

            method = HttpMethod.Get
            baseUrl = MockUrl.GET_DASHBOARD_DATA

            //Act
            val result = dataSource.getDashboardData("id", mapOf("search" to "search"))

            //Assert
            assertTrue { result is ApiResponse.Success }
            assertEquals(expected, (result as ApiResponse.Success).data)
        }

    @Test
    fun `getDashboardData() should return ApiResponse Error`() =
        runTest {
            //Arrange
            response = HttpResponseData(
                content = "Error",
                status = HttpStatusCode.BadRequest
            )

            method = HttpMethod.Get
            baseUrl = MockUrl.GET_DASHBOARD_DATA

            //Act
            val result = dataSource.getDashboardData("id", mapOf("search" to "search"))

            //Assert
            assertTrue { result is ApiResponse.Error }
        }

    @Test
    fun `editAttendance() should return ApiResponse Success`() = runTest {
        //Arrange
        val body = EditAttendanceBody(
            latitude = 100.0
        )
        response = HttpResponseData(
            content = {}.toString(),
            status = HttpStatusCode.Accepted
        )

        method = HttpMethod.Put
        baseUrl = MockUrl.EDIT

        //Act
        val result = dataSource.editAttendance("id", body)

        //Assert
        assertTrue { result is ApiResponse.Success }
    }

    @Test
    fun `editAttendance() should return ApiResponse Error`() = runTest {
        //Arrange
        val body = EditAttendanceBody(
            latitude = 100.0
        )
        response = HttpResponseData(
            content = {}.toString(),
            status = HttpStatusCode.BadRequest
        )

        method = HttpMethod.Put
        baseUrl = MockUrl.EDIT

        //Act
        val result = dataSource.editAttendance("id", body)

        //Assert
        assertTrue { result is ApiResponse.Error }
    }

    @Test
    fun `getExportData() should return ApiResponse Success with correct data`() = runTest {
        //Arrange
        val expected = GetExportAttendanceDataResponse(
            status = 200,
            message = "Success",
            data = GetExportAttendanceDataResponse.Data(
                sDateRange = listOf("2003-08-06"),
                data = listOf(
                    GetExportAttendanceDataResponse.Data.Data(
                        studentId = "id"
                    )
                )
            )
        )

        response = HttpResponseData(
            content = Json.encodeToString(expected),
            status = HttpStatusCode.OK
        )

        method = HttpMethod.Get
        baseUrl = MockUrl.GET_EXPORT_DATA

        //Act
        val result =
            dataSource.getExportData(GetExportAttendanceDataQueryParams(dateRange = "dateRange").toQueryMap())

        //Assert
        assertTrue { result is ApiResponse.Success }
        assertEquals(expected, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getExportData() should return ApiResponse Error`() = runTest {
        //Arrange
        response = HttpResponseData(
            content = "Error",
            status = HttpStatusCode.BadRequest
        )

        method = HttpMethod.Get
        baseUrl = MockUrl.GET_EXPORT_DATA

        //Act
        val result =
            dataSource.getExportData(GetExportAttendanceDataQueryParams(dateRange = "dateRange").toQueryMap())

        //Assert
        assertTrue { result is ApiResponse.Error }
    }
}

private object MockUrl {
    const val GET_ATTENDANCE_BY_STUDENT = "/attendance/by-student/id"
    const val GET_ATTENDANCE_BY_STUDENT_SUMMARY = "/attendance/by-student/id/summary"
    const val GET_ATTENDANCE_BY_CLASS = "/attendance/by-class/0"
    const val GET_ATTENDANCE_BY_CLASS_SUMMARY = "/attendance/by-class/0/summary"
    const val GET_ATTENDANCE_BY_ID = "/attendance/id"
    const val BASE = "/attendance"
    const val EDIT = "/attendance/id"
    const val CHECK_IN = "/attendance/check-in"
    const val GET_DASHBOARD_DATA = "/dashboard"
    const val GET_EXPORT_DATA = "/attendance/export"
}
