package com.rollinup.apiservice.domain.user.pagingsource

import androidx.paging.PagingSource
import com.rollinup.apiservice.data.mapper.UserMapper
import com.rollinup.apiservice.data.repository.user.pagingsource.GetUserPagingSource
import com.rollinup.apiservice.data.source.network.apiservice.UserApiService
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserListResponse
import com.rollinup.apiservice.model.user.UserEntity
import io.ktor.http.HttpStatusCode
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetUserPagingSourceTest {

    @MockK
    private lateinit var dataSource: UserApiService

    private lateinit var mapper: UserMapper
    private lateinit var pagingSource: GetUserPagingSource

    private val queryParams = GetUserQueryParams(page = 1, limit = 10)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mapper = UserMapper() // Using real mapper since it's a pure function/transformer
        pagingSource = GetUserPagingSource(
            dataSource = dataSource,
            query = queryParams,
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
        val mockDataList = listOf(
            GetUserListResponse.Data.UserData(
                id = "u1",
                userName = "john_doe",
                classX = "10A",
                email = "john@example.com",
                firstName = "John",
                lastName = "Doe",
                studentId = "STU001",
                address = "Address",
                role = "student",
                gender = "Laki-laki"
            )
        )

        val mockResponseData = GetUserListResponse.Data(
            record = 20,
            page = 1,
            data = mockDataList
        )
        val mockApiResponse = GetUserListResponse(
            status = 200, message = "OK", data = mockResponseData
        )

        coEvery {
            dataSource.getUsersList(any())
        } returns ApiResponse.Success(mockApiResponse, HttpStatusCode.OK)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(1, result.data.size)
        
        // Verify Mapping
        val item = result.data[0]
        assertEquals("u1", item.id)
        assertEquals("John Doe", item.fullName)

        // Verify Keys
        assertEquals(null, result.prevKey)
        assertEquals(null, result.nextKey)
    }

    @Test
    fun `load returns Page with valid nextKey when data size equals limit`() = runTest {
        // Arrange
        val limit = 10
        val mockDataList = List(limit) {
            GetUserListResponse.Data.UserData(
                id = "u$it",
                userName = "user$it",
                firstName = "First",
                lastName = "Last",
                role = "student",
                gender = "Laki-laki"
            )
        }

        val mockResponseData = GetUserListResponse.Data(
            record = 100,
            page = 1,
            data = mockDataList
        )
        val mockApiResponse = GetUserListResponse(200, "OK", mockResponseData)

        coEvery {
            dataSource.getUsersList(any())
        } returns ApiResponse.Success(mockApiResponse, HttpStatusCode.OK)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = limit, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(limit, result.data.size)
        assertEquals(2, result.nextKey)
    }

    @Test
    fun `load returns Error on API error`() = runTest {
        // Arrange
        val exception = Exception("Service Unavailable")
        coEvery {
            dataSource.getUsersList(any())
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