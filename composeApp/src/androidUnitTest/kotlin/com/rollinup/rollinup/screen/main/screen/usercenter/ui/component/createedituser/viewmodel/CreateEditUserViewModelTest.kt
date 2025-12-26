@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("UnusedFlow")

package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.viewmodel

import com.rollinup.apiservice.data.source.network.model.request.user.CheckEmailOrUsernameQueryParams
import com.rollinup.apiservice.domain.user.CheckEmailOrUsernameUseCase
import com.rollinup.apiservice.domain.user.EditUserUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.domain.user.GetUserOptionsUseCase
import com.rollinup.apiservice.domain.user.RegisterUserUseCase
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.model.user.UserOptionEntity
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormData
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.uistate.CreateEditUserUiState
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

class CreateEditUserViewModelTest {

    private lateinit var viewModel: CreateEditUserViewModel

    @MockK
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase

    @MockK
    private lateinit var getUserOptionUseCase: GetUserOptionsUseCase

    @MockK
    private lateinit var editUserUseCase: EditUserUseCase

    @MockK
    private lateinit var registerUserUseCase: RegisterUserUseCase

    @MockK
    private lateinit var checkEmailOrUsernameUseCase: CheckEmailOrUsernameUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    // ---------- Arrange Helpers ----------

    private fun arrangeGetOptions(result: Result<UserOptionEntity, NetworkError>) {
        coEvery { getUserOptionUseCase() } returns flowOf(result)
    }

    private fun arrangeGetUserById(id: String, result: Result<UserDetailEntity, NetworkError>) {
        coEvery { getUserByIdUseCase(id) } returns flowOf(result)
    }

    private fun arrangeRegisterUser(result: Result<Unit, NetworkError>) {
        coEvery { registerUserUseCase(any()) } returns flowOf(result)
    }

    private fun arrangeEditUser(result: Result<Unit, NetworkError>) {
        coEvery { editUserUseCase(any(), any()) } returns flowOf(result)
    }

    private fun arrangeCheckEmailOrUsername(
        query: CheckEmailOrUsernameQueryParams,
        result: Result<Unit, NetworkError>,
    ) {
        coEvery { checkEmailOrUsernameUseCase(query) } returns flowOf(result)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = CreateEditUserViewModel(
            getUserByIdUseCase,
            getUserOptionUseCase,
            editUserUseCase,
            registerUserUseCase,
            checkEmailOrUsernameUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ---------- Tests ----------

    // 1. Initialization Tests

    @Test
    fun `init(null) should start in Create Mode and fetch options`() = runTest {
        // Arrange
        val options = UserOptionEntity(
            roleIdOptions = listOf(OptionData("Student", "123")),
            classIdOptions = listOf(OptionData("Class A", "123"))
        )
        arrangeGetOptions(Result.Success(options))

        // Act
        viewModel.init(null)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isEdit)
        assertFalse(state.isLoadingOptions)
        assertEquals(options.roleIdOptions, state.formOptions.role)
        assertEquals(options.classIdOptions, state.formOptions.classX)
        coVerify { getUserOptionUseCase() }
        coVerify(exactly = 0) { getUserByIdUseCase(any()) }
    }

    @Test
    fun `init(id) should start in Edit Mode and fetch options and details`() = runTest {
        // Arrange
        val userId = "user123"
        val options = UserOptionEntity()
        val userDetail = UserDetailEntity(
            id = userId,
            firstName = "John",
            lastName = "Doe",
            userName = "jdoe",
            email = "jdoe@example.com",
            role = UserDetailEntity.Data("123", "Student"),
            birthDay = "2000-01-01"
        )

        arrangeGetOptions(Result.Success(options))
        arrangeGetUserById(userId, Result.Success(userDetail))

        // Act
        viewModel.init(userId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state.isEdit)
        assertFalse(state.isLoading)
        assertEquals(userDetail, state.initialDetail)
        assertEquals("John", state.formData.firstName)
        assertEquals("jdoe", state.formData.userName)
        coVerify {
            getUserOptionUseCase()
            getUserByIdUseCase(userId)
        }
    }

    @Test
    fun `init(id) error fetching detail should handle gracefully`() = runTest {
        // Arrange
        val userId = "user123"
        arrangeGetOptions(Result.Success(UserOptionEntity()))
        arrangeGetUserById(userId, Result.Error(NetworkError.RESPONSE_ERROR))

        // Act
        viewModel.init(userId)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        // FormData should remain empty/default
        assertEquals(CreateEditUserFormData(), state.formData)
    }

    @Test
    fun `getOptions() error should update isLoadingOptions`() = runTest {
        // Arrange
        arrangeGetOptions(Result.Error(NetworkError.RESPONSE_ERROR))

        // Act
        viewModel.getOptions()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoadingOptions)
        assertTrue(state.formOptions.role.isEmpty())
    }

    // 2. Form Management Tests

    @Test
    fun `updateForm() should update formData`() {
        // Arrange
        val newData = CreateEditUserFormData(firstName = "Jane")
        val cb = viewModel.getCallback()

        // Act
        cb.onUpdateForm(newData)

        // Assert
        assertEquals(newData, viewModel.uiState.value.formData)
    }

    @Test
    fun `resetForm() should clear formData`() {
        // Arrange
        val cb = viewModel.getCallback()
        cb.onUpdateForm(CreateEditUserFormData(firstName = "Dirty"))

        // Act
        cb.onResetForm()

        // Assert
        assertEquals(CreateEditUserFormData(), viewModel.uiState.value.formData)
    }

    @Test
    fun `toggleStay() should update isStay`() {
        // Arrange
        val cb = viewModel.getCallback()

        // Act
        cb.onToggleStay(true)

        // Assert
        assertTrue(viewModel.uiState.value.isStay)
    }

    @Test
    fun `resetUiState() should reset entire state`() {
        // Arrange
        viewModel.init("123") // Dirties the state

        // Act
        viewModel.resetUiState()

        // Assert
        assertEquals(CreateEditUserUiState(), viewModel.uiState.value)
    }

    @Test
    fun `resetMessageState() should clear submitState`() = runTest {
        // Arrange: Simulate a success state first
        val formData = CreateEditUserFormData(
            userName = "u", firstName = "f", lastName = "l", email = "e",
            role = "1", gender = "M", birthDay = 100L
        )
        arrangeRegisterUser(Result.Success(Unit))
        viewModel.getCallback().onSubmit(formData, false)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.submitState == true)

        // Act
        viewModel.getCallback().onResetMessageState()

        // Assert
        assertNull(viewModel.uiState.value.submitState)
    }

    // 3. Validation Tests (validateForm)

    @Test
    fun `validateForm - Create Mode - Empty Fields should fail`() {
        // Arrange
        val cb = viewModel.getCallback()
        val emptyForm = CreateEditUserFormData() // All nulls/blanks

        // Act
        val result = cb.onValidateForm(emptyForm, false) // isEdit = false

        // Assert
        assertFalse(result)
        val state = viewModel.uiState.value.formData
        assertEquals("Username can't be empty", state.userNameError)
        assertEquals("Last name can't be empty", state.lastNameError)
        assertEquals(
            "First bane can't be empty",
            state.firstNameError
        ) // Typo "bane" matches source code
        assertEquals("Email can't be empty", state.emailError)
        assertTrue(state.genderError == true)
        assertTrue(state.roleError == true)
    }

    @Test
    fun `validateForm - Create Mode - Student Role Validation`() = runTest {
        // Arrange
        // We must load options first so "Student" role is identified
        val studentRoleId = "123"
        val options = UserOptionEntity(
            roleIdOptions = listOf(OptionData("Student", studentRoleId)),
            classIdOptions = listOf(OptionData("1A", "123"))
        )
        arrangeGetOptions(Result.Success(options))
        viewModel.getOptions()
        advanceUntilIdle()

        val formWithStudentRole = CreateEditUserFormData(
            userName = "valid", firstName = "valid", lastName = "valid", email = "valid",
            gender = "M", birthDay = 100L,
            role = studentRoleId,
            // Missing studentId and classId
            studentId = null,
            classId = null
        )
        val cb = viewModel.getCallback()

        // Act
        val result = cb.onValidateForm(formWithStudentRole, false)

        // Assert
        assertFalse(result)
        val state = viewModel.uiState.value.formData
        assertEquals("Student ID can't be empty", state.studentIdError)
        assertTrue(state.classError == true)
    }

    @Test
    fun `validateForm - Create Mode - Valid Form`() = runTest {
        // Arrange
        val cb = viewModel.getCallback()
        val validForm = CreateEditUserFormData(
            userName = "user",
            firstName = "first",
            lastName = "last",
            email = "email@test.com",
            role = "123",
            gender = "123",
            birthDay = 1000L
        )

        // Act
        val result = cb.onValidateForm(validForm, false)

        // Assert
        assertTrue(result)
        val state = viewModel.uiState.value.formData
        assertNull(state.userNameError)
    }

    @Test
    fun `validateForm - Edit Mode - Skips most validations`() {
        // Arrange
        val cb = viewModel.getCallback()
        val emptyForm = CreateEditUserFormData() // Completely empty

        // Act
        // isEdit = true. Logic in VM: if (!isEdit && ...). 
        // So in edit mode, empty checks are skipped.
        val result = cb.onValidateForm(emptyForm, true)

        // Assert
        assertTrue(result) // Should pass based on current implementation
        val state = viewModel.uiState.value.formData
        assertNull(state.userNameError)
        assertNull(state.firstNameError)
    }

    // 4. Submission Tests

    @Test
    fun `submit() - Create User (Register) - Success`() = runTest {
        // Arrange
        val cb = viewModel.getCallback()
        val formData = CreateEditUserFormData(
            userName = "u", firstName = "f", lastName = "l", email = "e",
            role = "123", gender = "123", birthDay = 100L
        )
        arrangeRegisterUser(Result.Success(Unit))

        // Act
        cb.onSubmit(formData, false) // isEdit = false
        advanceUntilIdle()

        // Assert
        assertFalse(viewModel.uiState.value.isLoadingOverlay)
        assertTrue(viewModel.uiState.value.submitState == true)
        coVerify { registerUserUseCase(any()) }
        coVerify(exactly = 0) { editUserUseCase(any(), any()) }
    }

    @Test
    fun `submit() - Edit User - Success`() = runTest {
        // Arrange
        val cb = viewModel.getCallback()
        val formData = CreateEditUserFormData(id = "123", userName = "u")
        arrangeEditUser(Result.Success(Unit))

        // Act
        cb.onSubmit(formData, true) // isEdit = true
        advanceUntilIdle()

        // Assert
        assertFalse(viewModel.uiState.value.isLoadingOverlay)
        assertTrue(viewModel.uiState.value.submitState == true)
        coVerify { editUserUseCase("123", any()) }
        coVerify(exactly = 0) { registerUserUseCase(any()) }
    }

    @Test
    fun `submit() - Validation Failure should abort`() = runTest {
        // Arrange
        val cb = viewModel.getCallback()
        val invalidForm = CreateEditUserFormData() // Empty form in create mode

        // Act
        cb.onSubmit(invalidForm, false)

        // Assert
        coVerify(exactly = 0) {
            registerUserUseCase(any())
            editUserUseCase(any(), any())
        }
    }

    @Test
    fun `submit() - API Error should update submitState`() = runTest {
        // Arrange
        val cb = viewModel.getCallback()
        val formData = CreateEditUserFormData(
            userName = "u", firstName = "f", lastName = "l", email = "e",
            role = "123", gender = "F", birthDay = 100L
        )
        arrangeRegisterUser(Result.Error(NetworkError.RESPONSE_ERROR))

        // Act
        cb.onSubmit(formData, false)
        advanceUntilIdle()

        // Assert
        assertFalse(viewModel.uiState.value.isLoadingOverlay)
        assertTrue(viewModel.uiState.value.submitState == false)
    }

    // 5. Uniqueness Check Tests

    @Test
    fun `checkUserName() - Taken - Should set error`() = runTest {
        // Arrange
        val username = "taken_user"
        val query = CheckEmailOrUsernameQueryParams(username = username)
        // Result.Error means "Already used" based on VM logic interpretation:
        // "val errorMessage = if (result is Result.Error) 'Username is already used' else null"
        arrangeCheckEmailOrUsername(query, Result.Error(NetworkError.RESPONSE_ERROR))

        val cb = viewModel.getCallback()

        // Act
        cb.onCheckUserName(username)
        advanceUntilIdle()

        // Assert
        assertEquals("Username is already used", viewModel.uiState.value.formData.userNameError)
    }

    @Test
    fun `checkUserName() - Available - Should clear error`() = runTest {
        // Arrange
        val username = "new_user"
        val query = CheckEmailOrUsernameQueryParams(username = username)
        arrangeCheckEmailOrUsername(query, Result.Success(Unit))

        val cb = viewModel.getCallback()

        // Act
        cb.onCheckUserName(username)
        advanceUntilIdle()

        // Assert
        assertNull(viewModel.uiState.value.formData.userNameError)
    }

    @Test
    fun `checkEmail() - Taken - Should set error`() = runTest {
        // Arrange
        val email = "taken@mail.com"
        val query = CheckEmailOrUsernameQueryParams(email = email)
        arrangeCheckEmailOrUsername(query, Result.Error(NetworkError.RESPONSE_ERROR))

        val cb = viewModel.getCallback()

        // Act
        cb.onCheckEmail(email)
        advanceUntilIdle()

        // Assert
        assertEquals("Email is already used", viewModel.uiState.value.formData.emailError)
    }

    @Test
    fun `checkEmail() - Available - Should clear error`() = runTest {
        // Arrange
        val email = "new@mail.com"
        val query = CheckEmailOrUsernameQueryParams(email = email)
        arrangeCheckEmailOrUsername(query, Result.Success(Unit))

        val cb = viewModel.getCallback()

        // Act
        cb.onCheckEmail(email)
        advanceUntilIdle()

        // Assert
        assertNull(viewModel.uiState.value.formData.emailError)
    }
}