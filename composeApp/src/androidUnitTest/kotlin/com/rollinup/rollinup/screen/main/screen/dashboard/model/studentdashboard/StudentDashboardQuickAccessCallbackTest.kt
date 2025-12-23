package com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard

import org.junit.Assert
import org.junit.Test

class StudentDashboardQuickAccessCallbackTest {

    @Test
    fun `callbacks should execute assigned functions correctly`() {
        // Arrange
        var isCreatePermitCalled = false
        var isCheckInCalled = false
        var isGoToPermitHistoryCalled = false
        var isGoToAttendanceHistoryCalled = false

        val callback = StudentDashboardQuickAccessCallback(
            onCreatePermit = { isCreatePermitCalled = true },
            onCheckIn = { isCheckInCalled = true },
            onGoToPermitHistory = { isGoToPermitHistoryCalled = true },
            onGoToAttendanceHistory = { isGoToAttendanceHistoryCalled = true }
        )

        // Act
        callback.onCreatePermit()
        callback.onCheckIn()
        callback.onGoToPermitHistory()
        callback.onGoToAttendanceHistory()

        // Assert
        Assert.assertTrue("onCreatePermit should have been called", isCreatePermitCalled)
        Assert.assertTrue("onCheckIn should have been called", isCheckInCalled)
        Assert.assertTrue("onGoToPermitHistory should have been called", isGoToPermitHistoryCalled)
        Assert.assertTrue(
            "onGoToAttendanceHistory should have been called",
            isGoToAttendanceHistoryCalled
        )
    }
}