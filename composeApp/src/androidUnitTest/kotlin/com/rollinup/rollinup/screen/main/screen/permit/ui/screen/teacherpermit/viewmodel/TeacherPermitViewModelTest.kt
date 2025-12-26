@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("UnusedFlow")

package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.viewmodel

import androidx.paging.PagingData
import com.rollinup.apiservice.domain.permit.GetPermitByClassListUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByClassPagingUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.component.export.FileWriter
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class TeacherPermitViewModelTest {

    private lateinit var viewModel: TeacherPermitViewModel

    @MockK
    private lateinit var getPermitByClassListUseCase: GetPermitByClassListUseCase

    @MockK
    private lateinit var getPermitByClassPagingUseCase: GetPermitByClassPagingUseCase

    @MockK
    private lateinit var fileWriter: FileWriter

    @get:Rule
    val coroutineRule = CoroutineTestRule() // UnconfinedTestDispatcher

    // ---------- Arrange Helpers ----------

    private fun arrangeGetPermitList(
        classKey: Int,
        result: Result<List<PermitByClassEntity>, NetworkError>,
    ) {
        coEvery {
            getPermitByClassListUseCase(classKey, any())
        } returns flowOf(result)
    }

    private fun arrangeGetPermitPaging(
        classKey: Int,
        pagingData: PagingData<PermitByClassEntity>,
    ) {
        coEvery {
            getPermitByClassPagingUseCase(classKey, any())
        } returns flowOf(pagingData)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TeacherPermitViewModel(
            getPermitByClassListUseCase = getPermitByClassListUseCase,
            getPermitByClassPagingUseCase = getPermitByClassPagingUseCase,
            fileWriter = fileWriter
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
        viewModel.init(null, false)

        // Assert
        val state = viewModel.uiState.value
        assertEquals(LoginEntity(), state.user)

        coVerify(exactly = 0) {
            getPermitByClassListUseCase(any(), any())
            getPermitByClassPagingUseCase(any(), any())
        }
    }

    @Test
    fun `init() should load list when desktop`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 4)
        val permits = listOf(PermitByClassEntity(id = "1"))

        arrangeGetPermitList(
            user.classKey!!,
            Result.Success(permits)
        )

        // Act
        viewModel.init(user, false)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(user, state.user)
        assertEquals(permits, state.itemList)
        assertFalse(state.isLoading)

        coVerify(exactly = 1) {
            getPermitByClassListUseCase(user.classKey!!, any())
        }
    }

    @Test
    fun `init() should load paging when mobile`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 4)
        val pagingData = PagingData.from(
            listOf(PermitByClassEntity(id = "1"))
        )

        arrangeGetPermitPaging(user.classKey!!, pagingData)

        // Act
        viewModel.init(user, true)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.isMobile)
        assertNotNull(viewModel.pagingData.value)

        coVerify(exactly = 1) {
            getPermitByClassPagingUseCase(user.classKey!!, any())
        }
    }

    @Test
    fun `search() should refresh based on device type`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 4)
        val cb = viewModel.getCallback()

        arrangeGetPermitList(user.classKey!!, Result.Success(emptyList()))
        viewModel.init(user, false)

        // Act
        cb.onSearch("izin")
        advanceUntilIdle()

        // Assert
        assertEquals("izin", viewModel.uiState.value.searchQuery)

        coVerify(atLeast = 2) {
            getPermitByClassListUseCase(user.classKey!!, any())
        }
    }

    @Test
    fun `filter() should refresh based on device type`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 4)
        val filter = PermitFilterData(status = listOf("APPROVED"))
        val cb = viewModel.getCallback()

        arrangeGetPermitPaging(user.classKey!!, PagingData.empty())
        viewModel.init(user, true)

        // Act
        cb.onFilter(filter)
        advanceUntilIdle()

        // Assert
        assertEquals(filter, viewModel.uiState.value.filterData)

        coVerify(atLeast = 2) {
            getPermitByClassPagingUseCase(user.classKey!!, any())
        }
    }

    @Test
    fun `tabChange() should update tab and refresh`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 4)
        val cb = viewModel.getCallback()

        arrangeGetPermitList(user.classKey!!, Result.Success(emptyList()))
        viewModel.init(user, false)

        // Act
        cb.onTabChange(PermitTab.INACTIVE.ordinal)
        advanceUntilIdle()

        // Assert
        assertEquals(PermitTab.INACTIVE, viewModel.uiState.value.currentTab)
    }

    @Test
    fun `selectAll() desktop should select all items`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 4)
        val permits = listOf(PermitByClassEntity(id = "1"))

        arrangeGetPermitList(
            user.classKey!!,
            Result.Success(permits)
        )

        val cb = viewModel.getCallback()
        viewModel.init(user, false)
        advanceUntilIdle()

        // Act
        cb.onSelectAll()

        // Assert
        assertEquals(permits, viewModel.uiState.value.itemSelected)
    }

    @Test
    fun `exportFile() success should write excel and update state`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 4)
        val permits = listOf(PermitByClassEntity(id = "1"))

        arrangeGetPermitList(
            user.classKey!!,
            Result.Success(permits)
        )
        coEvery { fileWriter.writeExcel(any(), any()) } returns Unit

        val cb = viewModel.getCallback()
        viewModel.init(user, false)

        // Act
        cb.onExportFile("permit.xlsx")
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.exportState == true)
        assertFalse(state.isLoadingOverlay)

        coVerify {
            fileWriter.writeExcel("permit.xlsx", any())
        }
    }

    @Test
    fun `exportFile() error should set exportState false`() = runTest {
        // Arrange
        val user = LoginEntity(classKey = 4)

        arrangeGetPermitList(
            user.classKey!!,
            Result.Error(NetworkError.RESPONSE_ERROR)
        )

        val cb = viewModel.getCallback()
        viewModel.init(user, false)

        // Act
        cb.onExportFile("permit.xlsx")
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.exportState!!)
        assertFalse(state.isLoadingOverlay)
    }

    @Test
    fun `resetMessageState() should reset exportState`() = runTest {
        // Arrange
        val cb = viewModel.getCallback()

        // Act
        cb.onResetMessageState()

        // Assert
        assertNull(viewModel.uiState.value.exportState)
    }

    @Test
    fun `resetSelection() should set selected item to empty`() {
        //Arrange
        val permit = PermitByClassEntity(id = "123")

        val cb = viewModel.getCallback()
        cb.onUpdateSelection(permit)

        //Act
        cb.onResetSelection()

        //Assert
        assertTrue { viewModel.uiState.value.itemSelected.isEmpty() }
    }

    @Test
    fun `updateSelection() should add new item in selected item list`() {
        //Arrange
        val expected = listOf(
            PermitByClassEntity("123"),
            PermitByClassEntity("124"),
            PermitByClassEntity("125"),
        )

        val cb = viewModel.getCallback()

        //Act
        cb.onUpdateSelection(expected[0])
        cb.onUpdateSelection(expected[1])
        cb.onUpdateSelection(expected[2])

        //Assert
        assertEquals(expected, viewModel.uiState.value.itemSelected)
    }

    @Test
    fun `updateSelection() should remove item from selected item list`() {
        //Arrange
        val expected = listOf(
            PermitByClassEntity("123"),
            PermitByClassEntity("124"),
            PermitByClassEntity("125"),
        )

        val cb = viewModel.getCallback()

        //Act
        cb.onUpdateSelection(expected[0])
        cb.onUpdateSelection(expected[1])
        cb.onUpdateSelection(expected[2])

        cb.onUpdateSelection(expected[2])

        //Assert
        assertEquals(expected.take(2), viewModel.uiState.value.itemSelected)
    }

    @Test
    fun `selectAll() on mobile should success fetch all permit list`() = runTest {
        //Arrange
        val user = LoginEntity(classKey = 12)
        val mockList = listOf(PermitByClassEntity(id = "123"))
        arrangeGetPermitPaging(classKey = 12, PagingData.from(mockList))
        arrangeGetPermitList(classKey = 12, result = Result.Success(mockList))

        viewModel.init(user, true)

        val cb = viewModel.getCallback()

        //Act
        cb.onSelectAll()

        //Assert
        coVerify {
            getPermitByClassListUseCase(12, any())
        }

        val state = viewModel.uiState.value
        assertEquals(mockList, state.itemSelected)
        assertTrue(state.isAllSelected)
    }


    @Test
    fun `selectAll() on mobile should failed fetch all permit list`() = runTest {
        //Arrange
        val user = LoginEntity(classKey = 12)
        val mockList = listOf(PermitByClassEntity(id = "123"))
        arrangeGetPermitPaging(classKey = 12, PagingData.from(mockList))
        arrangeGetPermitList(classKey = 12, result = Result.Error(NetworkError.RESPONSE_ERROR))

        viewModel.init(user, true)

        val cb = viewModel.getCallback()

        //Act
        cb.onSelectAll()

        //Assert
        coVerify {
            getPermitByClassListUseCase(12, any())
        }

        val state = viewModel.uiState.value
        assertTrue(state.itemSelected.isEmpty())
        assertFalse(state.isAllSelected)
    }

    @Test
    fun `selectAll() on mobile should do nothing when classKey is null`() = runTest {
        //Arrange
        val user = LoginEntity()

        viewModel.init(user, true)

        //Act
        val cb = viewModel.getCallback()
        cb.onSelectAll()

        //Assert
        coVerify(exactly = 0) {
            getPermitByClassListUseCase(any(), any())
        }
    }
}
