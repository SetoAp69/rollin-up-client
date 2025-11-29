package com.rollinup.rollinup.screen.main.screen.attendance.di

import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.viewmodel.AttendanceByStudentViewModel
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.viewmodel.AttendanceViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object AttendanceModule {
    operator fun invoke() = module {
        viewModelOf(::AttendanceViewModel)
        viewModelOf(::AttendanceByStudentViewModel)
    }
}