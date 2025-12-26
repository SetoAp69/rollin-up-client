package com.rollinup.apiservice.data.repository.attendance

import com.rollinup.apiservice.data.mapper.AttendanceMapper
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
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.attendance.DashboardDataEntity
import com.rollinup.apiservice.model.attendance.ExportAttendanceDataEntity
import com.rollinup.apiservice.model.common.Result
import io.ktor.http.HttpStatusCode
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AttendanceRepositoryTest {
    private lateinit var repository: AttendanceRepository

    @MockK
    private val dataSource: AttendanceApiService = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ioDispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    // Using real mapper as per standard unit testing for Repositories (if logic is pure)
    private val mapper = AttendanceMapper()

    @Before
    fun init() {
        MockKAnnotations.init(this)
        repository = AttendanceRepositoryImpl(
            datasource = dataSource,
            mapper = mapper,
            ioDispatcher = ioDispatcher
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }
//
//    @Test
//    fun `getAttendanceByClassPaging() should return paging data`() = runTest {
//        //Arrange
//        val mockList = listOf(
//            AttendanceByClassEntity(attendance = AttendanceByClassEntity.Attendance(id = "123")),
//            AttendanceByClassEntity(attendance = AttendanceByClassEntity.Attendance(id = "124")),
//            AttendanceByClassEntity(attendance = AttendanceByClassEntity.Attendance(id = "125")),
//        )
//
//        val mockResponse = GetAttendanceListByClassResponse(
//            status = 200,
//            message = "",
//            data = GetAttendanceListByClassResponse.Data(
//                record = 10,
//                page = 1,
//                summary = GetAttendanceListByClassResponse.Data.Summary(),
//                data = listOf(
//                    GetAttendanceListByClassResponse.Data.Data(
//                        attendance = GetAttendanceListByClassResponse.Data.Attendance(id = "123"),
//                    ),
//                    GetAttendanceListByClassResponse.Data.Data(
//                        attendance = GetAttendanceListByClassResponse.Data.Attendance(id = "124"),
//                    ),
//
//                    GetAttendanceListByClassResponse.Data.Data(
//                        attendance = GetAttendanceListByClassResponse.Data.Attendance(id = "125"),
//                    ),
//                )
//            )
//        )
//
//        val classKey = 123
//        val queryParams = GetAttendanceListByClassQueryParams(page = 1, limit = 10)
//
//        coEvery {
//            dataSource.getAttendanceListByClass(
//                classKey,
//                queryParams.toQueryMap()
//            )
//        } returns ApiResponse.Success(mockResponse, HttpStatusCode.OK)
//
//        //Act
//        repository.getAttendanceByClassPaging(classKey, queryParams)
//
//
//        //Assert
//        coVerify {
//            dataSource.getAttendanceListByClass(any(), any())
//        }
//    }

    @Test
    fun `getAttendanceByStudentList should retunr Result Success Flow`() = runTest {
        //Arrange
        val studentId = "123"
        val mockList = listOf(
            AttendanceByStudentEntity(id = "123"),
            AttendanceByStudentEntity(id = "124"),
            AttendanceByStudentEntity(id = "125"),
        )

        val mockResponse = GetAttendanceListByStudentResponse(
            status = 200,
            message = "",
            data = GetAttendanceListByStudentResponse.Data(
                record = 10,
                page = 1,
                summary = GetAttendanceListByStudentResponse.Data.Summary(),
                data = listOf(
                    GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(id = "123"),
                    GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(id = "124"),
                    GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(id = "125"),
                )
            )
        )

        val queryParams = GetAttendanceListByStudentQueryParams()

        coEvery {
            dataSource.getAttendanceListByStudent(
                id = "123",
                query = queryParams.toQueryMap(),
            )
        } returns ApiResponse.Success(mockResponse, HttpStatusCode.OK)

        //Act
        val result = repository.getAttendanceByStudentList(studentId, queryParams).first()

        //Assert
        coVerify {
            dataSource.getAttendanceListByStudent(studentId, queryParams.toQueryMap())
        }

        assertEquals(mockList, (result as Result.Success).data)
    }

    @Test
    fun `getAttendanceByStudentList should return Result Error Flow`() = runTest {
        //Arrange
        val studentId = "123"
        val queryParams = GetAttendanceListByStudentQueryParams()

        coEvery {
            dataSource.getAttendanceListByStudent(
                id = "123",
                query = queryParams.toQueryMap(),
            )
        } returns ApiResponse.Error(Exception())

        //Act
        val result = repository.getAttendanceByStudentList(studentId, queryParams).first()

        //Assert
        coVerify {
            dataSource.getAttendanceListByStudent(studentId, queryParams.toQueryMap())
        }

        assertTrue {
            result is Result.Error
        }
    }

    @Test
    fun `getAttendanceByStudentList should return Result Error Flow when catch an exception`() =
        runTest {
            //Arrange
            val studentId = "123"

            val queryParams = GetAttendanceListByStudentQueryParams()

            coEvery {
                dataSource.getAttendanceListByStudent(
                    id = "123",
                    query = queryParams.toQueryMap(),
                )
            } coAnswers {
                throw Exception()
            }

            //Act
            val result = repository.getAttendanceByStudentList(studentId, queryParams).first()

            //Assert
            coVerify {
                dataSource.getAttendanceListByStudent(studentId, queryParams.toQueryMap())
            }

            assertTrue {
                result is Result.Error
            }
        }


    @Test
    fun `getAttendanceByStudentList should return Result Success Flow`() = runTest {
        val studentId = "123"
        // Expected Mapped Data
        val expectedList = listOf(
            AttendanceByStudentEntity(id = "123", status = AttendanceStatus.NO_DATA),
            AttendanceByStudentEntity(id = "124", status = AttendanceStatus.NO_DATA),
            AttendanceByStudentEntity(id = "125", status = AttendanceStatus.NO_DATA),
        )

        val mockResponse = GetAttendanceListByStudentResponse(
            status = 200,
            message = "",
            data = GetAttendanceListByStudentResponse.Data(
                record = 10,
                page = 1,
                summary = GetAttendanceListByStudentResponse.Data.Summary(),
                data = listOf(
                    GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(id = "123"),
                    GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(id = "124"),
                    GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(id = "125"),
                )
            )
        )

        val queryParams = GetAttendanceListByStudentQueryParams()

        coEvery {
            dataSource.getAttendanceListByStudent(
                id = studentId,
                query = queryParams.toQueryMap(),
            )
        } returns ApiResponse.Success(mockResponse, HttpStatusCode.OK)

        val result = repository.getAttendanceByStudentList(studentId, queryParams).first()

        coVerify {
            dataSource.getAttendanceListByStudent(studentId, queryParams.toQueryMap())
        }
        assertEquals(expectedList, (result as Result.Success).data)
    }

    @Test
    fun `getAttendanceByClassList should return Result Success Flow`() = runTest {
        val classKey = 101

        // Expected Entity
        val expectedEntity = listOf(
            AttendanceByClassEntity(
                student = AttendanceByClassEntity.Student(
                    id = "s1",
                    name = "John",
                    studentId = "123"
                ),
                attendance = AttendanceByClassEntity.Attendance(
                    id = "a1",
                    status = AttendanceStatus.NO_DATA,
                    checkedInAt = "07:00",
                    date = "2024-01-01"
                ),
                permit = null
            )
        )

        // Mock Response
        val mockResponse = GetAttendanceListByClassResponse(
            status = 200,
            message = "Success",
            data = GetAttendanceListByClassResponse.Data(
                record = 1,
                page = 1,
                summary = GetAttendanceListByClassResponse.Data.Summary(),
                data = listOf(
                    GetAttendanceListByClassResponse.Data.Data(
                        student = GetAttendanceListByClassResponse.Data.User(
                            id = "s1",
                            name = "John",
                            studentId = "123"
                        ),
                        attendance = GetAttendanceListByClassResponse.Data.Attendance(
                            id = "a1",
                            status = "Hadir",
                            checkedInAt = "07:00",
                            date = "2024-01-01"
                        ),
                        permit = null
                    )
                )
            )
        )

        val queryParams = GetAttendanceListByClassQueryParams()

        coEvery {
            dataSource.getAttendanceListByClass(classKey, queryParams.toQueryMap())
        } returns ApiResponse.Success(mockResponse, HttpStatusCode.OK)

        val result = repository.getAttendanceByClassList(classKey, queryParams).first()

        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `getAttendanceByClassList should return Result Error Flow`() = runTest {
        //Arrange
        val classKey = 101
        val expectedEntity = listOf(
            AttendanceByClassEntity(
                student = AttendanceByClassEntity.Student(
                    id = "s1",
                    name = "John",
                    studentId = "123"
                ),
                attendance = AttendanceByClassEntity.Attendance(
                    id = "a1",
                    status = AttendanceStatus.NO_DATA,
                    checkedInAt = "07:00",
                    date = "2024-01-01"
                ),
                permit = null
            )
        )

        val queryParams = GetAttendanceListByClassQueryParams()

        //Act
        coEvery {
            dataSource.getAttendanceListByClass(classKey, queryParams.toQueryMap())
        } returns ApiResponse.Error(Exception())

        val result = repository.getAttendanceByClassList(classKey, queryParams).first()

        //Assert
        coVerify {
            dataSource.getAttendanceListByClass(classKey, queryParams.toQueryMap())
        }
        assertTrue(result is Result.Error)
    }


    @Test
    fun `getAttendanceByClassList should return Result Error Flow when catch an exception`() =
        runTest {
            //Arrange
            val classKey = 101
            val expectedEntity = listOf(
                AttendanceByClassEntity(
                    student = AttendanceByClassEntity.Student(
                        id = "s1",
                        name = "John",
                        studentId = "123"
                    ),
                    attendance = AttendanceByClassEntity.Attendance(
                        id = "a1",
                        status = AttendanceStatus.NO_DATA,
                        checkedInAt = "07:00",
                        date = "2024-01-01"
                    ),
                    permit = null
                )
            )

            val queryParams = GetAttendanceListByClassQueryParams()

            //Act
            coEvery {
                dataSource.getAttendanceListByClass(classKey, queryParams.toQueryMap())
            } coAnswers {
                throw Exception()
            }

            val result = repository.getAttendanceByClassList(classKey, queryParams).first()

            //Assert
            coVerify {
                dataSource.getAttendanceListByClass(classKey, queryParams.toQueryMap())
            }
            assertTrue(result is Result.Error)
        }

    @Test
    fun `getAttendanceByStudentSummary should return Result Success Flow`() = runTest {
        val studentId = "123"
        val dateRange = listOf(1L, 2L)

        // Expected
        val expectedSummary = AttendanceSummaryEntity(
            checkedIn = 5,
            late = 1,
            excused = 0,
            approvalPending = 0,
            absent = 0,
            sick = 0,
            other = 0
        )

        // Mock Response
        val mockResponse = GetAttendanceSummaryResponse(
            status = 200, message = "OK",
            data = GetAttendanceSummaryResponse.Data(
                checkedIn = 5,
                late = 1,
                excused = 0,
                approvalPending = 0,
                absent = 0,
                sick = 0,
                other = 0
            )
        )

        coEvery {
            dataSource.getAttendanceByStudentSummary(studentId, any())
        } returns ApiResponse.Success(mockResponse, HttpStatusCode.OK)

        val result = repository.getAttendanceByStudentSummary(studentId, dateRange).first()

        assertTrue(result is Result.Success)
        assertEquals(expectedSummary, result.data)

        // Verify mock json string was called
        coVerify {
            dataSource.getAttendanceByStudentSummary(
                studentId,
                match { it["date"] == "[1,2]" })
        }
    }

    @Test
    fun `getAttendanceByStudentSummary should return Result Error Flow`() = runTest {

        //Arrange
        val studentId = "123"
        val dateRange = listOf(1L, 2L)

        coEvery {
            dataSource.getAttendanceByStudentSummary(studentId, any())
        } returns ApiResponse.Error(Exception())

        //Act
        val result = repository.getAttendanceByStudentSummary(studentId, dateRange).first()

        //Assert
        coVerify {
            dataSource.getAttendanceByStudentSummary(
                studentId,
                match { it["date"] == "[1,2]" })
        }
        assertTrue(result is Result.Error)
    }


    @Test
    fun `getAttendanceByStudentSummary should return Result Error Flow when catch an exception`() =
        runTest {
            //Arrange
            val studentId = "123"
            val dateRange = listOf(1L, 2L)

            coEvery {
                dataSource.getAttendanceByStudentSummary(studentId, any())
            } coAnswers {
                throw Exception()
            }

            //Act
            val result = repository.getAttendanceByStudentSummary(studentId, dateRange).first()

            //Assert
            coVerify {
                dataSource.getAttendanceByStudentSummary(
                    studentId,
                    match { it["date"] == "[1,2]" })
            }
            assertTrue(result is Result.Error)
        }

    @Test
    fun `getAttendanceByClassSummary should return Result Success Flow`() = runTest {
        val classKey = 101
        val date = 170000000L

        val expectedSummary = AttendanceSummaryEntity(
            checkedIn = 10,
            late = 0,
            excused = 2,
            approvalPending = 0,
            absent = 1,
            sick = 1,
            other = 0
        )

        val mockResponse = GetAttendanceSummaryResponse(
            status = 200, message = "OK",
            data = GetAttendanceSummaryResponse.Data(
                checkedIn = 10,
                late = 0,
                excused = 2,
                approvalPending = 0,
                absent = 1,
                sick = 1,
                other = 0
            )
        )

        coEvery {
            dataSource.getAttendanceByClassSummary(classKey, mapOf("date" to date.toString()))
        } returns ApiResponse.Success(mockResponse, HttpStatusCode.OK)

        val result = repository.getAttendanceByClassSummary(classKey, date).first()

        assertTrue(result is Result.Success)
        assertEquals(expectedSummary, result.data)
    }

    @Test
    fun `getAttendanceByClassSummary should return Result Error Flow`() = runTest {
        //Arrange
        val classKey = 101
        val date = 170000000L

        coEvery {
            dataSource.getAttendanceByClassSummary(classKey, mapOf("date" to date.toString()))
        } returns ApiResponse.Error(Exception())

        //Acrt
        val result = repository.getAttendanceByClassSummary(classKey, date).first()

        assertTrue(result is Result.Error)
    }

    @Test
    fun `getAttendanceByClassSummary should return Result Error Flow when catch an error`() =
        runTest {
            //Arrange
            val classKey = 101
            val date = 170000000L

            coEvery {
                dataSource.getAttendanceByClassSummary(classKey, mapOf("date" to date.toString()))
            } coAnswers { throw Exception() }

            //Acrt
            val result = repository.getAttendanceByClassSummary(classKey, date).first()

            assertTrue(result is Result.Error)
        }

    @Test
    fun `getAttendanceById should return Result Success Flow with mapped detail`() = runTest {
        val id = "att123"

        // Expected
        val expectedDetail = AttendanceDetailEntity(
            id = "att123",
            student = AttendanceDetailEntity.User(
                id = "u1",
                name = "Test User",
                studentId = "12345",
                xClass = "10A"
            ),
            status = AttendanceStatus.NO_DATA,
            checkedInAt = "07:30",
            updatedAt = "08:00",
            createdAt = "07:00",
            permit = null
        )

        // Mock Response
        val mockResponse = GetAttendanceByIdResponse(
            status = 200, message = "OK",
            data = GetAttendanceByIdResponse.Data(
                id = "att123",
                student = GetAttendanceByIdResponse.Data.User(
                    id = "u1",
                    name = "Test User",
                    studentId = "12345",
                    xClass = "10A"
                ),
                status = "Hadir",
                checkedInAt = "07:30",
                updatedAt = "08:00",
                createdAt = "07:00",
                permit = null
            )
        )

        coEvery { dataSource.getAttendanceById(id) } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        val result = repository.getAttendanceById(id).first()

        assertTrue(result is Result.Success)
        assertEquals(expectedDetail, (result as Result.Success).data)
    }

    @Test
    fun `getAttendanceById should return Result Error Flow`() = runTest {
        //Arrange
        val id = "att123"

        coEvery { dataSource.getAttendanceById(id) } returns ApiResponse.Error(Exception())

        val result = repository.getAttendanceById(id).first()

        assertTrue(result is Result.Error)
    }

    @Test
    fun `getAttendanceById should return Result Error when catch an error`() = runTest {
        //Arrange
        val id = "att123"
        val mockResponse = GetAttendanceByIdResponse(
            status = 200, message = "OK",
            data = GetAttendanceByIdResponse.Data(
                id = "att123",
                student = GetAttendanceByIdResponse.Data.User(
                    id = "u1",
                    name = "Test User",
                    studentId = "12345",
                    xClass = "10A"
                ),
                status = "Hadir",
                checkedInAt = "07:30",
                updatedAt = "08:00",
                createdAt = "07:00",
                permit = null
            )
        )

        coEvery { dataSource.getAttendanceById(id) } coAnswers { throw Exception() }

        //Act
        val result = repository.getAttendanceById(id).first()

        //Assert
        coVerify {
            dataSource.getAttendanceById(id)
        }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `createAttendanceData should return Result Success Unit`() = runTest {
        val body = CreateAttendanceBody(id = "s1", checkInAt = 10L, date = "2024-01-01")

        coEvery { dataSource.createAttendanceData(body) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.Created
        )

        val result = repository.createAttendanceData(body).first()

        assertTrue(result is Result.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `createAttendanceData should return Result Error on failure`() = runTest {
        val body = CreateAttendanceBody(id = "s1", checkInAt = 10L, date = "2024-01-01")
        coEvery { dataSource.createAttendanceData(body) } returns ApiResponse.Error(Exception("Failed"))

        val result = repository.createAttendanceData(body).first()

        assertTrue(result is Result.Error)
    }

    @Test
    fun `createAttendanceData should return Result Error when catch an exception`() = runTest {
        val body = CreateAttendanceBody(id = "s1", checkInAt = 10L, date = "2024-01-01")
        coEvery { dataSource.createAttendanceData(body) } coAnswers { throw Exception() }

        val result = repository.createAttendanceData(body).first()

        assertTrue(result is Result.Error)
    }

    @Test
    fun `checkIn should return Result Success Unit`() = runTest {
        val body = CheckInBody(latitude = 1.0, longitude = 1.0)

        coEvery { dataSource.checkIn(body) } returns ApiResponse.Success(Unit, HttpStatusCode.OK)

        val result = repository.checkIn(body).first()

        assertTrue(result is Result.Success)
    }

    @Test
    fun `checkIn should return Result Error flow`() = runTest {
        val body = CheckInBody(latitude = 1.0, longitude = 1.0)

        coEvery { dataSource.checkIn(body) } returns ApiResponse.Error(Exception())

        val result = repository.checkIn(body).first()

        assertTrue(result is Result.Error)
    }

    @Test
    fun `checkIn should return Result Error flow when catch an exception`() = runTest {
        val body = CheckInBody(latitude = 1.0, longitude = 1.0)

        coEvery { dataSource.checkIn(body) } coAnswers { throw Exception() }

        val result = repository.checkIn(body).first()

        assertTrue(result is Result.Error)
    }

    @Test
    fun `getDashboardData should return Result Success Flow`() = runTest {
        val studentId = "s1"
        val date = LocalDate(2024, 1, 1)

        // Expected
        val expectedDashboard = DashboardDataEntity(
            attendanceStatus = AttendanceStatus.NO_DATA,
            summary = AttendanceSummaryEntity(
                checkedIn = 1,
                late = 0,
                excused = 0,
                approvalPending = 0,
                absent = 0,
                sick = 0,
                other = 0
            )
        )

        // Mock Response
        val mockResponse = GetDashboardDataResponse(
            status = 200, message = "OK",
            data = GetDashboardDataResponse.Data(
                status = "Hadir",
                summary = GetDashboardDataResponse.Data.Summary(
                    checkedIn = 1,
                    late = 0,
                    excused = 0,
                    approvalPending = 0,
                    absent = 0,
                    sick = 0,
                    other = 0
                )
            )
        )

        coEvery {
            dataSource.getDashboardData(studentId, mapOf("date" to date.toString()))
        } returns ApiResponse.Success(mockResponse, HttpStatusCode.OK)

        val result = repository.getDashboardData(studentId, date).first()

        assertTrue(result is Result.Success)
        assertEquals(expectedDashboard, result.data)
    }

    @Test
    fun `getDashboardData should return Result Error Flow`() = runTest {
        //Arrange
        val studentId = "s1"
        val date = LocalDate(2024, 1, 1)

        coEvery {
            dataSource.getDashboardData(studentId, mapOf("date" to date.toString()))
        } returns ApiResponse.Error(Exception())

        //Act
        val result = repository.getDashboardData(studentId, date).first()

        //Assert
        coVerify {
            dataSource.getDashboardData(studentId, mapOf("date" to date.toString()))
        }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `getDashboardData should return Result Error Flow when catch an exception`() = runTest {
        //Arrange
        val studentId = "s1"
        val date = LocalDate(2024, 1, 1)

        coEvery {
            dataSource.getDashboardData(studentId, mapOf("date" to date.toString()))
        } coAnswers {
            throw Exception()
        }

        //Act
        val result = repository.getDashboardData(studentId, date).first()

        //Assert
        coVerify {
            dataSource.getDashboardData(studentId, mapOf("date" to date.toString()))
        }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `editAttendanceData should return Result Success Unit`() = runTest {
        val id = "att1"
        val body = EditAttendanceBody(status = AttendanceStatus.NO_DATA)

        coEvery { dataSource.editAttendance(id, body) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.OK
        )

        val result = repository.editAttendanceData(id, body).first()

        assertTrue(result is Result.Success)
    }

    @Test
    fun `editAttendanceData should return Result Error flow`() = runTest {
        val id = "att1"
        val body = EditAttendanceBody(status = AttendanceStatus.NO_DATA)

        coEvery { dataSource.editAttendance(id, body) } returns ApiResponse.Error(Exception())

        val result = repository.editAttendanceData(id, body).first()

        assertTrue(result is Result.Error)
    }

    @Test
    fun `editAttendanceData should return Result Error flow when catch an exception`() = runTest {
        val id = "att1"
        val body = EditAttendanceBody(status = AttendanceStatus.NO_DATA)

        coEvery { dataSource.editAttendance(id, body) } coAnswers { throw Exception()}

        val result = repository.editAttendanceData(id, body).first()

        assertTrue(result is Result.Error)
    }

    @Test
    fun `getAttendanceExportData should return Result Success with mapped data`() = runTest {
        val queryParams = GetExportAttendanceDataQueryParams()

        // Expected
        val expectedExport = ExportAttendanceDataEntity(
            sDateRange = listOf("2024-01-01"),
            data = listOf(
                ExportAttendanceDataEntity.Data(
                    fullName = "John Doe",
                    classX = "10A",
                    studentId = "123",
                    dataPerDate = listOf(
                        ExportAttendanceDataEntity.Data.AttendanceRecord(
                            sDate = "2024-01-01",
                            status = "Hadir"
                        )
                    )
                )
            )
        )

        // Mock Response
        val mockResponse = GetExportAttendanceDataResponse(
            status = 200, message = "OK",
            data = GetExportAttendanceDataResponse.Data(
                sDateRange = listOf("2024-01-01"),
                data = listOf(
                    GetExportAttendanceDataResponse.Data.Data(
                        fullName = "John Doe",
                        classX = "10A",
                        studentId = "123",
                        dataPerDate = listOf(
                            GetExportAttendanceDataResponse.Data.Data.Data(
                                date = "2024-01-01",
                                status = "Hadir"
                            )
                        )
                    )
                )
            )
        )

        coEvery { dataSource.getExportData(queryParams.toQueryMap()) } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        val result = repository.getAttendanceExportData(queryParams).first()

        assertTrue(result is Result.Success)
        assertEquals(expectedExport, result.data)
    }

    @Test
    fun `getAttendanceExportData should return Result Error flow`() = runTest {
        //Arrange
        val queryParams = GetExportAttendanceDataQueryParams()

        coEvery { dataSource.getExportData(queryParams.toQueryMap()) } returns ApiResponse.Error(Exception())

        //Act
        val result = repository.getAttendanceExportData(queryParams).first()

        //Assert
        coVerify {
            dataSource.getExportData(queryParams.toQueryMap())
        }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `getAttendanceExportData should return Result Error flow when catch an exception`() = runTest {
        //Arrange
        val queryParams = GetExportAttendanceDataQueryParams()

        coEvery { dataSource.getExportData(queryParams.toQueryMap()) } coAnswers {throw Exception()}

        //Act
        val result = repository.getAttendanceExportData(queryParams).first()

        //Assert
        coVerify {
            dataSource.getExportData(queryParams.toQueryMap())
        }
        assertTrue(result is Result.Error)
    }

}