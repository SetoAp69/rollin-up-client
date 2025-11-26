package com.rollinup.rollinup.screen.main.screen.usercenter.di

import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.viewmodel.CreateEditUserViewModel
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.viewmodel.UserCenterViewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object UserCenterModule {
    operator fun invoke() = module {
        viewModelOf(::UserCenterViewmodel)
        viewModelOf(::CreateEditUserViewModel)
    }
}