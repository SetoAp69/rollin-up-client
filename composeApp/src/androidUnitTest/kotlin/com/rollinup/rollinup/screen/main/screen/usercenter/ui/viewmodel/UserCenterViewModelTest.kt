@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("UnusedFlow")

package com.rollinup.rollinup.screen.main.screen.usercenter.ui.viewmodel

import com.rollinup.apiservice.data.source.network.model.request.user.DeleteUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.domain.user.DeleteUserUseCase
import com.rollinup.apiservice.domain.user.GetUserListUseCase
import com.rollinup.apiservice.domain.user.GetUserOptionsUseCase
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.model.user.UserOptionEntity
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterCallback
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterFilterData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserCenterViewModelTest {
    private lateinit var viewModel: UserCenterViewmodel
    private lateinit var cb: UserCenterCallback

    @MockK
    private val getUserListUseCase: GetUserListUseCase = mockk()

    @MockK
    private val getUserOptionUseCase: GetUserOptionsUseCase = mockk()

    @MockK
    private val deleteUserUseCase: DeleteUserUseCase = mockk()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private fun arrangeGetUserList(
        queryParams: GetUserQueryParams,
        result: Result<List<UserEntity>, NetworkError>,
    ) {
        coEvery {
            getUserListUseCase(queryParams)
        } returns flowOf(result)
    }

    private fun arrangeGetUserOptions(
        result: Result<UserOptionEntity, NetworkError>,
    ) {
        coEvery {
            getUserOptionUseCase()
        } returns flowOf(result)
    }

    private fun arrangeDeleteUser(
        body: DeleteUserBody,
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            deleteUserUseCase(body)
        } returns flowOf(result)
    }

    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewModel = UserCenterViewmodel(
            getUserListUseCase = getUserListUseCase,
            getUserOptionsUseCase = getUserOptionUseCase,
            deleteUserUseCase = deleteUserUseCase
        )
        cb = viewModel.getCallback()
    }

    @After
    fun teardown() {
        unmockkAll()
    }


    @Test
    fun `init() should return Result Success for both get user and get options`() = runTest {
        //Arrange
        val queryParams = GetUserQueryParams()
        val expectedUser = listOf(
            UserEntity(id = "1"),
            UserEntity(id = "2")
        )

        val expectedOptions = UserOptionEntity(
            roleOptions = listOf(OptionData("admin", 1)),
            classOptions = listOf(OptionData("class", 1)),
            roleIdOptions = listOf(OptionData("role", "roleId")),
            classIdOptions = listOf(OptionData("class", "classId"))
        )

        arrangeGetUserList(queryParams, Result.Success(expectedUser))
        arrangeGetUserOptions(Result.Success(expectedOptions))

        //Act
        viewModel.init()
        advanceUntilIdle()

        //Assert
        coVerify(exactly = 1) {
            getUserListUseCase(queryParams)
            getUserOptionUseCase()
        }

        val state = viewModel.uiState.value
        assertEquals(expectedUser, state.itemList)
        assertEquals(expectedOptions.roleOptions, state.filterOptions.roleOptions)
        assertEquals(expectedOptions.classOptions, state.filterOptions.classOptions)
        assertFalse(state.isLoadingFilter)
        assertFalse(state.isLoadingList)
    }

    @Test
    fun `init() should return Result Error for both get user and options`() = runTest {
        //Arrange
        val queryParams = GetUserQueryParams()

        arrangeGetUserList(queryParams, Result.Error(NetworkError.RESPONSE_ERROR))
//        arrangeGetUserOptions(Result.Error(NetworkError.RESPONSE_ERROR))
        coEvery { getUserOptionUseCase() } returns flowOf(Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        viewModel.init()

        //Assert
        coVerify(exactly = 1) {
            getUserListUseCase(queryParams)
            getUserOptionUseCase()
        }

        val state = viewModel.uiState.value
        assertTrue(state.itemList.isEmpty())
        assertEquals(emptyList<OptionData<Int>>(), state.filterOptions.roleOptions)
        assertEquals(emptyList<OptionData<Int>>(), state.filterOptions.classOptions)
        assertFalse(state.isLoadingFilter)
        assertFalse(state.isLoadingList)
    }

    @Test
    fun `search() should update search query and get list based on search query`() = runTest {
        //Arrange
        val expectedUser = listOf(
            UserEntity(id = "1"),
            UserEntity(id = "2")
        )
        val searchQuery = "search"
        val queryParams = GetUserQueryParams(search = searchQuery)

        arrangeGetUserList(queryParams, Result.Success(expectedUser))

        //Act
        cb.onSearch(searchQuery)

        //Assert
        coVerify(exactly = 1) {
            getUserListUseCase(queryParams)
        }

        val state = viewModel.uiState.value
        assertEquals(searchQuery, state.searchQuery)
        assertEquals(expectedUser, state.itemList)
        assertFalse(state.isLoadingList)
    }

    @Test
    fun `filter() should update filter data and get list based on filter data`() = runTest {
        //Arrange
        val expectedUser = listOf(
            UserEntity(id = "1"),
            UserEntity(id = "2")
        )
        val filterData = UserCenterFilterData(
            classKey = listOf(2),
            role = listOf(1),
            gender = listOf("M")
        )
        val queryParams = GetUserQueryParams(
            classX = listOf(2).toJsonString(),
            role = listOf(1).toJsonString(),
            gender = listOf("M").toJsonString()
        )

        arrangeGetUserList(queryParams, Result.Success(expectedUser))

        //Act
        cb.onFilter(filterData)

        //Assert
        coVerify(exactly = 1) {
            getUserListUseCase(queryParams)
        }

        val state = viewModel.uiState.value
        assertEquals(filterData, state.filterData)
        assertEquals(queryParams, state.queryParams)
        assertEquals(expectedUser, state.itemList)
        assertFalse(state.isLoadingList)
    }

    @Test
    fun `updateSelection() should add unselected data to itemSelected List`() {
        //Arrange
        val expected = listOf(
            UserEntity(id = "2"), UserEntity("3"), UserEntity("67")
        )

        val userEntity = UserEntity(id = "2")
        val userEntity1 = UserEntity(id = "3")
        val userEntity2 = UserEntity(id = "67")

        //Act
        cb.onUpdateSelection(userEntity)
        cb.onUpdateSelection(userEntity1)
        cb.onUpdateSelection(userEntity2)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expected, state.itemSelected)
    }

    @Test
    fun `updateSelection() should remove data from itemSelected list`() {
        //Arrange
        val expected = listOf(
            UserEntity(id = "2"), UserEntity("3")
        )

        val userEntity = UserEntity(id = "2")
        val userEntity1 = UserEntity(id = "3")
        val userEntity2 = UserEntity(id = "67")

        //Act
        cb.onUpdateSelection(userEntity)
        cb.onUpdateSelection(userEntity1)
        cb.onUpdateSelection(userEntity2)
        cb.onUpdateSelection(userEntity2)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(expected, state.itemSelected)
    }

    @Test
    fun `resetMessageState() should reset all message state to null`() = runTest {
        //Arrange
        val listId = listOf("1", "2", "3")
        val body = DeleteUserBody(listId)

        arrangeDeleteUser(body, Result.Error(NetworkError.RESPONSE_ERROR))

        cb.onDeleteUser(listId)

        //Act
        cb.onResetMessageState()

        //Assert
        coVerify(exactly = 1) {
            deleteUserUseCase(body)
        }

        val state = viewModel.uiState.value
        assertNull(state.deleteUserState)
    }

    @Test
    fun `deleteUser() should return Result Success`() = runTest {
        //Arrange
        val listId = listOf("1", "2", "3")
        val body = DeleteUserBody(listId)

        arrangeDeleteUser(body, Result.Success(Unit))

        //Act
        cb.onDeleteUser(listId)

        //Assert
        coVerify(exactly = 1) {
            deleteUserUseCase(body)
        }

        val state = viewModel.uiState.value
        assertTrue(state.deleteUserState!!)
    }

    @Test
    fun `deleteUser() should return Result Error`() = runTest {
        //Arrange
        val listId = listOf("1", "2", "3")
        val body = DeleteUserBody(listId)

        arrangeDeleteUser(body, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        cb.onDeleteUser(listId)

        //Assert
        coVerify(exactly = 1) {
            deleteUserUseCase(body)
        }

        val state = viewModel.uiState.value
        assertFalse(state.deleteUserState!!)
    }

    @Test
    fun `selectAll() should select all item if not all items is selected`() {
        //Arrange
        val userList = listOf(
            UserEntity("1"),
            UserEntity("2"),
            UserEntity("3"),
            UserEntity("4"),
        )
        val queryParams = GetUserQueryParams()
        arrangeGetUserList(queryParams, Result.Success(userList))
        arrangeGetUserOptions(Result.Success(UserOptionEntity()))

        viewModel.init()

        //Act
        cb.onSelectAll()

        //Assert
        val state = viewModel.uiState.value
        assertEquals(userList, state.itemSelected)
    }


    @Test
    fun `selectAll() should set itemselected to empty if all items is selected`() {
        //Arrange
        val userList = listOf(
            UserEntity("1"),
            UserEntity("2"),
            UserEntity("3"),
            UserEntity("4"),
        )
        val queryParams = GetUserQueryParams()
        arrangeGetUserList(queryParams, Result.Success(userList))
        arrangeGetUserOptions(Result.Success(UserOptionEntity()))


        viewModel.init()

        //Act
        cb.onSelectAll()
        cb.onSelectAll()

        //Assert
        val state = viewModel.uiState.value
        assertEquals(emptyList<UserEntity>(), state.itemSelected)
    }
}