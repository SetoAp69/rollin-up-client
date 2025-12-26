package com.rollinup.apiservice.data.repository.uimode

import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.common.model.UiMode
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UiModeRepositoryTest {

    private lateinit var repository: UiModeRepository

    @MockK
    private val localDataStore: LocalDataStore = mockk()

    @Before
    fun init() {
        MockKAnnotations.init(this)
        repository = UiModeRepositoryImpl(localDataStore)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `getUiMode should return mode from local data store`() = runTest {
        // Arrange
        // Assuming UiMode is an enum or sealed class you have defined
        val expectedMode = UiMode.DARK 
        coEvery { localDataStore.getLocalUiModeSetting() } returns expectedMode

        // Act
        val result = repository.getUiMode()

        // Assert
        assertEquals(expectedMode, result)
        coVerify { localDataStore.getLocalUiModeSetting() }
    }

    @Test
    fun `updateUiMode should call update on local data store`() = runTest {
        // Arrange
        val newMode = UiMode.LIGHT
        coEvery { localDataStore.updateLocalUiModeSetting(newMode) } returns Unit

        // Act
        repository.updateUiMode(newMode)

        // Assert
        coVerify { localDataStore.updateLocalUiModeSetting(newMode) }
    }
}