@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.viewmodel

import com.rollinup.apiservice.domain.permit.GetPermitByIdUseCase
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.uistate.PermitDetailUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("UnusedFlow")
class PermitDetailViewModelTest {

    private lateinit var viewModel: PermitDetailViewModel

    @MockK
    private lateinit var getPermitByIdUseCase: GetPermitByIdUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule() // UnconfinedTestDispatcher

    // ---------- Arrange Helpers ----------

    private fun arrangeGetPermitById(
        id: String,
        result: Result<PermitDetailEntity, NetworkError>,
    ) {
        coEvery {
            getPermitByIdUseCase(id)
        } returns flowOf(result)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = PermitDetailViewModel(
            getPermitByIdUseCase = getPermitByIdUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ---------- Tests ----------

    @Test
    fun `init() with blank id should do nothing`() = runTest {
        // Arrange

        // Act
        viewModel.init("")
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(PermitDetailUiState(), state)

        coVerify(exactly = 0) {
            getPermitByIdUseCase(any())
        }
    }

    @Test
    fun `init() success should load permit detail`() = runTest {
        // Arrange
        val id = "permit-1"
        val detail = PermitDetailEntity(id = id)

        arrangeGetPermitById(id, Result.Success(detail))

        // Act
        viewModel.init(id)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(detail, state.detail)

        coVerify(exactly = 1) {
            getPermitByIdUseCase(id)
        }
    }

    @Test
    fun `init() error should stop loading and keep detail null`() = runTest {
        // Arrange
        val id = "permit-1"

        arrangeGetPermitById(
            id,
            Result.Error(NetworkError.RESPONSE_ERROR)
        )

        // Act
        viewModel.init(id)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(PermitDetailEntity(), state.detail)

        coVerify(exactly = 1) {
            getPermitByIdUseCase(id)
        }
    }

    @Test
    fun `reset() should reset uiState to default`() = runTest {
        // Arrange
        val id = "permit-1"
        arrangeGetPermitById(
            id,
            Result.Error(NetworkError.RESPONSE_ERROR)
        )
        viewModel.init(id)
        advanceUntilIdle()

        // Act
        viewModel.reset()

        // Assert
        assertEquals(PermitDetailUiState(), viewModel.uiState.value)
    }
}
