package com.rollinup.rollinup.screen.dashboard

import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.now
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.plus

fun getAttendanceListDummy(): List<AttendanceByStudentEntity> {
    val today = LocalDateTime.now()

    return (0..5).map {
        val day = if (today.day + it > 28 || today.day + it < 1) it + 1 else today.day + it
        val localDateTime = LocalDateTime(
            date = LocalDate(day = day, month = today.month, year = today.year),
            time = today.time
        )

        AttendanceByStudentEntity(
            id = "id$it",
            status = AttendanceStatus.entries.random(),
            date = localDateTime.date.toString(),
            checkInTime = localDateTime.time.toString(),
            permit = if (it in listOf(3, 2)) {
                AttendanceByStudentEntity.Permit(
                    id = "id$it-peeremit",
                    reason = "balls itch",
                    type = PermitType.entries.random(),
                    start = localDateTime.toString(),
                    end = localDateTime.toString()
                )
            } else {
                null
            }
        )
    }
}

fun getAttendanceByClassDummy(): List<AttendanceByClassEntity> {
    val today = LocalDateTime.now()

    return (0..37).map { index ->

        val localDateTime = today.date.plus(DatePeriod(days = index)).atTime(today.time)

        // ----- Student -----
        val student = AttendanceByClassEntity.Student(
            id = "student-$index",
            name = "Student $index",
            studentId = "S00$index"
        )

        // ----- Attendance (70% chance of having attendance data) -----
        val attendance = if (index % 3 != 0) {
            AttendanceByClassEntity.Attendance(
                id = "att-$index",
                checkedInAt = localDateTime.toString(),
                status = AttendanceStatus.entries.random(),
                date = localDateTime.date.toString()
            )
        } else null

        // ----- Permit (only for index 2 and 3) -----
        val permit = if (index in listOf(2, 3)) {
            AttendanceByClassEntity.Permit(
                id = "permit-$index",
                reason = "Random reason $index",
                type = PermitType.entries.random(),
                start = localDateTime.toString(),
                end = localDateTime.toString()
            )
        } else null

        AttendanceByClassEntity(
            student = student,
            attendance = attendance,
            permit = permit
        )
    }
}


fun getAttendanceDetailDummy(): AttendanceDetailEntity {
    return AttendanceDetailEntity(
        id = "idasdasd",
        student = AttendanceDetailEntity.User(
            id = "adads",
            studentId = "student",
            name = "John Doe",
            xClass = "Mipa 4"
        ),
        status = AttendanceStatus.entries.random(),
        checkedInAt = LocalDateTime.now().toString(),
        updatedAt = LocalDateTime.now().toString(),
        createdAt = LocalDateTime.now().toString(),
        permit = AttendanceDetailEntity.Permit(
            id = "weqee",
            reason = "Balls itchy",
            type = PermitType.entries.random(),
            startTime = LocalDateTime.now().toString(),
            endTime = LocalDateTime.now().toString(),
            note = "NO Balls",
            attachment = "dasdasdas.jpg",
        )
    )
}