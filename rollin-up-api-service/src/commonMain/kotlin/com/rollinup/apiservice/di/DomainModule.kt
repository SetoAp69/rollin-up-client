package com.rollinup.apiservice.di

import com.rollinup.apiservice.domain.attendance.CreateAttendanceDataUseCase
import com.rollinup.apiservice.domain.attendance.EditAttendanceDataUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetDashboardDataUseCase
import com.rollinup.apiservice.domain.auth.LoginJWTUseCase
import com.rollinup.apiservice.domain.auth.LoginUseCase
import com.rollinup.apiservice.domain.generalsetting.ListenGeneralSettingSSE
import com.rollinup.apiservice.domain.pagging.GetPagingDummyUseCase
import com.rollinup.apiservice.domain.permit.CreatePermitUseCase
import com.rollinup.apiservice.domain.permit.DoApprovalUseCase
import com.rollinup.apiservice.domain.permit.EditPermitUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByClassListUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByClassPagingUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByIdUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByStudentListUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByStudentPagingUseCase
import com.rollinup.apiservice.domain.user.CreateResetPasswordRequestUseCase
import com.rollinup.apiservice.domain.user.EditUserUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.domain.user.GetUserListUseCase
import com.rollinup.apiservice.domain.user.RegisterUserUseCase
import com.rollinup.apiservice.domain.user.SubmitResetOtpUseCase
import com.rollinup.apiservice.domain.user.SubmitResetPasswordUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DomainModule {
    operator fun invoke() = module {
        authDomain()
        userDomain()
        generalSettingModule()
        pagingDummyDomain()
        permitDomain()
        attendanceDomain()
    }

    private fun Module.authDomain() {
        singleOf(::LoginUseCase)
        singleOf(::LoginJWTUseCase)
    }

    private fun Module.generalSettingModule() {
        singleOf(::ListenGeneralSettingSSE)
    }

    private fun Module.userDomain() {
        singleOf(::GetUserListUseCase)
        singleOf(::GetUserByIdUseCase)
        singleOf(::RegisterUserUseCase)
        singleOf(::EditUserUseCase)
        singleOf(::CreateResetPasswordRequestUseCase)
        singleOf(::SubmitResetOtpUseCase)
        singleOf(::SubmitResetPasswordUseCase)
    }

    private fun Module.pagingDummyDomain() {
        singleOf(::GetPagingDummyUseCase)
    }

    private fun Module.permitDomain() {
        singleOf(::GetPermitByIdUseCase)
        singleOf(::GetPermitByStudentPagingUseCase)
        singleOf(::GetPermitByClassPagingUseCase)
        singleOf(::GetPermitByStudentListUseCase)
        singleOf(::GetPermitByClassListUseCase)
        singleOf(::DoApprovalUseCase)
        singleOf(::CreatePermitUseCase)
        singleOf(::EditPermitUseCase)
    }

    private fun Module.attendanceDomain(){
        singleOf(::GetAttendanceByStudentPagingUseCase)
        singleOf(::GetAttendanceByClassPagingUseCase)
        singleOf(::GetAttendanceByClassListUseCase)
        singleOf(::GetAttendanceByStudentListUseCase)
        singleOf(::GetAttendanceByIdUseCase)
        singleOf(::CreateAttendanceDataUseCase)
        singleOf(::EditAttendanceDataUseCase)
        singleOf(::GetDashboardDataUseCase)
    }
}

