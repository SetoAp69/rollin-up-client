package com.rollinup.apiservice.data.repository.generalsetting

import com.rollinup.apiservice.data.mapper.GlobalSettingMapper
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.apiservice.data.source.network.apiservice.GlobalSettingApiService
import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.globalsetting.GetGlobalSettingResponse
import com.rollinup.apiservice.data.source.network.model.response.sse.GeneralSettingResponse
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.utils.Utils
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GlobalSettingRepositoryTest {

    private lateinit var repository: GlobalSettingRepository

    @MockK
    private val apiDataSource: GlobalSettingApiService = mockk()

    @MockK
    private val localDataSource: LocalDataStore = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ioDispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    // Real mapper
    private val mapper = GlobalSettingMapper()

    @Before
    fun init() {
        MockKAnnotations.init(this)

        // Mock Utils for error handling
        mockkObject(Utils)
        every { Utils.handleApiError(any<Exception>()) } returns Result.Error(NetworkError.RESPONSE_ERROR)

        repository = GlobalSettingRepositoryImpl(
            apiDataSource = apiDataSource,
            localDataSource = localDataSource,
            ioDispatcher = ioDispatcher,
            mapper = mapper
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    //region GetGlobalSetting
    @Test
    fun `getGlobalSetting should return Result Success Flow with mapped data`() = runTest {
        // Arrange
        val mockData = GetGlobalSettingResponse.Data(
            semesterStart = "2024-01-01",
            semesterEnd = "2024-06-01",
            updatedAt = "2024-01-01",
            schoolPeriodStart = "07:00",
            schoolPeriodEnd = "15:00",
            checkInPeriodStart = "06:30",
            checkInPeriodEnd = "07:30",
            latitude = 1.0,
            longitude = 1.0,
            radius = 100.0
        )
        val mockResponse = GetGlobalSettingResponse(
            status = 200, message = "OK", data = mockData
        )

        val expectedEntity = GlobalSetting(
            semesterStart = "2024-01-01",
            semesterEnd = "2024-06-01",
            updatedAt = "2024-01-01",
            _schoolPeriodStart = "07:00",
            _schoolPeriodEnd = "15:00",
            _checkInPeriodStart = "06:30",
            _checkInPeriodEnd = "07:30",
            latitude = 1.0,
            longitude = 1.0,
            radius = 100.0
        )

        coEvery { apiDataSource.getGlobalSetting() } returns ApiResponse.Success(
            mockResponse,
            HttpStatusCode.OK
        )

        // Act
        val result = repository.getGlobalSetting().first()

        // Assert
        coVerify { apiDataSource.getGlobalSetting() }
        assertTrue(result is Result.Success)
        assertEquals(expectedEntity, result.data)
    }

    @Test
    fun `getGlobalSetting should return Result Error Flow`() = runTest {
        // Arrange
        coEvery { apiDataSource.getGlobalSetting() } returns ApiResponse.Error(Exception("Network"))

        // Act
        val result = repository.getGlobalSetting().first()

        // Assert
        coVerify { apiDataSource.getGlobalSetting() }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `getGlobalSetting should return Result Error Flow when exception is thrown`() = runTest {
        // Arrange
        coEvery { apiDataSource.getGlobalSetting() } throws RuntimeException("Crash")

        // Act
        val result = repository.getGlobalSetting().first()

        // Assert
        coVerify { apiDataSource.getGlobalSetting() }
        assertTrue(result is Result.Error)
    }
    //endregion

    //region EditGlobalSetting
    @Test
    fun `editGlobalSetting should return Result Success Unit`() = runTest {
        // Arrange
        val body = EditGlobalSettingBody(latitude = 1.0, longitude = 1.0)
        coEvery { apiDataSource.editGlobalSetting(body) } returns ApiResponse.Success(
            Unit,
            HttpStatusCode.OK
        )

        // Act
        val result = repository.editGlobalSetting(body).first()

        // Assert
        coVerify { apiDataSource.editGlobalSetting(body) }
        assertTrue(result is Result.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `editGlobalSetting should return Result Error Flow`() = runTest {
        // Arrange
        val body = EditGlobalSettingBody(latitude = 1.0, longitude = 1.0)
        coEvery { apiDataSource.editGlobalSetting(body) } returns ApiResponse.Error(Exception("Failed"))

        // Act
        val result = repository.editGlobalSetting(body).first()

        // Assert
        coVerify { apiDataSource.editGlobalSetting(body) }
        assertTrue(result is Result.Error)
    }

    @Test
    fun `editGlobalSetting should return Result Error Flow when exception is thrown`() = runTest {
        // Arrange
        val body = EditGlobalSettingBody(latitude = 1.0, longitude = 1.0)
        coEvery { apiDataSource.editGlobalSetting(body) } throws RuntimeException("Error")

        // Act
        val result = repository.editGlobalSetting(body).first()

        // Assert
        coVerify { apiDataSource.editGlobalSetting(body) }
        assertTrue(result is Result.Error)
    }
    //endregion

    //region Listen (SSE)
    @Test
    fun `listen should emit Unit on successful SSE response`() = runTest {
        // Arrange
        coEvery { apiDataSource.listen() } returns flowOf(
            ApiResponse.Success(Unit, HttpStatusCode.OK)
        )

        // Act
        val resultList = repository.listen().toList()

        // Assert
        coVerify { apiDataSource.listen() }
        assertTrue(resultList.isNotEmpty())
        assertEquals(Unit, resultList.first())
    }

    @Test
    fun `listen should handle exceptions silently (print stack trace)`() = runTest {
        // Arrange
        coEvery { apiDataSource.listen() } throws RuntimeException("SSE Connection Failed")

        // Act
        val resultList = repository.listen().toList()

        // Assert
        coVerify { apiDataSource.listen() }
        assertTrue(resultList.isEmpty())
    }
    //endregion

    //region Cache (Local DataStore)
    @Test
    fun `getCachedGlobalSetting should return data from local source`() = runTest {
        // Arrange
        val mockCachedSetting = GlobalSetting(
            semesterStart = "2024", semesterEnd = "2025", updatedAt = "now",
            _schoolPeriodStart = "07:00", _schoolPeriodEnd = "15:00",
            _checkInPeriodStart = "06:00", _checkInPeriodEnd = "07:00",
            latitude = 0.0, longitude = 0.0, radius = 0.0
        )
        coEvery { localDataSource.getLocalGlobalSetting() } returns mockCachedSetting

        // Act
        val result = repository.getCachedGlobalSetting()

        // Assert
        coVerify { localDataSource.getLocalGlobalSetting() }
        assertEquals(mockCachedSetting, result)
    }

    @Test
    fun `updateCachedGlobalSetting should call local source update`() = runTest {
        // Arrange
        val newSetting = GlobalSetting(
            semesterStart = "2024", semesterEnd = "2025", updatedAt = "now",
            _schoolPeriodStart = "07:00", _schoolPeriodEnd = "15:00",
            _checkInPeriodStart = "06:00", _checkInPeriodEnd = "07:00",
            latitude = 0.0, longitude = 0.0, radius = 0.0
        )
        coEvery { localDataSource.updateGlobalSetting(newSetting) } returns Unit

        // Act
        repository.updateCachedGlobalSetting(newSetting)

        // Assert
        coVerify { localDataSource.updateGlobalSetting(newSetting) }
    }
    //endregion
}