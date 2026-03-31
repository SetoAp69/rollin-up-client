@file:Suppress("UnusedFlow", "OptInWithoutImplementation")
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rollinup.rollinup

import com.rollinup.apiservice.domain.uimode.GetUiModeUseCase
import com.rollinup.apiservice.domain.uimode.UpdateUiModeUseCase
import com.rollinup.common.model.UiMode
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class UiModeViewModelTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @MockK
    private val getUiModeUseCase: GetUiModeUseCase = mockk()

    @MockK
    private val updateUiModeUseCase: UpdateUiModeUseCase = mockk()

    private lateinit var viewModel: UiModeViewModel

    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewModel = UiModeViewModel(
            updateUiModeUseCase = updateUiModeUseCase,
            getUiModeUseCase = getUiModeUseCase
        )
    }

    @Test
    fun `getUiMode() should fetch uiMode and update state`() = runTest {
        val expectedMode = UiMode.DARK
        coEvery { getUiModeUseCase() } returns expectedMode

        viewModel.getUiMode()

        advanceUntilIdle()

        assertEquals(expectedMode, viewModel.uiMode.value)
        coVerify(exactly = 1) { getUiModeUseCase() }
    }

    @Test
    fun `updateUiMode() should invoke use case and trigger getUiMode`() = runTest {
        val expectedMode = UiMode.LIGHT
        coEvery { updateUiModeUseCase(expectedMode) } returns Unit
        coEvery { getUiModeUseCase() } returns expectedMode

        viewModel.updateUiMode(expectedMode)

        advanceUntilIdle()

        assertEquals(expectedMode, viewModel.uiMode.value)
        coVerify(exactly = 1) { updateUiModeUseCase(expectedMode) }
        coVerify(exactly = 1) { getUiModeUseCase() }
    }
}
