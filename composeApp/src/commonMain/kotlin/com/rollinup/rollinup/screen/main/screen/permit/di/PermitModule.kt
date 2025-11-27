package com.rollinup.rollinup.screen.main.screen.permit.di

import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.viewmodel.PermitApprovalViewModel
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.viewmodel.PermitDetailViewModel
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.viewmodel.StudentPermitViewModel
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.viewmodel.TeacherPermitViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object PermitModule {
    operator fun invoke() = module {
        viewModelOf(::PermitDetailViewModel)
        viewModelOf(::PermitApprovalViewModel)
        viewModelOf(::TeacherPermitViewModel)
        viewModelOf(::StudentPermitViewModel)
    }
}