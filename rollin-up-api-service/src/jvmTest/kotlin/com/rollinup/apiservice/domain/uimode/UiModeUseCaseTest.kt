package com.rollinup.apiservice.domain.uimode

import com.rollinup.apiservice.data.repository.uimode.UiModeRepository
import com.rollinup.common.model.UiMode
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UiModeUseCaseTest {

    @MockK
    private lateinit var repository: UiModeRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `GetUiModeUseCase should call repository`() = runTest {
        val useCase = GetUiModeUseCase(repository)
        val expected = UiMode.DARK
        coEvery { repository.getUiMode() } returns expected

        val result = useCase()

        assertEquals(expected, result)
        coVerify(exactly = 1) { repository.getUiMode() }
    }

    @Test
    fun `UpdateUiModeUseCase should call repository`() = runTest {
        val useCase = UpdateUiModeUseCase(repository)
        val mode = UiMode.LIGHT
        coEvery { repository.updateUiMode(mode) } returns Unit

        useCase(mode)

        coVerify(exactly = 1) { repository.updateUiMode(mode) }
    }
}