@file:Suppress("UNCHECKED_CAST")

package com.rollinup.rollinup.component.permitform.viewmodel

import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.domain.permit.CreatePermitUseCase
import com.rollinup.apiservice.domain.permit.EditPermitUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByIdUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.component.permitform.model.PermitFormData
import com.rollinup.rollinup.component.permitform.uistate.PermitFormUiState
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue


@Suppress("UnusedFlow")
@ExperimentalCoroutinesApi
class PermitFormViewModelTest() {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var viewModel: PermitFormViewModel

    @MockK
    private val getPermitByIdUseCase: GetPermitByIdUseCase = mockk()

    @MockK
    private val editPermitUseCase: EditPermitUseCase = mockk()

    @MockK
    private val createPermitUseCase: CreatePermitUseCase = mockk()

    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewModel = PermitFormViewModel(
            getPermitByIdUseCase = getPermitByIdUseCase,
            editPermitUseCase = editPermitUseCase,
            createPermitUseCase = createPermitUseCase
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    private fun arrangeGetPermitById(
        id: String,
        result: Result<PermitDetailEntity, NetworkError>,
    ) {
        coEvery {
            getPermitByIdUseCase(id)
        } returns flowOf(result)
    }

    private fun arrangeEditPermit(
        body: CreateEditPermitBody,
        id: String,
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            editPermitUseCase(id, body)
        } returns flowOf(result)
    }

    private fun arrangeCreatePermit(
        body: CreateEditPermitBody,
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            createPermitUseCase(body)
        } returns flowOf(result)
    }

    @Test
    fun `init() should do nothing when id is null`() {
        //Arrange
        val id: String? = null
        val user: LoginEntity? = LoginEntity()

        //Act
        viewModel.init(id, user)

        //Assert
        coVerify(exactly = 0) {
            getPermitByIdUseCase(any())
        }
    }

    @Test
    fun `init() should return Result Success`() = runTest {
        //Arrange
        val id = "123"
        val user = LoginEntity("123")

        val mockkDetail = PermitDetailEntity(
            id = "id",
            date = "2003-08-06",
            name = "NN",
            type = PermitType.DISPENSATION,
            student = PermitDetailEntity.User(
                id = "wf",
                name = "nn",
                studentId = "123"
            ),
            startTime = "2025-10-28T09:00:00+07:00",
            endTime = "2025-10-28T09:00:00+09:00",
            attachment = "attachment",
            note = "note",
            reason = "reason",
            createdAt = "123",
            updatedAt = "123",
            approvalStatus = ApprovalStatus.APPROVAL_PENDING,
            approvalNote = "approval note",
            approvedBy = PermitDetailEntity.User(
                id = "12",
                name = "st",
                username = "st",
                studentId = "st",
                xClass = "1"
            ),
            approvedAt = "213"
        )
        val expectedFormData = PermitFormData(
            duration = listOf(
                "2025-10-28T09:00:00+07:00".parseToLocalDateTime().toEpochMillis(),
                "2025-10-28T09:00:00+09:00".parseToLocalDateTime().toEpochMillis()
            ),
            reason = "reason",
            type = PermitType.DISPENSATION,
            note = "note"
        )

        arrangeGetPermitById(id, Result.Success(mockkDetail))

        //Act
        viewModel.init(id, user)

        //Assert
        coVerify(exactly = 1) {
            getPermitByIdUseCase(id)
        }

        val state = viewModel.uiState.value
        assertEquals(expectedFormData, state.formData)
    }

    @Test
    fun `init() should return Result Error`() = runTest {
        //Arrange
        val id = "123"
        val user = LoginEntity("123")

        val expectedFormData = PermitFormData()

        arrangeGetPermitById(id, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        viewModel.init(id, user)

        //Assert
        coVerify(exactly = 1) {
            getPermitByIdUseCase(id)
        }

        val state = viewModel.uiState.value
        assertEquals(expectedFormData, state.formData)
        assertFalse(state.isLoadingOverlay)
    }

    @Test
    fun `reset() should reset uiState`() = runTest {
        //Arrange
        val id = "123"
        val user = LoginEntity("123")

        val mockkDetail = PermitDetailEntity(
            id = "id",
            date = "2003-08-06",
            name = "NN",
            type = PermitType.DISPENSATION,
            student = PermitDetailEntity.User(
                id = "wf",
                name = "nn",
                studentId = "123"
            ),
            startTime = "2025-10-28T09:00:00+07:00",
            endTime = "2025-10-28T09:00:00+09:00",
            attachment = "attachment",
            note = "note",
            reason = "reason",
            createdAt = "123",
            updatedAt = "123",
            approvalStatus = ApprovalStatus.APPROVAL_PENDING,
            approvalNote = "approval note",
            approvedBy = PermitDetailEntity.User(
                id = "12",
                name = "st",
                username = "st",
                studentId = "st",
                xClass = "1"
            ),
            approvedAt = "213"
        )

        arrangeGetPermitById(id, Result.Success(mockkDetail))
        viewModel.init(id, user)

        //Act
        viewModel.reset()

        //Assert
        coVerify(exactly = 1) {
            getPermitByIdUseCase(id)
        }

        val state = viewModel.uiState.value
        assertEquals(PermitFormUiState(), state)
    }

    @Test
    fun `validateForMData should return true when all data is valid`() {
        //Arrange
        val file: MultiPlatformFile = mockk(relaxed = true)
        val formData = PermitFormData(
            duration = listOf(10L, 11L),
            reason = "Not Sick",
            isSick = false,
            type = PermitType.ABSENCE,
            attachment = file,
            note = "note",
        )
        val cb = viewModel.getCallback()

        //Act
        val isValid = cb.onValidateForm(formData)

        //Assert
        assertTrue(isValid)
    }

    @Test
    fun `validateForMData should return false when data is not valid`() {
        //Arrange
        val file: MultiPlatformFile = mockk(relaxed = true)
        val formData = PermitFormData(reason = "")
        val cb = viewModel.getCallback()

        //Act
        val isValid = cb.onValidateForm(formData)

        //Assert
        assertFalse(isValid)
    }

    @Test
    fun `validateFormData should return true when data is valid for edit`() = runTest {
        val formData = PermitFormData()
        val id = "123"
        val mockkDetail = PermitDetailEntity(
            id = "id",
            date = "2003-08-06",
            name = "NN",
            type = PermitType.DISPENSATION,
            student = PermitDetailEntity.User(
                id = "wf",
                name = "nn",
                studentId = "123"
            ),
            startTime = "2025-10-28T09:00:00+07:00",
            endTime = "2025-10-28T09:00:00+09:00",
            attachment = "attachment",
            note = "note",
            reason = "reason",
            createdAt = "123",
            updatedAt = "123",
            approvalStatus = ApprovalStatus.APPROVAL_PENDING,
            approvalNote = "approval note",
            approvedBy = PermitDetailEntity.User(
                id = "12",
                name = "st",
                username = "st",
                studentId = "st",
                xClass = "1"
            ),
            approvedAt = "213"
        )
        arrangeGetPermitById(id, Result.Success(mockkDetail))
        viewModel.init(id, LoginEntity())

        //Act
        val cb = viewModel.getCallback()
        val isValid = cb.onValidateForm(formData)

        //Assert
        assertTrue(isValid)
    }

    @Test
    fun `submit() create should return Result Success`() = runTest {
        //Arrange
        val file: MultiPlatformFile = mockk(relaxed = true)
        val formData = PermitFormData(
            duration = listOf(10L, 11L),
            reason = "Not Sick",
            isSick = false,
            type = PermitType.ABSENCE,
            attachment = file,
            note = "note",
        )
        val body = with(formData) {
            CreateEditPermitBody(
                studentId = "",
                reason = reason,
                duration = duration.filter { it != null }.ifEmpty { null } as List<Long>?,
                type = type,
                note = note,
                attachment = attachment,
            )
        }
        arrangeCreatePermit(body, Result.Success(Unit))

        //Act
        val cb = viewModel.getCallback()
        var isSuccess: Boolean? = null
        cb.onSubmit(formData, { isSuccess = true }, { isSuccess = false })


        //Assert
        coVerify {
            createPermitUseCase(body)
        }

        val state = viewModel.uiState.value
        assertFalse(state.isLoadingOverlay)
        assertTrue(isSuccess == true)
    }

    @Test
    fun `submit() create should return Result Error`() = runTest {
        //Arrange
        val file: MultiPlatformFile = mockk(relaxed = true)
        val formData = PermitFormData(
            duration = listOf(10L, 11L),
            reason = "Not Sick",
            isSick = false,
            type = PermitType.ABSENCE,
            attachment = file,
            note = "note",
        )
        val body = with(formData) {
            CreateEditPermitBody(
                studentId = "",
                reason = reason,
                duration = duration.filter { it != null }.ifEmpty { null } as List<Long>?,
                type = type,
                note = note,
                attachment = attachment,
            )
        }
        arrangeCreatePermit(body, Result.Error(NetworkError.RESPONSE_ERROR))

        //Act
        val cb = viewModel.getCallback()
        var isSuccess: Boolean? = null
        cb.onSubmit(formData, { isSuccess = true }, { isSuccess = false })


        //Assert
        coVerify {
            createPermitUseCase(body)
        }

        val state = viewModel.uiState.value
        assertFalse(state.isLoadingOverlay)
        assertTrue(isSuccess == false)
    }


    @Test
    fun `submit() should return Result Success on edit`() = runTest {
        //Arrange
        val id = "123"
        val user = LoginEntity("studentId")

        val mockkDetail = PermitDetailEntity(
            id = "123",
            date = "2003-08-06",
            name = "NN",
            type = PermitType.DISPENSATION,
            student = PermitDetailEntity.User(
                id = "123",
                name = "nn",
                studentId = "123"
            ),
            startTime = "2025-10-28T09:00:00+07:00",
            endTime = "2025-10-28T09:00:00+09:00",
            attachment = "attachment",
            note = "note",
            reason = "reason",
            createdAt = "123",
            updatedAt = "123",
            approvalStatus = ApprovalStatus.APPROVAL_PENDING,
            approvalNote = "approval note",
            approvedBy = PermitDetailEntity.User(
                id = "12",
                name = "st",
                username = "st",
                studentId = "st",
                xClass = "1"
            ),
            approvedAt = "213"
        )
        val formData = PermitFormData(

            duration = listOf(
                "2025-10-28T09:00:00+07:00".parseToLocalDateTime().toEpochMillis(),
                "2025-10-28T09:00:00+09:00".parseToLocalDateTime().toEpochMillis()
            ),
            reason = "reason",
            type = PermitType.DISPENSATION,
            note = "note"
        )
        val body = with(formData) {
            CreateEditPermitBody(
                studentId = null,
                reason = reason,
                duration = duration.filter { it != null }.ifEmpty { null } as List<Long>?,
                type = type,
                note = note,
                attachment = attachment,
            )
        }

        arrangeGetPermitById(id, Result.Success(mockkDetail))
        arrangeEditPermit(body, id, Result.Success(Unit))

        viewModel.init(id, user)

        //Act
        val cb = viewModel.getCallback()
        var isSuccess: Boolean? = null
        cb.onSubmit(formData, { isSuccess = true }, { isSuccess = false })
        advanceUntilIdle()
        //Assert
        coVerify(exactly = 1) {
            getPermitByIdUseCase(id)
            editPermitUseCase(id, body)
        }

        val state = viewModel.uiState.value
        assertEquals(formData, state.formData)
        assertEquals(true, isSuccess)
    }

    @Test
    fun `submit() should return Result Error on edit`() = runTest {
        //Arrange
        val id = "123"
        val user = LoginEntity("studentId")

        val mockkDetail = PermitDetailEntity(
            id = "id",
            date = "2003-08-06",
            name = "NN",
            type = PermitType.DISPENSATION,
            student = PermitDetailEntity.User(
                id = "wf",
                name = "nn",
                studentId = "123"
            ),
            startTime = "2025-10-28T09:00:00+07:00",
            endTime = "2025-10-28T09:00:00+09:00",
            attachment = "attachment",
            note = "note",
            reason = "reason",
            createdAt = "123",
            updatedAt = "123",
            approvalStatus = ApprovalStatus.APPROVAL_PENDING,
            approvalNote = "approval note",
            approvedBy = PermitDetailEntity.User(
                id = "12",
                name = "st",
                username = "st",
                studentId = "st",
                xClass = "1"
            ),
            approvedAt = "213"
        )
        val formData = PermitFormData(
            duration = listOf(
                "2025-10-28T09:00:00+07:00".parseToLocalDateTime().toEpochMillis(),
                "2025-10-28T09:00:00+09:00".parseToLocalDateTime().toEpochMillis()
            ),
            reason = "reason",
            type = PermitType.DISPENSATION,
            note = "note"
        )
        val body = with(formData) {
            CreateEditPermitBody(
                studentId = null,
                reason = reason,
                duration = duration.filter { it != null }.ifEmpty { null } as List<Long>?,
                type = type,
                note = note,
                attachment = attachment,
            )
        }

        arrangeGetPermitById(id, Result.Success(mockkDetail))
        arrangeEditPermit(body, id, Result.Error(NetworkError.RESPONSE_ERROR))

        viewModel.init(id, user)

        //Act
        val cb = viewModel.getCallback()
        var isSuccess: Boolean? = null
        cb.onSubmit(formData, { isSuccess = true }, { isSuccess = false })

        //Assert
        coVerify(exactly = 1) {
            getPermitByIdUseCase(id)
            editPermitUseCase
        }

        val state = viewModel.uiState.value
        assertEquals(formData, state.formData)
        assertEquals(false, isSuccess)
    }

    @Test
    fun `submit() should do nothing when form is invalid`() = runTest {
        //Arrange
        val formData = PermitFormData()

        //Act
        val cb = viewModel.getCallback()
        var isSuccess: Boolean? = null
        cb.onSubmit(formData, { isSuccess = true }, { isSuccess = false })

        //sAsert
        coVerify(exactly = 0) {
            createPermitUseCase(any())
        }
        assertNull(isSuccess)
    }

    @Test
    fun `updateFormData() sshould update form data with given value`() {
        //Arrange
        val formData = PermitFormData(
            note = "Note"
        )

        //ACt
        val cb = viewModel.getCallback()
        cb.onUpdateFormData(formData)

        //Assert
        val state = viewModel.uiState.value
        assertEquals(formData, state.formData)
    }

}
