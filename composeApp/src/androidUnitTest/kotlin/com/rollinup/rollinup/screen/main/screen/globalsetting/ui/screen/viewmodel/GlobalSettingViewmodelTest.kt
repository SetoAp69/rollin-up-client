@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("UnusedFlow")

package com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.viewmodel

import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.domain.globalsetting.EditGlobalSettingUseCase
import com.rollinup.apiservice.domain.globalsetting.GetGlobalSettingUseCase
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.CoroutineTestRule
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.GlobalSettingFormData
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
import java.time.LocalTime

class GlobalSettingViewmodelTest {

    private lateinit var viewModel: GlobalSettingViewmodel

    @MockK
    private lateinit var getGlobalSettingUseCase: GetGlobalSettingUseCase

    @MockK
    private lateinit var editGlobalSettingUseCase: EditGlobalSettingUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule() // UnconfinedTestDispatcher

    // ---------- Arrange Helpers ----------

    private fun arrangeGetGlobalSetting(
        result: Result<GlobalSetting, NetworkError>,
    ) {
        coEvery {
            getGlobalSettingUseCase()
        } returns flowOf(result)
    }

    private fun arrangeEditGlobalSetting(
        body: EditGlobalSettingBody,
        result: Result<Unit, NetworkError>,
    ) {
        coEvery {
            editGlobalSettingUseCase(body)
        } returns flowOf(result)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = GlobalSettingViewmodel(
            getGlobalSettingUseCase = getGlobalSettingUseCase,
            editGlobalSettingUseCase = editGlobalSettingUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ---------- Tests ----------

    @Test
    fun `init() should load global setting successfully`() = runTest {
        // Arrange
        val entity = GlobalSetting(
            _checkInPeriodStart = LocalTime.of(7, 0).toString(),
            _checkInPeriodEnd = LocalTime.of(9, 0).toString(),
            _schoolPeriodStart = LocalTime.of(7, 30).toString(),
            _schoolPeriodEnd = LocalTime.of(15, 0).toString(),
            longitude = 110.0,
            latitude = -7.0,
            radius = 100.00
        )

        arrangeGetGlobalSetting(Result.Success(entity))

        // Act
        viewModel.init()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.formData)
        assertEquals(
            entity.checkInPeriodStart.toSecondOfDay().toLong(),
            state.formData.attendanceStart
        )

        coVerify(exactly = 1) {
            getGlobalSettingUseCase()
        }
    }

    @Test
    fun `init() error should stop loading`() = runTest {
        // Arrange
        arrangeGetGlobalSetting(Result.Error(NetworkError.RESPONSE_ERROR))

        // Act
        viewModel.init()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
    }

    @Test
    fun `updateForm() should update formData`() = runTest {
        // Arrange
        val formData = GlobalSettingFormData(
            attendanceStart = 1,
            attendanceEnd = 2,
            schoolStart = 3,
            schoolEnd = 4,
            long = 110.0,
            lat = -7.0,
            rad = 100.00
        )
        val cb = viewModel.getCallback()

        // Act
        cb.onUpdateForm(formData)

        // Assert
        assertEquals(formData, viewModel.uiState.value.formData)
    }

    @Test
    fun `submitForm() valid form success should update submitState true`() = runTest {
        // Arrange
        val formData = GlobalSettingFormData(
            attendanceStart = 1,
            attendanceEnd = 2,
            schoolStart = 3,
            schoolEnd = 4,
            long = 110.0,
            lat = -7.0,
            rad = 100.00
        )

        val body = EditGlobalSettingBody(
            checkInPeriodStart = 1,
            checkInPeriodEnd = 2,
            schoolPeriodStart = 3,
            schoolPeriodEnd = 4,
            longitude = 110.0,
            latitude = -7.0,
            radius = 100.00
        )

        arrangeEditGlobalSetting(body, Result.Success(Unit))
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmit(formData)
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value.submitState == true)

        coVerify(exactly = 1) {
            editGlobalSettingUseCase(body)
        }
    }

    @Test
    fun `submitForm() invalid attendance period should set error and not submit`() = runTest {
        // Arrange
        val formData = GlobalSettingFormData(
            attendanceStart = 10,
            attendanceEnd = 5,
            schoolStart = 1,
            schoolEnd = 2
        )
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmit(formData)

        // Assert
        val state = viewModel.uiState.value
        assertNotNull(state.formData.attendanceStartError)
        assertNotNull(state.formData.attendanceEndError)

        coVerify(exactly = 0) {
            editGlobalSettingUseCase(any())
        }
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
    fun `refresh should reload global setting`() = runTest {
        // Arrange
        arrangeGetGlobalSetting(Result.Error(NetworkError.RESPONSE_ERROR))
        val cb = viewModel.getCallback()

        // Act
        cb.onRefresh()
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) {
            getGlobalSettingUseCase()
        }
    }

    @Test
    fun `validateForm school period invalid should set school errors`() = runTest {
        // Arrange
        val formData = GlobalSettingFormData(
            attendanceStart = 1,
            attendanceEnd = 2,
            schoolStart = 10,
            schoolEnd = 5
        )
        val cb = viewModel.getCallback()

        // Act
        cb.onSubmit(formData)

        // Assert
        val state = viewModel.uiState.value
        assertNotNull(state.formData.schoolStartError)
        assertNotNull(state.formData.schoolEndError)
    }
}
