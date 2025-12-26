package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByClassResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceListByStudentResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetAttendanceSummaryResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetDashboardDataResponse
import com.rollinup.apiservice.data.source.network.model.response.attendance.GetExportAttendanceDataResponse
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.attendance.DashboardDataEntity
import com.rollinup.apiservice.model.attendance.ExportAttendanceDataEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.utils.Utils.getFileLink

class AttendanceMapper {
    fun mapAttendanceListByStudent(data: List<GetAttendanceListByStudentResponse.Data.GetAttendanceByStudentListDTO>): List<AttendanceByStudentEntity> {
        return data.map { d ->
            AttendanceByStudentEntity(
                id = d.id,
                status = AttendanceStatus.fromValue(d.status),
                date = d.date,
                checkInTime = d.checkedInAt,
                permit = d.permit?.let { p ->
                    AttendanceByStudentEntity.Permit(
                        id = p.id,
                        reason = p.reason,
                        type = PermitType.fromValue(p.type),
                        start = p.startTime,
                        end = p.endTime
                    )
                }
            )
        }
    }

    fun mapDashboardData(data: GetDashboardDataResponse.Data): DashboardDataEntity {
        return DashboardDataEntity(
            attendanceStatus = AttendanceStatus.fromValue(data.status ?: ""),
            summary = with(data.summary) {
                AttendanceSummaryEntity(
                    checkedIn = checkedIn,
                    late = late,
                    excused = excused,
                    approvalPending = approvalPending,
                    absent = absent,
                    sick = sick,
                    other = other
                )
            }
        )
    }

    fun mapAttendanceListByClass(data: List<GetAttendanceListByClassResponse.Data.Data>): List<AttendanceByClassEntity> {
        return data.map { d ->
            AttendanceByClassEntity(
                student = d.student.let { s ->
                    AttendanceByClassEntity.Student(
                        id = s.id,
                        name = s.name,
                        studentId = s.studentId ?: ""
                    )
                },
                attendance = d.attendance?.let { a ->
                    AttendanceByClassEntity.Attendance(
                        id = a.id,
                        checkedInAt = a.checkedInAt,
                        status = AttendanceStatus.fromValue(a.status),
                        date = a.date
                    )
                },
                permit = d.permit?.let { p ->
                    AttendanceByClassEntity.Permit(
                        id = p.id,
                        reason = p.reason,
                        type = PermitType.fromValue(p.type),
                        start = p.startTime,
                        end = p.endTime
                    )
                }
            )
        }
    }

    fun mapAttendanceByClassSummary(data: GetAttendanceSummaryResponse.Data) =
        AttendanceSummaryEntity(
            checkedIn = data.checkedIn,
            late = data.late,
            excused = data.excused,
            approvalPending = data.approvalPending,
            absent = data.absent,
            sick = data.sick,
            other = data.other
        )

    fun mapAttendanceById(data: GetAttendanceByIdResponse.Data): AttendanceDetailEntity {
        return AttendanceDetailEntity(
            id = data.id,
            student = data.student.let { s ->
                AttendanceDetailEntity.User(
                    id = s.id,
                    name = s.name,
                    studentId = s.studentId,
                    xClass = s.xClass
                )
            },
            status = AttendanceStatus.fromValue(data.status),
            checkedInAt = data.checkedInAt,
            updatedAt = data.updatedAt,
            createdAt = data.createdAt,
            permit = data.permit?.let { p ->
                AttendanceDetailEntity.Permit(
                    id = p.id,
                    reason = p.reason,
                    type = PermitType.fromValue(p.type),
                    startTime = p.startTime,
                    endTime = p.endTime,
                    note = p.note,
                    attachment = p.attachment.getFileLink(),
                    approvalNote = p.approvalNote,
                    approvedBy = p.approvedBy?.let { ap ->
                        AttendanceDetailEntity.User(
                            id = ap.id,
                            studentId = "",
                            name = ap.name,
                            xClass = ap.xClass
                        )
                    },
                    approvedAt = p.approvedAt
                )
            }
        )
    }

    fun mapExportAttendanceData(data: GetExportAttendanceDataResponse.Data) =
        ExportAttendanceDataEntity(
            sDateRange = data.sDateRange,
            data = data.data.map { d ->
                ExportAttendanceDataEntity.Data(
                    fullName = d.fullName,
                    classX = d.classX,
                    studentId = d.studentId,
                    dataPerDate = d.dataPerDate.map { record ->
                        ExportAttendanceDataEntity.Data.AttendanceRecord(
                            sDate = record.date,
                            status = record.status
                        )
                    }
                )
            }
        )

}