package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.data.source.network.model.response.globalsetting.GetGlobalSettingResponse
import com.rollinup.apiservice.data.source.network.model.response.sse.GeneralSettingResponse
import kotlinx.datetime.LocalTime
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GlobalSettingMapperTest {

    private lateinit var mapper: GlobalSettingMapper

    @Before
    fun setup() {
        mapper = GlobalSettingMapper()
    }

    //region mapGlobalSetting
    @Test
    fun `mapGlobalSetting should map SSE response correctly`() {
        // Arrange
        val response = GeneralSettingResponse(
            semesterStart = "2024-01-01",
            semesterEnd = "2024-06-01",
            updatedAt = "2024-01-01T12:00:00Z",
            schoolPeriodStart = "07:00",
            schoolPeriodEnd = "15:00",
            checkInPeriodStart = "06:30",
            checkInPeriodEnd = "07:30",
            latitude = -6.200000,
            longitude = 106.816666,
            radius = 50.0
        )

        // Act
        val result = mapper.mapGlobalSetting(response)

        // Assert
        assertEquals("2024-01-01", result.semesterStart)
        assertEquals(LocalTime.parse("07:00"), result.schoolPeriodStart)
        assertEquals(50.0, result.radius)
    }
    //endregion

    //region mapGetGlobalSettingResponse
    @Test
    fun `mapGetGlobalSettingResponse should map API response correctly`() {
        // Arrange
        val response = GetGlobalSettingResponse.Data(
            semesterStart = "2024-01-01",
            semesterEnd = "2024-06-01",
            updatedAt = "2024-01-01T12:00:00Z",
            schoolPeriodStart = "07:00",
            schoolPeriodEnd = "15:00",
            checkInPeriodStart = "06:30",
            checkInPeriodEnd = "07:30",
            latitude = -6.200000,
            longitude = 106.816666,
            radius = 50.0
        )

        // Act
        val result = mapper.mapGetGlobalSettingResponse(response)

        // Assert
        assertEquals("2024-01-01", result.semesterStart)
        assertEquals(LocalTime.parse("07:00"), result.schoolPeriodStart)
        assertEquals(50.0, result.radius)
    }
    //endregion
}