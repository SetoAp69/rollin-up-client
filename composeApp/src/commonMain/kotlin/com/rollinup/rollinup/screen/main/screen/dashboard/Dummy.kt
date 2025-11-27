package com.rollinup.rollinup.screen.dashboard

import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.common.utils.Utils.now
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.plus

fun getDummyUsers(total: Int): List<UserEntity> {
    val genders = listOf(Gender.MALE, Gender.FEMALE)
    return List(total) { index ->
        UserEntity(
            id = "user-${index + 1}",
            userName = "user${index + 1}",
            classX = "Class ${(1..12).random()}",
            email = "user${index + 1}@example.com",
            fullName = "User Fullname ${index + 1}",
            studentId = "SID${1000 + index}",
            address = "Street No. ${(1..200).random()} City",
            gender = genders.random(),
            role = listOf("admin", "teacher", "student").random()
        )
    }
}

fun generateDummyUserDetail(): UserDetailEntity {
    val id = "user-${(1..9999).random()}"
    val firstName = listOf("Nana", "Riko", "Sena", "Hana", "Mika", "Ando").random()
    val lastName = listOf("Putra", "Sato", "Wijaya", "Tanaka", "Saputra").random()

    return UserDetailEntity(
        id = id,
        userName = "${firstName.lowercase()}.${lastName.lowercase()}",
        firstName = firstName,
        lastName = lastName,
        classX = UserDetailEntity.Data(
            id = "class-${(1..12).random()}",
            name = "Class ${(1..12).random()}",
            key = (1..12).random()
        ),
        email = "$firstName.$lastName@example.com".lowercase(),
        fullName = "$firstName $lastName",
        studentId = "SID${(1000..9999).random()}",
        address = "Street No. ${(1..200).random()} City",
        gender = listOf(Gender.MALE, Gender.FEMALE).random(),
        phoneNumber = "08${(100000000..999999999).random()}",
        birthDay = "${(1995..2012).random()}-${
            (1..12).random().toString().padStart(2, '0')
        }-${(1..28).random().toString().padStart(2, '0')}",
        role = UserDetailEntity.Data(
            id = listOf("admin", "teacher", "student").random(),
            name = listOf("Administrator", "Teacher", "Student").random(),
            key = (1..3).random()
        )
    )
}


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

fun generateDummyPermitByStudentList(total: Int): List<PermitByStudentEntity> {
    return List(total) { index ->
        PermitByStudentEntity(
            id = "permit-student-$index",
            studentId = "SID${1000 + index}",
            name = "Student ${index + 1}",
            date = "2025-${(1..12).random().toString().padStart(2, '0')}-${
                (1..28).random().toString().padStart(2, '0')
            }",
            startTime = "${(7..10).random()}:00",
            endTime = "${(11..14).random()}:00",
            reason = listOf("Sick", "Competition", "Family Event", null).random(),
            approvalStatus = ApprovalStatus.entries.random(),
            type = PermitType.entries.random(),
            createdAt = "2025-${(1..12).random().toString().padStart(2, '0')}-${
                (1..28).random().toString().padStart(2, '0')
            }T07:30:00"
        )
    }
}

fun generateDummyPermitByClassList(total: Int): List<PermitByClassEntity> {
    val types = listOf("DISPENSATION", "ABSENT")

    return List(total) { index ->
        val studentId = "student-${1000 + index}"
        val studentName = "Student ${index + 1}"

        PermitByClassEntity(
            id = "permit-class-$index",
            name = studentName,
            date = "2025-${(1..12).random().toString().padStart(2, '0')}-${
                (1..28).random().toString().padStart(2, '0')
            }",
            startTime = "${(7..10).random()}:00",
            endTime = "${(11..14).random()}:00",
            reason = listOf("Sick", "Family Event", "Competition", null).random(),
            approvalStatus = ApprovalStatus.entries.random(),
            type = types.random(),
            student = PermitByClassEntity.User(
                id = studentId,
                name = studentName,
                xClass = "X-${(1..12).random()}"
            ),
            createdAt = "2025-${(1..12).random().toString().padStart(2, '0')}-${
                (1..28).random().toString().padStart(2, '0')
            }T08:00:00"
        )
    }
}

fun generateDummyPermitDetail(): PermitDetailEntity {
    val studentId = "SID${(1000..9999).random()}"
    val studentName = listOf("Aiko", "Rafi", "Sora", "Luna", "Nata").random()

    return PermitDetailEntity(
        id = "permit-detail-${(1..999).random()}",
        date = "2025-${(1..12).random().toString().padStart(2, '0')}-${(1..28).random().toString().padStart(2, '0')}",
        name = studentName,
        type = PermitType.entries.random(),
        startTime = "${(7..10).random()}:00",
        endTime = "${(11..14).random()}:00",
        attachment = "https://example.com/image${(1..10).random()}.jpg",
        note = listOf("Please approve ASAP", "Sent via app", null).random(),
        reason = listOf("Sick", "Competition", "Family Emergency", null).random(),
        createdAt = "2025-01-${(1..28).random().toString().padStart(2, '0')}T08:12:00",
        updatedAt = "2025-01-${(1..28).random().toString().padStart(2, '0')}T09:00:00",
        approvalStatus = ApprovalStatus.entries.random(),
        approvalNote = listOf("Approved", "Check details again", null).random(),
        approvedAt = if ((0..1).random() == 1) "2025-01-15T10:00:00" else null,
        approvedBy = if ((0..1).random() == 1) {
            PermitDetailEntity.User(
                id = "teacher-${(1..50).random()}",
                name = "Teacher ${(1..50).random()}",
                username = "teacher${(1..50).random()}",
                studentId = null,
                xClass = null
            )
        } else null,
        student = PermitDetailEntity.User(
            id = studentId,
            name = studentName,
            username = studentName.lowercase(),
            studentId = studentId,
            xClass = "X-${(1..12).random()}"
        )
    )
}


