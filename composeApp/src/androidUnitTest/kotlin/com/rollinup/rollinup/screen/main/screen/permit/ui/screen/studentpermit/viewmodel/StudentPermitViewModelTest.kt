@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.viewmodel

import androidx.paging.PagingData
import com.rollinup.apiservice.domain.permit.CancelPermitUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByStudentPagingUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StudentPermitViewModelTest {

    private lateinit var viewModel: StudentPermitViewModel

    @MockK
    private lateinit var getPermitByStudentPagingUseCase: GetPermitByStudentPagingUseCase

    @MockK
    private lateinit var cancelPermitUseCase: CancelPermitUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule() // UnconfinedTestDispatcher

    // ---------- Arrange Helpers ----------

    private fun arrangeGetPermitPaging(
        userId: String,
        pagingData: PagingData<PermitByStudentEntity>,
    ) {
        coEvery {
            getPermitByStudentPagingUseCase(userId, any())
        } returns flowOf(pagingData)
    }

    private fun arrangeCancelPermit(
        permitId: String,
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            cancelPermitUseCase(permitId)
        } returns flowOf(result)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = StudentPermitViewModel(
            getPermitByStudentPagingUseCase = getPermitByStudentPagingUseCase,
            cancelPermitUseCase = cancelPermitUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ---------- Tests ----------

    @Test
    fun `init() with null user should do nothing`() = runTest {
        // Arrange

        // Act
        viewModel.init(null)

        // Assert
        val state = viewModel.uiState.value
        assertEquals(LoginEntity(), state.user)

        coVerify(exactly = 0) {
            getPermitByStudentPagingUseCase(any(), any())
        }
    }

    @Test
    fun `init() should set user and load paging data`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student-1")
        val pagingData = PagingData.from(
            listOf(PermitByStudentEntity(id = "permit-1"))
        )

        arrangeGetPermitPaging(user.id, pagingData)

        // Act
        viewModel.init(user)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(user, state.user)
        assertNotNull(viewModel.pagingData.value)

        coVerify(exactly = 1) {
            getPermitByStudentPagingUseCase(user.id, any())
        }
    }

    @Test
    fun `search() should update searchQuery and refresh paging`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student-1")
        val cb = viewModel.getCallback()

        arrangeGetPermitPaging(user.id, PagingData.empty())
        viewModel.init(user)

        // Act
        cb.onSearch("izin sakit")
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals("izin sakit", state.searchQuery)

        coVerify(atLeast = 2) {
            getPermitByStudentPagingUseCase(user.id, any())
        }
    }

    @Test
    fun `filter() should update filterData and refresh paging`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student-1")
        val filter = PermitFilterData(status = listOf("APPROVED"))
        val cb = viewModel.getCallback()

        arrangeGetPermitPaging(user.id, PagingData.empty())
        viewModel.init(user)

        // Act
        cb.onFilter(filter)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(filter, state.filterData)

        coVerify(atLeast = 2) {
            getPermitByStudentPagingUseCase(user.id, any())
        }
    }

    @Test
    fun `tabChange() should update currentTab and refresh paging`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student-1")
        val cb = viewModel.getCallback()

        arrangeGetPermitPaging(user.id, PagingData.empty())
        viewModel.init(user)

        // Act
        cb.onTabChange(PermitTab.INACTIVE.ordinal)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(PermitTab.INACTIVE, state.currentTab)

        coVerify(atLeast = 2) {
            getPermitByStudentPagingUseCase(user.id, any())
        }
    }

    @Test
    fun `cancelPermit() success should update cancelState true`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student-1")
        val permitId = "permit-123"
        val cb = viewModel.getCallback()

        arrangeGetPermitPaging(user.id, PagingData.empty())
        arrangeCancelPermit(permitId, Result.Success(Unit))

        viewModel.init(user)

        // Act
        cb.onCancelPermit(permitId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.cancelState == true)
        assertFalse(state.isLoadingOverlay)

        coVerify(exactly = 1) {
            cancelPermitUseCase(permitId)
        }
    }

    @Test
    fun `cancelPermit() error should update cancelState false`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student-1")
        val permitId = "permit-123"
        val cb = viewModel.getCallback()

        arrangeGetPermitPaging(user.id, PagingData.empty())
        arrangeCancelPermit(
            permitId,
            Result.Error(NetworkError.RESPONSE_ERROR)
        )

        viewModel.init(user)

        // Act
        cb.onCancelPermit(permitId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.cancelState!!)
        assertFalse(state.isLoadingOverlay)
    }

    @Test
    fun `resetMessageState() should set message state to null`() = runTest {
        // Arrange
        val user = LoginEntity(id = "student-1")
        val permitId = "permit-123"
        val cb = viewModel.getCallback()

        arrangeGetPermitPaging(user.id, PagingData.empty())
        arrangeCancelPermit(
            permitId,
            Result.Error(NetworkError.RESPONSE_ERROR)
        )

        viewModel.init(user)
        cb.onCancelPermit(permitId)

        // Act
        cb.onResetMessageState()

        // Assert
        val state = viewModel.uiState.value
        assertNull(state.cancelState)
        assertFalse(state.isLoadingOverlay)
    }
}
