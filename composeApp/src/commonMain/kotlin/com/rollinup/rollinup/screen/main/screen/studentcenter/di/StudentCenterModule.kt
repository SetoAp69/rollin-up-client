package com.rollinup.rollinup.screen.main.screen.studentcenter.di

import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.viewmodel.StudentCenterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object StudentCenterModule {
    operator fun invoke() = module {
        viewModelOf(::StudentCenterViewModel)
    }
}