package com.rollinup.apiservice.model.attendance

import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class ExportAttendanceDataEntityTest {

    @Test
    fun `default constructor should create empty lists`() {
        val entity = ExportAttendanceDataEntity()
        assertTrue(entity.sDateRange.isEmpty(), "sDateRange should be empty by default")
        assertTrue(entity.data.isEmpty(), "data list should be empty by default")
        assertTrue(entity.dateRange.isEmpty(), "dateRange computed property should be empty")
    }

    @Test
    fun `dateRange should parse valid ISO date strings to LocalDate`() {
        // Arrange
        val dateStrings = listOf("2024-01-01", "2024-12-31")
        val entity = ExportAttendanceDataEntity(sDateRange = dateStrings)

        // Act
        val result = entity.dateRange

        // Assert
        assertEquals(2, result.size)
        assertEquals(LocalDate(2024, 1, 1), result[0])
        assertEquals(LocalDate(2024, 12, 31), result[1])
    }

    @Test
    fun `dateRange should throw exception for invalid date formats`() {
        // Arrange
        val invalidDateStrings = listOf("01-01-2024") // Wrong format (requires YYYY-MM-DD)
        val entity = ExportAttendanceDataEntity(sDateRange = invalidDateStrings)

        // Act & Assert
        assertFailsWith<IllegalArgumentException> {
            entity.dateRange
        }
    }

    @Test
    fun `Data default constructor should have default values`() {
        val data = ExportAttendanceDataEntity.Data()
        assertEquals("", data.fullName)
        assertEquals("", data.classX)
        assertEquals("", data.studentId)
        assertTrue(data.dataPerDate.isEmpty())
    }

    @Test
    fun `Data should hold provided values correctly`() {
        val data = ExportAttendanceDataEntity.Data(
            fullName = "John Doe",
            classX = "10A",
            studentId = "STU001",
            dataPerDate = listOf(ExportAttendanceDataEntity.Data.AttendanceRecord())
        )

        assertEquals("John Doe", data.fullName)
        assertEquals("10A", data.classX)
        assertEquals("STU001", data.studentId)
        assertEquals(1, data.dataPerDate.size)
    }


    @Test
    fun `AttendanceRecord default constructor should have default values`() {
        val record = ExportAttendanceDataEntity.Data.AttendanceRecord()
        assertEquals("", record.sDate)
        assertEquals("", record.status)
    }

    @Test
    fun `AttendanceRecord date property should parse valid ISO string`() {
        // Arrange
        val record = ExportAttendanceDataEntity.Data.AttendanceRecord(
            sDate = "2024-05-20",
            status = "Present"
        )

        // Act
        val result = record.date

        // Assert
        assertEquals(LocalDate(2024, 5, 20), result)
    }

    @Test
    fun `AttendanceRecord date property should throw exception for empty or invalid string`() {
        // Arrange
        val record = ExportAttendanceDataEntity.Data.AttendanceRecord(sDate = "")

        // Act & Assert
        assertFailsWith<IllegalArgumentException> {
            record.date
        }
    }


    @Test
    fun `Full object graph should maintain data integrity`() {
        // Arrange
        val record1 = ExportAttendanceDataEntity.Data.AttendanceRecord("2024-01-01", "Present")
        val record2 = ExportAttendanceDataEntity.Data.AttendanceRecord("2024-01-02", "Absent")

        val studentData = ExportAttendanceDataEntity.Data(
            fullName = "Jane Doe",
            classX = "12B",
            studentId = "123",
            dataPerDate = listOf(record1, record2)
        )

        val entity = ExportAttendanceDataEntity(
            sDateRange = listOf("2024-01-01", "2024-01-02"),
            data = listOf(studentData)
        )

        // Assert
        assertEquals(1, entity.data.size)
        assertEquals("Jane Doe", entity.data[0].fullName)

        // Verify computed dates in nested records
        assertEquals(LocalDate(2024, 1, 1), entity.data[0].dataPerDate[0].date)
        assertEquals("Present", entity.data[0].dataPerDate[0].status)

        assertEquals(LocalDate(2024, 1, 2), entity.data[0].dataPerDate[1].date)
        assertEquals("Absent", entity.data[0].dataPerDate[1].status)
    }
}