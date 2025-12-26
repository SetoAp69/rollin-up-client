@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.viewmodel

import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.domain.user.GetUserListUseCase
import com.rollinup.apiservice.domain.user.GetUserOptionsUseCase
import com.rollinup.apiservice.domain.user.GetUserPagingUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.model.user.UserOptionEntity
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.component.export.FileWriter
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterFilterData
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

@Suppress("UnusedFlow")
class StudentCenterViewModelTest {

    private lateinit var viewModel: StudentCenterViewModel

    @MockK
    private lateinit var getUserListUseCase: GetUserListUseCase

    @MockK
    private lateinit var getUserPagingUseCase: GetUserPagingUseCase

    @MockK
    private lateinit var getUserOptionUseCase: GetUserOptionsUseCase

    @MockK
    private lateinit var fileWriter: FileWriter

    @get:Rule
    val coroutineRule = CoroutineTestRule() // UnconfinedTestDispatcher

    private fun arrangeGetUserList(
        queryParams: GetUserQueryParams,
        result: Result<List<UserEntity>, NetworkError>,
    ) {
        coEvery { getUserListUseCase(queryParams) } returns flowOf(result)
    }

    private fun arrangeGetUserPagingUseCase(
        queryParams: GetUserQueryParams,
        result: List<UserEntity>,
    ) {
        coEvery {
            getUserPagingUseCase(queryParams)
        } returns flowOf(PagingData.from(result))
    }

    private fun arrangeGetUserOption(
        result: Result<UserOptionEntity, NetworkError>,
    ) {
        coEvery {
            getUserOptionUseCase()
        } returns flowOf(result)
    }

    private fun arrangeInitialData(isMobile: Boolean) {
        val queryParams = GetUserQueryParams(role = listOf(2).toJsonString())
        val users = listOf(UserEntity("1"))
        if (isMobile) {
            arrangeGetUserPagingUseCase(queryParams, users)
        } else {
            arrangeGetUserList(queryParams, Result.Success(users))
        }

        arrangeGetUserOption(Result.Success(UserOptionEntity()))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = StudentCenterViewModel(
            getUserListUseCase = getUserListUseCase,
            getUserPagingUseCase = getUserPagingUseCase,
            getUserOptionUseCase = getUserOptionUseCase,
            fileWriter = fileWriter
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `init() with null user should do nothing`() = runTest {
        // Arrange

        // Act
        viewModel.init(null, false)

        // Assert
        val state = viewModel.uiState.value
        assertEquals(state.user, LoginEntity())

        coVerify(exactly = 0) {
            getUserListUseCase(any())
            getUserOptionUseCase()
        }
    }

    @Test
    fun `init() should return Result Error on getting list and filter option`() = runTest {
        //Arrange
        val queryParams = GetUserQueryParams(role = listOf(2).toJsonString())
        val user = LoginEntity()

        arrangeGetUserList(queryParams, Result.Error(NetworkError.RESPONSE_ERROR))
        arrangeGetUserOption(Result.Error(NetworkError.RESPONSE_ERROR))

        //ACT
        viewModel.init(user, false)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(emptyList<UserEntity>(), state.itemList)
        assertEquals(emptyList<OptionData<Int>>(), state.classOptions)
    }

    @Test
    fun `init() should load list and filter when not mobile`() = runTest {
        // Arrange
        val user = LoginEntity(id = "1")
        val users = listOf(UserEntity(id = "1"), UserEntity(id = "2"))
        val queryParams = GetUserQueryParams(role = listOf(2).toJsonString())
        val optionEntity = UserOptionEntity(
            roleOptions = listOf(OptionData("role", 2)),
            classOptions = listOf(OptionData("class", 1)),
            roleIdOptions = listOf(OptionData("roleId", "roleId")),
            classIdOptions = listOf(OptionData("classId", "classId")),
        )

        val expectedClassOptions = listOf(OptionData("class", 1))

        arrangeGetUserList(queryParams, Result.Success(users))
        arrangeGetUserOption(Result.Success(optionEntity))

        // Act
        viewModel.init(user, false)

        // Assert
        val state = viewModel.uiState.value
        assertEquals(user, state.user)
        assertEquals(users, state.itemList)
        assertEquals(expectedClassOptions, state.classOptions)
        assertFalse(state.isLoading)
        assertFalse(state.isLoadingFilter)

        coVerify(exactly = 1) {
            getUserListUseCase(queryParams)
            getUserOptionUseCase()
        }
    }

    @Test
    fun `init() should load list and filter when on mobile`() = runTest {
        // Arrange
        val user = LoginEntity(id = "1")
        val users = listOf(UserEntity(id = "1"), UserEntity(id = "2"))
        val queryParams = GetUserQueryParams(role = listOf(2).toJsonString())
        val optionEntity = UserOptionEntity(
            roleOptions = listOf(OptionData("role", 2)),
            classOptions = listOf(OptionData("class", 1)),
            roleIdOptions = listOf(OptionData("roleId", "roleId")),
            classIdOptions = listOf(OptionData("classId", "classId")),
        )

        val expectedClassOptions = listOf(OptionData("class", 1))

        arrangeGetUserPagingUseCase(queryParams, users)
        arrangeGetUserOption(Result.Success(optionEntity))

        // Act
        viewModel.init(user, true)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(expectedClassOptions, state.classOptions)
        assertFalse(state.isLoading)
        assertFalse(state.isLoadingFilter)

        coVerify(exactly = 1) {
            getUserPagingUseCase(queryParams)
            getUserOptionUseCase()
        }
    }


    @Test
    fun `search() should update search query and refresh paging list on mobile`() = runTest {
        // Arrange
        val user = LoginEntity(id = "userID")
        val users = listOf(UserEntity(id = "1"))
        val cb = viewModel.getCallback()

        arrangeInitialData(true)
        arrangeGetUserPagingUseCase(
            GetUserQueryParams(
                search = "john",
                role = listOf(2).toJsonString()
            ), users
        )

        viewModel.init(user, true)

        // Act
        cb.onSearch("john")
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals("john", state.searchQuery)

        coVerify(exactly = 1) {
            getUserPagingUseCase(GetUserQueryParams(role = listOf(2).toJsonString()))
            getUserOptionUseCase()
            getUserPagingUseCase(
                GetUserQueryParams(
                    search = "john",
                    role = listOf(2).toJsonString()
                )
            )
        }
    }


    @Test
    fun `search() should update search query and refresh list on non-mobile`() = runTest {
        // Arrange
        val user = LoginEntity(id = "userID")
        val users = listOf(UserEntity(id = "1"))
        val cb = viewModel.getCallback()
        val queryparams = GetUserQueryParams(role = listOf(2).toJsonString())

        arrangeInitialData(false)
        arrangeGetUserList(queryparams.copy(search = "john"), Result.Success(users))
        viewModel.init(user, false)

        // Act
        cb.onSearch("john")
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals("john", state.searchQuery)

        coVerify(exactly = 1) {
            getUserListUseCase(GetUserQueryParams(role = listOf(2).toJsonString()))
            getUserOptionUseCase()
            getUserListUseCase(GetUserQueryParams(search = "john", role = listOf(2).toJsonString()))
        }
    }

    @Test
    fun `filter() should update filter data and refresh paging data on mobile`() = runTest {
        // Arrange
        val filter = StudentCenterFilterData(classX = listOf(1))
        val users = listOf(UserEntity(id = "99"))
        val queryParams = GetUserQueryParams(
            classX = listOf(1).toJsonString(),
            role = listOf(2).toJsonString()
        )
        val cb = viewModel.getCallback()

        arrangeInitialData(true)
        arrangeGetUserPagingUseCase(queryParams, users)
        viewModel.init(LoginEntity("1"), true)

        // Act
        cb.onFilter(filter)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(filter, state.filterData)

        coVerify(exactly = 1) {
            getUserPagingUseCase(queryParams)
        }
    }

    @Test
    fun `filter() should update filter data and refresh paging data on non-mobile`() = runTest {
        // Arrange
        val filter = StudentCenterFilterData(classX = listOf(1))
        val users = listOf(UserEntity(id = "99"))
        val queryParams = GetUserQueryParams(
            role = listOf(2).toJsonString(),
            classX = listOf(1).toJsonString(),
        )
        val cb = viewModel.getCallback()

        arrangeInitialData(false)
        arrangeGetUserList(queryParams, Result.Success(users))
        // Act
        cb.onFilter(filter)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(filter, state.filterData)
        assertEquals(users, state.itemList)

        coVerify(exactly = 1) {
            getUserListUseCase(queryParams)
        }
    }

    @Test
    fun `exportFile() success should write excel and update state`() = runTest {
        // Arrange
        val filename = "students.xlsx"
        val users = listOf(UserEntity(id = "1"))

        coEvery { getUserListUseCase(any()) } returns flowOf(Result.Success(users))
        coEvery { fileWriter.writeExcel(any(), any()) } returns Unit

        val cb = viewModel.getCallback()

        // Act
        cb.onExportFile(filename)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.exportState == true)
        assertFalse(state.isLoadingOverlay)

        coVerify {
            fileWriter.writeExcel(filename, any())
        }
    }

    @Test
    fun `exportFile() error should set exportState false`() = runTest {
        // Arrange
        coEvery { getUserListUseCase(any()) } returns flowOf(
            Result.Error(NetworkError.RESPONSE_ERROR)
        )

        val cb = viewModel.getCallback()

        // Act
        cb.onExportFile("file.xlsx")
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.exportState!!)
        assertFalse(state.isLoadingOverlay)
    }

    @Test
    fun `resetMessageState() should reset exportState to null`() = runTest {
        // Arrange
        val cb = viewModel.getCallback()

        // Act
        cb.onResetMessageState()

        // Assert
        val state = viewModel.uiState.value
        assertNull(state.exportState)
    }

    @Test
    fun `paging flow should update pagingData`() = runTest {
        // Arrange
        val pagingData = PagingData.from(listOf(UserEntity(id = "1")))
        coEvery { getUserPagingUseCase(any()) } returns flowOf(pagingData)
        coEvery { getUserOptionUseCase() } returns flowOf(Result.Success(UserOptionEntity()))

        // Act
        viewModel.init(LoginEntity(id = "1"), true)
        advanceUntilIdle()

        // Assert
        assertNotNull(viewModel.pagingData.value)
    }


}
