package com.rollinup.rollinup.screen.main.screen.globalsetting.di

import com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.viewmodel.GlobalSettingViewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object GlobalSettingModule {
    operator fun invoke() = module {
        viewModelOf(::GlobalSettingViewmodel)
    }
}