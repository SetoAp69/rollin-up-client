package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.permit.GetPermitListByStudentResponse
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.utils.Utils
import com.rollinup.apiservice.utils.Utils.getFileLink
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PermitMapperTest {

    private lateinit var mapper: PermitMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(Utils)
        every { any<String>().getFileLink() } answers { "http://mock/${firstArg<String>()}" }
        mapper = PermitMapper()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    //region mapPermitListByStudentResponse
    @Test
    fun `mapPermitListByStudentResponse should map correctly`() {
        // Arrange
        val response = listOf(
            GetPermitListByStudentResponse.Data.PermitListDTO(
                id = "p1",
                studentId = "123",
                name = "Sakit",
                date = "2024-01-01",
                startTime = "07:00",
                reason = "Fever",
                approvalStatus = "APPROVED",
                type = "Sakit",
                endTime = "15:00",
                createdAt = "2023-12-31"
            )
        )

        // Act
        val result = mapper.mapPermitListByStudentResponse(response)

        // Assert
        val entity = result.first()
        assertEquals("p1", entity.id)
        assertEquals(ApprovalStatus.APPROVED, entity.approvalStatus)
        assertEquals(PermitType.ABSENCE, entity.type)
    }
    //endregion

    //region mapPermitListByClass
    @Test
    fun `mapPermitListByClass should map correctly`() {
        // Arrange
        val response = listOf(
            GetPermitListByClassResponse.Data.PermitListDTO(
                id = "p1",
                name = "Izin",
                date = "2024-01-01",
                startTime = "08:00",
                reason = "Family",
                approvalStatus = "APPROVAL_PENDING",
                type = "Izin",
                endTime = "12:00",
                student = GetPermitListByClassResponse.Data.User(
                    id = "s1",
                    name = "John",
                    xClass = "10A"
                ),
                createdAt = "2023-12-31"
            )
        )

        // Act
        val result = mapper.mapPermitListByClass(response)

        // Assert
        val entity = result.first()
        assertEquals("p1", entity.id)
        assertEquals(ApprovalStatus.APPROVAL_PENDING, entity.approvalStatus)
        assertEquals(PermitType.ABSENCE, entity.type)
        assertEquals("s1", entity.student.id)
    }
    //endregion

    //region mapPermitById
    @Test
    fun `mapPermitById should map all fields including approvedBy`() {
        // Arrange
        val response = GetPermitByIdResponse.Data(
            id = "p1",
            date = "2024-01-01",
            name = "Sakit",
            student = GetPermitByIdResponse.Data.User(
                id = "s1", name = "John", username = "john_d", studentId = "123", xClass = "10A"
            ),
            startTime = "07:00",
            endTime = "15:00",
            attachment = "doc.jpg",
            note = "Note",
            reason = "Reason",
            createdAt = "now",
            updatedAt = "now",
            approvalStatus = "APPROVED",
            approvalNote = "OK",
            approvedBy = GetPermitByIdResponse.Data.User(
                id = "t1", name = "Teacher", username = "teach", xClass = null
            ),
            approvedAt = "later"
        )

        // Act
        val result = mapper.mapPermitById(response)

        // Assert
        assertEquals("p1", result.id)
        assertEquals("http://mock/doc.jpg", result.attachment)
        assertNotNull(result.approvedBy)
        assertEquals("t1", result.approvedBy.id)
        assertEquals(ApprovalStatus.APPROVED, result.approvalStatus)
    }

    @Test
    fun `mapPermitById should handle null approvedBy`() {
        // Arrange
        val response = GetPermitByIdResponse.Data(
            id = "p1",
            date = "2024-01-01",
            name = "Sakit",
            student = GetPermitByIdResponse.Data.User(
                id = "s1", name = "John", username = "john_d", studentId = "123", xClass = "10A"
            ),
            startTime = "07:00",
            endTime = "15:00",
            attachment = "doc.jpg",
            note = "Note",
            reason = "Reason",
            createdAt = "now",
            updatedAt = "now",
            approvalStatus = "APPROVAL_PENDING",
            approvalNote = null,
            approvedBy = null, // Testing null branch
            approvedAt = null
        )

        // Act
        val result = mapper.mapPermitById(response)

        // Assert
        assertEquals(ApprovalStatus.APPROVAL_PENDING, result.approvalStatus)
        assertNull(result.approvedBy)
    }
    //endregion
}