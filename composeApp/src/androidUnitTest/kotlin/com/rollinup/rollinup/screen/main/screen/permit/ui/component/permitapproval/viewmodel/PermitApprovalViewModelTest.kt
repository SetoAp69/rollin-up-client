@file:OptIn(ExperimentalCoroutinesApi::class)
package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.viewmodel

import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.domain.permit.DoApprovalUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByIdUseCase
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.screen.main.screen.permit.model.permitapproval.PermitApprovalFormData
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.uistate.PermitApprovalUiState
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("UnusedFlow")
class PermitApprovalViewModelTest {

    private lateinit var viewModel: PermitApprovalViewModel

    @MockK
    private lateinit var getPermitByIdUseCase: GetPermitByIdUseCase

    @MockK
    private lateinit var doApprovalUseCase: DoApprovalUseCase

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

    private fun arrangeDoApproval(
        body: PermitApprovalBody,
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            doApprovalUseCase(body)
        } returns flowOf(result)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = PermitApprovalViewModel(
            getPermitByIdUseCase = getPermitByIdUseCase,
            doApprovalUseCase = doApprovalUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ---------- Tests ----------

    @Test
    fun `init() single selection should fetch detail`() = runTest {
        // Arrange
        val id = "permit-1"
        val detail = PermitDetailEntity(id = id)

        arrangeGetPermitById(id, Result.Success(detail))

        // Act
        viewModel.init(listOf(id))
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(listOf(id), state.selectedId)
        assertEquals(detail, state.detail)
        assertFalse(state.isLoading)

        coVerify(exactly = 1) {
            getPermitByIdUseCase(id)
        }
    }

    @Test
    fun `init() multiple selection should not fetch detail`() = runTest {
        // Arrange
        val ids = listOf("1", "2")

        // Act
        viewModel.init(ids)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(ids, state.selectedId)
        assertEquals(PermitDetailEntity(), state.detail)

        coVerify(exactly = 0) {
            getPermitByIdUseCase(any())
        }
    }

    @Test
    fun `getInitialData() error should stop loading`() = runTest {
        // Arrange
        val id = "permit-1"
        arrangeGetPermitById(id, Result.Error(NetworkError.RESPONSE_ERROR))

        // Act
        viewModel.init(listOf(id))
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(PermitDetailEntity(), state.detail)
    }

    @Test
    fun `updateFormData() should update uiState`() = runTest {
        // Arrange
        val formData = PermitApprovalFormData(
            approvalNote = "Approved",
            isApproved = true
        )
        val cb = viewModel.getCallback()

        // Act
        cb.onUpdateFormData(formData)

        // Assert
        assertEquals(formData, viewModel.uiState.value.formData)
    }

    @Test
    fun `validate() should return true when form is valid`() = runTest {
        // Arrange
        val formData = PermitApprovalFormData(
            approvalNote = "OK",
            isApproved = true
        )
        val cb = viewModel.getCallback()

        // Act
        val result = cb.onValidate(formData)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `submit() success should update submitState true`() = runTest {
        // Arrange
        val ids = listOf("1")
        val formData = PermitApprovalFormData(
            approvalNote = "Approved",
            isApproved = true
        )
        val body = PermitApprovalBody(
            listId = ids,
            approvalNote = "Approved",
            isApproved = true
        )

        arrangeGetPermitById("1", Result.Success(PermitDetailEntity("1")))
        arrangeDoApproval(body, Result.Success(Unit))
        viewModel.init(ids)

        val cb = viewModel.getCallback()

        // Act
        cb.onSubmit(formData)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.submitState == true)
        assertFalse(state.isLoadingOverlay)

        coVerify(exactly = 1) {
            doApprovalUseCase(body)
        }
    }

    @Test
    fun `submit() error should update submitState false`() = runTest {
        // Arrange
        val ids = listOf("1")
        val formData = PermitApprovalFormData(
            approvalNote = "Reject",
            isApproved = false
        )
        val body = PermitApprovalBody(
            listId = ids,
            approvalNote = "Reject",
            isApproved = false
        )

        arrangeDoApproval(body, Result.Error(NetworkError.RESPONSE_ERROR))
        arrangeGetPermitById("1", Result.Success(PermitDetailEntity("1")))
        viewModel.init(ids)

        val cb = viewModel.getCallback()

        // Act
        cb.onSubmit(formData)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.submitState!!)
        assertFalse(state.isLoadingOverlay)
    }

    @Test
    fun `resetMessageState() should reset submitState`() = runTest {
        // Arrange
        val cb = viewModel.getCallback()

        // Act
        cb.onResetMessageState()

        // Assert
        assertNull(viewModel.uiState.value.submitState)
    }

    @Test
    fun `reset() should reset uiState to default`() = runTest {
        // Arrange
        arrangeGetPermitById("1", Result.Success(PermitDetailEntity("1")))
        viewModel.init(listOf("1"))

        // Act
        viewModel.reset()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(PermitApprovalUiState(), state)
    }
}
