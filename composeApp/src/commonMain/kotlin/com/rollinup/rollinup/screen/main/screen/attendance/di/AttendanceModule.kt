package com.rollinup.rollinup.screen.main.screen.attendance.di

import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.viewmodel.AttendanceByStudentViewModel
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.viewmodel.AttendanceByClassViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object AttendanceModule {
    operator fun invoke() = module {
        viewModelOf(::AttendanceByClassViewModel)
        viewModelOf(::AttendanceByStudentViewModel)
    }
}