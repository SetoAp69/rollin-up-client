package com.rollinup.apiservice.domain.globalsetting

import com.rollinup.apiservice.data.repository.generalsetting.GlobalSettingRepository
import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.apiservice.model.common.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GlobalSettingUseCaseTest {

    @MockK
    private lateinit var repository: GlobalSettingRepository

    private lateinit var listenGlobalSettingSSE: ListenGlobalSettingSSE
    private lateinit var getGlobalSettingUseCase: GetGlobalSettingUseCase
    private lateinit var editGlobalSettingUseCase: EditGlobalSettingUseCase
    private lateinit var getCachedGlobalSettingUseCase: GetCachedGlobalSettingUseCase
    private lateinit var updateCachedGlobalSettingUseCase: UpdateCachedGlobalSettingUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        listenGlobalSettingSSE = ListenGlobalSettingSSE(repository)
        getGlobalSettingUseCase = GetGlobalSettingUseCase(repository)
        editGlobalSettingUseCase = EditGlobalSettingUseCase(repository)
        getCachedGlobalSettingUseCase = GetCachedGlobalSettingUseCase(repository)
        updateCachedGlobalSettingUseCase = UpdateCachedGlobalSettingUseCase(repository)
    }

    @Test
    fun `ListenGlobalSettingSSE should call repository`() {
        // Arrange
        val expectedFlow = flowOf(Unit)
        every { repository.listen() } returns expectedFlow

        // Act
        val result = listenGlobalSettingSSE()

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.listen() }
    }

    @Test
    fun `GetGlobalSettingUseCase should call repository`() {
        // Arrange
        val mockSetting = mockk<GlobalSetting>()
        val expectedFlow = flowOf(Result.Success(mockSetting))
        every { repository.getGlobalSetting() } returns expectedFlow

        // Act
        val result = getGlobalSettingUseCase()

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.getGlobalSetting() }
    }

    @Test
    fun `EditGlobalSettingUseCase should call repository`() {
        // Arrange
        val body = EditGlobalSettingBody(latitude = 1.0)
        val expectedFlow = flowOf(Result.Success(Unit))
        every { repository.editGlobalSetting(body) } returns expectedFlow

        // Act
        val result = editGlobalSettingUseCase(body)

        // Assert
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { repository.editGlobalSetting(body) }
    }

    @Test
    fun `GetCachedGlobalSettingUseCase should call repository`() = runTest {
        // Arrange
        val mockSetting = mockk<GlobalSetting>()
        coEvery { repository.getCachedGlobalSetting() } returns mockSetting

        // Act
        val result = getCachedGlobalSettingUseCase()

        // Assert
        assertEquals(mockSetting, result)
        coVerify(exactly = 1) { repository.getCachedGlobalSetting() }
    }

    @Test
    fun `UpdateCachedGlobalSettingUseCase should call repository`() = runTest {
        // Arrange
        val setting = mockk<GlobalSetting>()
        coEvery { repository.updateCachedGlobalSetting(setting) } returns Unit

        // Act
        updateCachedGlobalSettingUseCase(setting)

        // Assert
        coVerify(exactly = 1) { repository.updateCachedGlobalSetting(setting) }
    }
}