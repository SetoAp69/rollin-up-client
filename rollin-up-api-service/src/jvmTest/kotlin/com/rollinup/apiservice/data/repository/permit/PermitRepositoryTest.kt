package com.rollinup.apiservice.data.repository.permit

import com.rollinup.apiservice.data.mapper.PermitMapper
import com.rollinup.apiservice.data.source.network.apiservice.PermitApiService
import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByStudentResponse
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.utils.Utils
import com.rollinup.apiservice.utils.Utils.getFileLink
import io.ktor.http.HttpStatusCode
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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

class PermitRepositoryTest {

    private lateinit var repository: PermitRepository

    @MockK
    private val dataSource: PermitApiService = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ioDispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    private val mapper = PermitMapper()

    @Before
    fun init() {
        MockKAnnotations.init(this)

        mockkObject(Utils)
        every { any<String>().getFileLink() } returns "http://mock.url/file.jpg"

        repository = PermitRepositoryImpl(
            dataSource = dataSource,
            mapper = mapper,
            ioDispatcher = ioDispatcher
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    //region GetPermitListByClass
    @Test
    fun `getPermitListByClass should return Result Success Flow with mapped data`() = runTest {
        // Arrange
        val classKey = 10
        val queryParams = GetPermitListQueryParams()

        val mockResponse = GetPermitListByClassResponse(
            status = 200, message = "OK",
            data = GetPermitListByClassResponse.Data(
                record = 1, page = 1,
                data = listOf(
                    GetPermitListByClassResponse.Data.PermitListDTO(
                        id = "p1",
                        name = "Sakit",
                        date = "2024-01-01",
                        startTime = "07:00",
                        reason = "Fever",
                        approvalStatus = "APPROVED",
                        type = "ABSENCE",
                        endTime = "15:00",
                        student = GetPermitListByClassResponse.Data.User(
                            id = "s1",
                            name = "John",
                            xClass = "10A"
                        ),
                        createdAt = "2023-12-31"
                    )
                )
            )
        )

        val expectedEntity = listOf(
            PermitByClassEntity(
                id = "p1",
                name = "Sakit",
                date = "2024-01-01",
                startTime = "07:00",
                reason = "Fever",
                approvalStatus = ApprovalStatus.APPROVED,
                type = PermitType.ABSENCE,
                endTime = "15:00",
                student = PermitByClassEntity.User(id = "s1", name = "John", xClass = "10A"),
                createdAt = "2023-12-31"
            )
        )

        coEvery {
            dataSource.getPermitListByClass(classKey, queryParams.toQueryMap())
        } returns ApiResponse.Success(mockResponse, HttpStatusCode.OK)

        // Act
        val result = repository.getPermitListByClass(classKey, queryParams).first()

        // Assert
        coVerify { dataSource.getPermitListByClass(classKey, queryParams.toQueryMap()) }
        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `getPermitListByClass should return Result Error Flow`() = runTest {
        // Arrange
        val classKey = 10
        val queryParams = GetPermitListQueryParams()

        coEvery {
            dataSource.getPermitListByClass(classKey, queryParams.toQueryMap())
        } returns ApiResponse.Error(Exception("API Error"))

        // Act
        val result = repository.getPermitListByClass(classKey, queryParams).first()

        // Assert
        coVerify { dataSource.getPermitListByClass(classKey, queryParams.toQueryMap()) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `getPermitListByClass should return Result Error Flow when exception is thrown`() =
        runTest {
            // Arrange
            val classKey = 10
            val queryParams = GetPermitListQueryParams()

            coEvery {
                dataSource.getPermitListByClass(classKey, queryParams.toQueryMap())
            } throws RuntimeException("Crash")

            // Act
            val result = repository.getPermitListByClass(classKey, queryParams).first()

            // Assert
            coVerify { dataSource.getPermitListByClass(classKey, queryParams.toQueryMap()) }
            assertTrue(result is Result.Error)
        }
    //endregion

    //region GetPermitListByStudent
    @Test
    fun `getPermitListByStudent should return Result Success Flow with mapped data`() = runTest {
        // Arrange
        val studentId = "s1"
        val queryParams = GetPermitListQueryParams()

        val mockResponse = GetPermitListByStudentResponse(
            status = 200, message = "OK",
            data = GetPermitListByStudentResponse.Data(
                record = 1, page = 1,
                data = listOf(
                    GetPermitListByStudentResponse.Data.PermitListDTO(
                        id = "p1", studentId = "123", name = "Sakit", date = "2024-01-01",
                        startTime = "07:00", reason = "Sick", approvalStatus = "Menunggu",
                        type = "Sakit", endTime = "15:00", createdAt = "2023-12-31"
                    )
                )
            )
        )

        val expectedEntity = listOf(
            PermitByStudentEntity(
                id = "p1",
                studentId = "123",
                name = "Sakit",
                date = "2024-01-01",
                startTime = "07:00",
                reason = "Sick",
                approvalStatus = ApprovalStatus.APPROVAL_PENDING,
                type = PermitType.ABSENCE,
                endTime = "15:00",
                createdAt = "2023-12-31"
            )
        )

        coEvery {
            dataSource.getPermitListByStudent(studentId, queryParams.toQueryMap())
        } returns ApiResponse.Success(mockResponse, HttpStatusCode.OK)

        // Act
        val result = repository.getPermitListByStudent(studentId, queryParams).first()

        // Assert
        coVerify { dataSource.getPermitListByStudent(studentId, queryParams.toQueryMap()) }
        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `getPermitListByStudent should return Result Error Flow`() = runTest {
        // Arrange
        val studentId = "s1"
        val queryParams = GetPermitListQueryParams()

        coEvery {
            dataSource.getPermitListByStudent(studentId, queryParams.toQueryMap())
        } returns ApiResponse.Error(Exception("Network"))

        // Act
        val result = repository.getPermitListByStudent(studentId, queryParams).first()

        // Assert
        coVerify { dataSource.getPermitListByStudent(studentId, queryParams.toQueryMap()) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `getPermitListByStudent should return Result Error Flow when exception is thrown`() =
        runTest {
            // Arrange
            val studentId = "s1"
            val queryParams = GetPermitListQueryParams()

            coEvery {
                dataSource.getPermitListByStudent(studentId, queryParams.toQueryMap())
            } throws RuntimeException("Unknown")

            // Act
            val result = repository.getPermitListByStudent(studentId, queryParams).first()

            // Assert
            coVerify { dataSource.getPermitListByStudent(studentId, queryParams.toQueryMap()) }
            assertTrue(result is Result.Error)
        }
    //endregion

    //region GetPermitById
    @Test
    fun `getPermitById should return Result Success Flow with mapped data`() = runTest {
        // Arrange
        val id = "p1"

        val mockResponse = GetPermitByIdResponse(
            status = 200, message = "OK",
            data = GetPermitByIdResponse.Data(
                id = "p1", date = "2024-01-01", name = "Sakit",
                student = GetPermitByIdResponse.Data.User(
                    id = "s1",
                    name = "John",
                    username = "john_d",
                    studentId = "123",
                    xClass = "10A"
                ),
                startTime = "07:00", endTime = "15:00", attachment = "file.jpg", note = "Note",
                reason = "Reason", createdAt = "now", updatedAt = "now",
                approvalStatus = "APPROVED", approvalNote = "OK",
                approvedBy = GetPermitByIdResponse.Data.User(
                    id = "t1",
                    name = "Teacher",
                    username = "teach",
                    xClass = null
                ),
                approvedAt = "later"
            )
        )

        val expectedEntity = PermitDetailEntity(
            id = "p1",
            date = "2024-01-01",
            name = "Sakit",
            student = PermitDetailEntity.User(
                id = "s1",
                name = "John",
                username = "john_d",
                studentId = "123",
                xClass = "10A"
            ),
            startTime = "07:00",
            endTime = "15:00",
            attachment = "http://mock.url/file.jpg",
            note = "Note",
            reason = "Reason",
            createdAt = "now",
            updatedAt = "now",
            approvalStatus = ApprovalStatus.APPROVED,
            approvalNote = "OK",
            approvedBy = PermitDetailEntity.User(
                id = "t1",
                name = "Teacher",
                username = "teach",
                xClass = null
            ),
            approvedAt = "later"
        )

        coEvery { dataSource.getPermitById(id) } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        // Act
        val result = repository.getPermitById(id).first()

        // Assert
        coVerify { dataSource.getPermitById(id) }
        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `getPermitById should return Result Error Flow`() = runTest {
        // Arrange
        val id = "p1"
        coEvery { dataSource.getPermitById(id) } returns ApiResponse.Error(Exception("Not Found"))

        // Act
        val result = repository.getPermitById(id).first()

        // Assert
        coVerify { dataSource.getPermitById(id) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `getPermitById should return Result Error Flow when exception is thrown`() = runTest {
        // Arrange
        val id = "p1"
        coEvery { dataSource.getPermitById(id) } throws RuntimeException("DB Error")

        // Act
        val result = repository.getPermitById(id).first()

        // Assert
        coVerify { dataSource.getPermitById(id) }
        assertTrue(result is Result.Error)
    }
    //endregion

    //region DoApproval
    @Test
    fun `doApproval should return Result Success Unit`() = runTest {
        // Arrange
        val body =
            PermitApprovalBody(listId = listOf("p1"), approvalNote = "Disetujui", isApproved = true)

        coEvery { dataSource.doApproval(body) } returns ApiResponse.Success(Unit, HttpStatusCode.OK)

        // Act
        val result = repository.doApproval(body).first()

        // Assert
        coVerify { dataSource.doApproval(body) }
        assertTrue(result is Result.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `doApproval should return Result Error Flow`() = runTest {
        // Arrange
        val body =
            PermitApprovalBody(listId = listOf("p1"), approvalNote = "Disetujui", isApproved = true)

        coEvery { dataSource.doApproval(body) } returns ApiResponse.Error(Exception("Failed"))

        // Act
        val result = repository.doApproval(body).first()

        // Assert
        coVerify { dataSource.doApproval(body) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `doApproval should return Result Error Flow when exception is thrown`() = runTest {
        // Arrange
        val body =
            PermitApprovalBody(listId = listOf("p1"), approvalNote = "Disetujui", isApproved = true)
        coEvery { dataSource.doApproval(body) } throws RuntimeException("Error")

        // Act
        val result = repository.doApproval(body).first()

        // Assert
        coVerify { dataSource.doApproval(body) }
        assertTrue(result is Result.Error)
    }
    //endregion

    //region CreatePermit
    @Test
    fun `createPermit should return Result Success Unit`() = runTest {
        // Arrange
        val body = CreateEditPermitBody(studentId = "s1", type = PermitType.ABSENCE)
        coEvery { dataSource.createPermit(body) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.Created
        )

        // Act
        val result = repository.createPermit(body).first()

        // Assert
        coVerify { dataSource.createPermit(body) }
        assertTrue(result is Result.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `createPermit should return Result Error Flow`() = runTest {
        // Arrange
        val body = CreateEditPermitBody(studentId = "s1", type = PermitType.ABSENCE)
        coEvery { dataSource.createPermit(body) } returns ApiResponse.Error(Exception("Validation Error"))

        // Act
        val result = repository.createPermit(body).first()

        // Assert
        coVerify { dataSource.createPermit(body) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `createPermit should return Result Error Flow when exception is thrown`() = runTest {
        // Arrange
        val body = CreateEditPermitBody(studentId = "s1", type = PermitType.ABSENCE)
        coEvery { dataSource.createPermit(body) } throws RuntimeException("Error")

        // Act
        val result = repository.createPermit(body).first()

        // Assert
        coVerify { dataSource.createPermit(body) }
        assertTrue(result is Result.Error)
    }
    //endregion

    //region EditPermit
    @Test
    fun `editPermit should return Result Success Unit`() = runTest {
        // Arrange
        val id = "p1"
        val body = CreateEditPermitBody(studentId = "s1", type = PermitType.ABSENCE)
        coEvery { dataSource.editPermit(id, body) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.OK
        )

        // Act
        val result = repository.editPermit(id, body).first()

        // Assert
        coVerify { dataSource.editPermit(id, body) }
        assertTrue(result is Result.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `editPermit should return Result Error Flow`() = runTest {
        // Arrange
        val id = "p1"
        val body = CreateEditPermitBody(studentId = "s1", type = PermitType.ABSENCE)
        coEvery {
            dataSource.editPermit(
                id,
                body
            )
        } returns ApiResponse.Error(Exception("Bad Request"))

        // Act
        val result = repository.editPermit(id, body).first()

        // Assert
        coVerify { dataSource.editPermit(id, body) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `editPermit should return Result Error Flow when exception is thrown`() = runTest {
        // Arrange
        val id = "p1"
        val body = CreateEditPermitBody(studentId = "s1", type = PermitType.ABSENCE)
        coEvery { dataSource.editPermit(id, body) } throws RuntimeException("Error")

        // Act
        val result = repository.editPermit(id, body).first()

        // Assert
        coVerify { dataSource.editPermit(id, body) }
        assertTrue(result is Result.Error)
    }
    //endregion

    //region CancelPermit
    @Test
    fun `cancelPermit should return Result Success Unit`() = runTest {
        // Arrange
        val id = "p1"
        coEvery { dataSource.cancelPermitRequest(id) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.OK
        )

        // Act
        val result = repository.cancelPermit(id).first()

        // Assert
        coVerify { dataSource.cancelPermitRequest(id) }
        assertTrue(result is Result.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `cancelPermit should return Result Error Flow`() = runTest {
        // Arrange
        val id = "p1"
        coEvery { dataSource.cancelPermitRequest(id) } returns ApiResponse.Error(Exception("Cannot cancel"))

        // Act
        val result = repository.cancelPermit(id).first()

        // Assert
        coVerify { dataSource.cancelPermitRequest(id) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `cancelPermit should return Result Error Flow when exception is thrown`() = runTest {
        // Arrange
        val id = "p1"
        coEvery { dataSource.cancelPermitRequest(id) } throws RuntimeException("Error")

        // Act
        val result = repository.cancelPermit(id).first()

        // Assert
        coVerify { dataSource.cancelPermitRequest(id) }
        assertTrue(result is Result.Error)
    }
    //endregion
}