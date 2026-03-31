package com.rollinup.rollinup.component.di

import com.rollinup.rollinup.component.permitform.viewmodel.PermitFormViewModel
import com.rollinup.rollinup.component.profile.profilepopup.viemodel.ProfileDialogViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object ComponentModule {
    operator fun invoke() = module {
        viewModelOf(::PermitFormViewModel)
        viewModelOf(::ProfileDialogViewModel)
    }
}