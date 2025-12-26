package com.rollinup.apiservice.domain.permit.pagingsource

import androidx.paging.PagingSource
import com.rollinup.apiservice.data.mapper.PermitMapper
import com.rollinup.apiservice.data.repository.permit.pagingsource.GetPermitByClassPagingSource
import com.rollinup.apiservice.data.source.network.apiservice.PermitApiService
import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByClassResponse
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.utils.Utils
import io.ktor.http.HttpStatusCode
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

class GetPermitByClassPagingSourceTest {

    @MockK
    private lateinit var dataSource: PermitApiService

    private lateinit var mapper: PermitMapper
    private lateinit var pagingSource: GetPermitByClassPagingSource

    private val classKey = 101
    private val queryParams = GetPermitListQueryParams(limit = 10, page = 1)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(Utils)

        mapper = PermitMapper()
        pagingSource = GetPermitByClassPagingSource(
            datasource = dataSource,
            mapper = mapper,
            classKey = classKey,
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
        val mockDtoList = listOf(
            GetPermitListByClassResponse.Data.PermitListDTO(
                id = "p1",
                name = "Izin",
                date = "2024-01-01",
                startTime = "08:00",
                reason = "Family",
                approvalStatus = "APPROVED",
                type = "ABSENCE",
                endTime = "12:00",
                student = GetPermitListByClassResponse.Data.User(
                    id = "s1", name = "John", xClass = "10A"
                ),
                createdAt = "2024-01-01"
            )
        )

        val mockResponseData = GetPermitListByClassResponse.Data(
            record = 20,
            page = 1,
            data = mockDtoList
        )
        val mockApiResponse = GetPermitListByClassResponse(
            status = 200, message = "OK", data = mockResponseData
        )

        coEvery {
            dataSource.getPermitListByClass(classKey, any())
        } returns ApiResponse.Success(mockApiResponse, HttpStatusCode.OK)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(1, result.data.size)

        val item = result.data[0]
        assertEquals("p1", item.id)
        assertEquals(ApprovalStatus.APPROVED, item.approvalStatus) // "Disetujui" -> APPROVED
        assertEquals(PermitType.ABSENCE, item.type) // "Izin" -> PERMIT
        assertEquals("s1", item.student.id)

        assertEquals(null, result.prevKey)
        assertEquals(null, result.nextKey)
    }

    @Test
    fun `load returns Page with valid nextKey when data size equals limit`() = runTest {
        // Arrange
        val limit = 10
        val mockDtoList = List(limit) {
            GetPermitListByClassResponse.Data.PermitListDTO(
                id = "p$it",
                name = "Izin",
                date = "2024-01-01",
                startTime = "08:00",
                reason = "Reason",
                approvalStatus = "APPROVAL_PENDING",
                type = "ABSENCE",
                endTime = "10:00",
                student = GetPermitListByClassResponse.Data.User(
                    id = "s$it",
                    name = "Name",
                    xClass = "10A"
                ),
                createdAt = "now"
            )
        }

        val mockResponseData = GetPermitListByClassResponse.Data(
            record = 20, page = 1, data = mockDtoList
        )
        val mockApiResponse = GetPermitListByClassResponse(200, "OK", mockResponseData)

        coEvery {
            dataSource.getPermitListByClass(classKey, any())
        } returns ApiResponse.Success(mockApiResponse, HttpStatusCode.OK)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = limit, placeholdersEnabled = false)
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(10, result.data.size)
        assertEquals(2, result.nextKey)
    }

    @Test
    fun `load returns Error on API error`() = runTest {
        // Arrange
        val exception = Exception("Server Error")
        coEvery {
            dataSource.getPermitListByClass(classKey, any())
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