package com.rollinup.apiservice.domain.attendance.pagingsource

import androidx.paging.PagingSource
import com.rollinup.apiservice.data.mapper.AttendanceMapper
import com.rollinup.apiservice.data.repository.attendance.pagingsource.GetAttendanceListByStudentPagingSource
import com.rollinup.apiservice.data.source.network.apiservice.AttendanceApiService
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByStudentResponse
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.utils.Utils
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAttendanceListByStudentPagingSourceTest {

    @MockK
    private lateinit var dataSource: AttendanceApiService

    private lateinit var mapper: AttendanceMapper
    private lateinit var pagingSource: GetAttendanceListByStudentPagingSource

    private val studentId = "student_123"
    private val queryParams = GetAttendanceListByStudentQueryParams(limit = 10, page = 1)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(Utils)
        mapper = AttendanceMapper()
        pagingSource = GetAttendanceListByStudentPagingSource(
            id = studentId,
            dataSource = dataSource,
            queryParams = queryParams,
            mapper = mapper
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `load returns Page on successful API response`() = runTest {
        // Arrange
        val mockDtoList = listOf(
            GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(
                id = "1", status = "CHECKED_IN", date = "2024-01-01"
            ),
            GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(
                id = "2", status = "Alpa", date = "2024-01-02"
            )
        )

        val mockResponseData = GetAttendanceListByStudentResponse.Data(
            record = 20,
            page = 1,
            summary = GetAttendanceListByStudentResponse.Data.Summary(),
            data = mockDtoList
        )

        val mockApiResponse = GetAttendanceListByStudentResponse(
            status = 200, message = "OK", data = mockResponseData
        )

        coEvery {
            dataSource.getAttendanceListByStudent(studentId, any())
        } returns ApiResponse.Success(mockApiResponse, io.ktor.http.HttpStatusCode.OK)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)

        // Verify Data Mapping
        assertEquals(2, result.data.size)
        assertEquals("1", result.data[0].id)
        assertEquals(AttendanceStatus.ON_TIME, result.data[0].status)

        // Verify Keys
        assertEquals(null, result.prevKey)
        assertEquals(null, result.nextKey)
    }

    @Test
    fun `load returns Page with null nextKey when data is empty`() = runTest {
        // Arrange
        val mockResponseData = GetAttendanceListByStudentResponse.Data(
            record = 0, page = 1, summary = GetAttendanceListByStudentResponse.Data.Summary(),
            data = emptyList()
        )
        val mockApiResponse = GetAttendanceListByStudentResponse(
            status = 200, message = "OK", data = mockResponseData
        )

        coEvery {
            dataSource.getAttendanceListByStudent(studentId, any())
        } returns ApiResponse.Success(mockApiResponse, io.ktor.http.HttpStatusCode.OK)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        assertTrue(result.data.isEmpty())
        assertEquals(null, result.nextKey) // No more data
    }

    @Test
    fun `load returns Page with null nextKey when data size less than limit`() = runTest {
        // Arrange
        val mockDtoList = listOf(
            GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO(id = "1")
        )
        // Only 1 item returned, but limit is 10
        val mockResponseData = GetAttendanceListByStudentResponse.Data(
            record = 1,
            page = 1,
            summary = GetAttendanceListByStudentResponse.Data.Summary(),
            data = mockDtoList
        )
        val mockApiResponse = GetAttendanceListByStudentResponse(200, "OK", mockResponseData)

        coEvery {
            dataSource.getAttendanceListByStudent(studentId, any())
        } returns ApiResponse.Success(mockApiResponse, io.ktor.http.HttpStatusCode.OK)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(null, result.nextKey) // Should be null because 1 < 10
    }

    @Test
    fun `load returns Error on API error`() = runTest {
        // Arrange
        val exception = Exception("Network Error")
        coEvery {
            dataSource.getAttendanceListByStudent(studentId, any())
        } returns ApiResponse.Error(exception)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(exception, result.throwable)
    }

    @Test(expected = RuntimeException::class)
    fun `load throws exception if generic exception occurs during call`() = runTest {
        coEvery {
            dataSource.getAttendanceListByStudent(studentId, any())
        } throws RuntimeException("Crash")

        // Act
        pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )
    }
}