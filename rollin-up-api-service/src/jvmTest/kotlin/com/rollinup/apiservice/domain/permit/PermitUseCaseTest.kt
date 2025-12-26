package com.rollinup.apiservice.domain.permit

import androidx.paging.PagingData
import com.rollinup.apiservice.data.repository.permit.PermitRepository
import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@Suppress("UnusedFlow")
class PermitUseCaseTest {

    @MockK
    private lateinit var repository: PermitRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `GetPermitByStudentPagingUseCase should call repository`() {
        val useCase = GetPermitByStudentPagingUseCase(repository)
        val id = "s1"
        val params = GetPermitListQueryParams()
        val expected = flowOf(PagingData.empty<PermitByStudentEntity>())
        every { repository.getPermitByStudentPaging(id, params) } returns expected

        val result = useCase(id, params)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.getPermitByStudentPaging(id, params) }
    }

    @Test
    fun `GetPermitByClassPagingUseCase should call repository`() {
        val useCase = GetPermitByClassPagingUseCase(repository)
        val classKey = 10
        val params = GetPermitListQueryParams()
        val expected = flowOf(PagingData.empty<PermitByClassEntity>())
        every { repository.getPermitByClassPaging(classKey, params) } returns expected

        val result = useCase(classKey, params)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.getPermitByClassPaging(classKey, params) }
    }

    @Test
    fun `GetPermitByStudentListUseCase should call repository`() {
        val useCase = GetPermitByStudentListUseCase(repository)
        val id = "s1"
        val params = GetPermitListQueryParams()
        val expected = flowOf(Result.Success(listOf<PermitByStudentEntity>()))
        every { repository.getPermitListByStudent(id, params) } returns expected

        val result = useCase(id, params)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.getPermitListByStudent(id, params) }
    }

    @Test
    fun `GetPermitByClassListUseCase should call repository`() {
        val useCase = GetPermitByClassListUseCase(repository)
        val classKey = 10
        val params = GetPermitListQueryParams()
        val expected = flowOf(Result.Success(listOf<PermitByClassEntity>()))
        every { repository.getPermitListByClass(classKey, params) } returns expected

        val result = useCase(classKey, params)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.getPermitListByClass(classKey, params) }
    }

    @Test
    fun `GetPermitByIdUseCase should call repository`() {
        val useCase = GetPermitByIdUseCase(repository)
        val id = "p1"
        val expected = flowOf(Result.Success(mockk<PermitDetailEntity>()))
        every { repository.getPermitById(id) } returns expected

        val result = useCase(id)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.getPermitById(id) }
    }

    @Test
    fun `DoApprovalUseCase should call repository`() {
        val useCase = DoApprovalUseCase(repository)
        val body = PermitApprovalBody(listId = listOf("p1"), isApproved = false)
        val expected = flowOf(Result.Success(Unit))
        every { repository.doApproval(body) } returns expected

        val result = useCase(body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.doApproval(body) }
    }

    @Test
    fun `CreatePermitUseCase should call repository`() {
        val useCase = CreatePermitUseCase(repository)
        val body = CreateEditPermitBody()
        val expected = flowOf(Result.Success(Unit))
        every { repository.createPermit(body) } returns expected

        val result = useCase(body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.createPermit(body) }
    }

    @Test
    fun `EditPermitUseCase should call repository`() {
        val useCase = EditPermitUseCase(repository)
        val id = "p1"
        val body = CreateEditPermitBody()
        val expected = flowOf(Result.Success(Unit))
        every { repository.editPermit(id, body) } returns expected

        val result = useCase(id, body)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.editPermit(id, body) }
    }

    @Test
    fun `CancelPermitUseCase should call repository editPermit (as per current impl)`() {
        val useCase = CancelPermitUseCase(repository)
        val id = "p1"
        val expected = flowOf(Result.Success(Unit))
        // Current impl invokes repository.editPermit(id, CreateEditPermitBody())
        every { repository.editPermit(id, any()) } returns expected

        val result = useCase(id)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.editPermit(id, any()) }
    }
}