package com.rollinup.apiservice.domain.attendance.pagingsource

import androidx.paging.PagingSource
import com.rollinup.apiservice.data.mapper.AttendanceMapper
import com.rollinup.apiservice.data.repository.attendance.pagingsource.GetAttendanceListByClassPaging
import com.rollinup.apiservice.data.source.network.apiservice.AttendanceApiService
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByClassResponse
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

class GetAttendanceListByClassPagingTest {

    @MockK
    private lateinit var dataSource: AttendanceApiService

    private lateinit var mapper: AttendanceMapper
    private lateinit var pagingSource: GetAttendanceListByClassPaging

    private val classKey = 101
    private val queryParams = GetAttendanceListByClassQueryParams(limit = 10, page = 1)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(Utils)
        mapper = AttendanceMapper()

        pagingSource = GetAttendanceListByClassPaging(
            classKey = classKey,
            mapper = mapper,
            dataSource = dataSource,
            queryParams = queryParams
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `load returns Page on successful API response`() = runTest {
        // Arrange
        val mockDataList = listOf(
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

        val mockResponseData = GetAttendanceListByClassResponse.Data(
            record = 20, page = 1,
            summary = GetAttendanceListByClassResponse.Data.Summary(),
            data = mockDataList
        )

        val mockApiResponse = GetAttendanceListByClassResponse(
            status = 200, message = "OK", data = mockResponseData
        )

        coEvery {
            dataSource.getAttendanceListByClass(classKey, any())
        } returns ApiResponse.Success(mockApiResponse, io.ktor.http.HttpStatusCode.OK)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(1, result.data.size)
        assertEquals("s1", result.data[0].student.id)
        assertEquals(null, result.prevKey)
        assertEquals(null, result.nextKey)
        assertEquals(null, result.nextKey)
    }

    @Test
    fun `load returns Page with valid nextKey when data size equals limit`() = runTest {
        // Arrange
        val mockDataList = List(10) { index ->
            GetAttendanceListByClassResponse.Data.Data(
                student = GetAttendanceListByClassResponse.Data.User(id = "$index"),
                attendance = null,
                permit = null
            )
        }

        val mockResponseData = GetAttendanceListByClassResponse.Data(
            record = 20, page = 1,
            summary = GetAttendanceListByClassResponse.Data.Summary(),
            data = mockDataList
        )
        val mockApiResponse = GetAttendanceListByClassResponse(200, "OK", mockResponseData)

        coEvery {
            dataSource.getAttendanceListByClass(classKey, any())
        } returns ApiResponse.Success(mockApiResponse, io.ktor.http.HttpStatusCode.OK)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(10, result.data.size)
        assertEquals(2, result.nextKey)
    }

    @Test
    fun `load returns Error on API error`() = runTest {
        // Arrange
        val exception = Exception("API Failed")
        coEvery {
            dataSource.getAttendanceListByClass(classKey, any())
        } returns ApiResponse.Error(exception)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(exception, result.throwable)
    }
}