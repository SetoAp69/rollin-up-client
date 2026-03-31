@file:Suppress("UnusedFlow", "OptInWithoutImplementation")
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rollinup.rollinup

import com.rollinup.apiservice.domain.locale.GetLocaleUseCase
import com.rollinup.apiservice.domain.locale.SetLocaleUseCase
import com.rollinup.common.model.LocaleEnum
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

class LocaleViewModelTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @MockK
    private val getLocaleUseCase: GetLocaleUseCase = mockk()

    @MockK
    private val setLocaleUseCase: SetLocaleUseCase = mockk()

    private lateinit var viewModel: LocaleViewModel

    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewModel = LocaleViewModel(
            getLocaleUseCase = getLocaleUseCase,
            setLocaleUseCase = setLocaleUseCase
        )
    }

    @Test
    fun `getLocale() should correctly fetch locale and update state`() = runTest {
        val expectedLocale = LocaleEnum.EN
        coEvery { getLocaleUseCase() } returns expectedLocale

        viewModel.getLocale()

        advanceUntilIdle()

        assertEquals(expectedLocale, viewModel.locale.value)
        coVerify(exactly = 1) { getLocaleUseCase() }
    }

    @Test
    fun `setLocale() should invoke use case and correctly update state`() = runTest {
        val expectedLocale = LocaleEnum.IN
        coEvery { setLocaleUseCase(expectedLocale) } returns Unit
        coEvery { getLocaleUseCase() } returns expectedLocale

        viewModel.setLocale(expectedLocale)

        advanceUntilIdle()

        assertEquals(expectedLocale, viewModel.locale.value)
        coVerify(exactly = 1) { setLocaleUseCase(expectedLocale) }
        coVerify(exactly = 1) { getLocaleUseCase() }
    }
}
