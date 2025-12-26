package com.rollinup.apiservice.di

import com.rollinup.apiservice.domain.attendance.CheckInUseCase
import com.rollinup.apiservice.domain.attendance.CreateAttendanceDataUseCase
import com.rollinup.apiservice.domain.attendance.EditAttendanceDataUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByClassSummaryUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByIdUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentListUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentPagingUseCase
import com.rollinup.apiservice.domain.attendance.GetAttendanceByStudentSummaryUseCase
import com.rollinup.apiservice.domain.attendance.GetDashboardDataUseCase
import com.rollinup.apiservice.domain.attendance.GetExportAttendanceDataUseCase
import com.rollinup.apiservice.domain.auth.ClearClientTokenUseCase
import com.rollinup.apiservice.domain.auth.LoginJWTUseCase
import com.rollinup.apiservice.domain.auth.LoginUseCase
import com.rollinup.apiservice.domain.auth.UpdatePasswordAndDeviceUseCase
import com.rollinup.apiservice.domain.globalsetting.EditGlobalSettingUseCase
import com.rollinup.apiservice.domain.globalsetting.GetCachedGlobalSettingUseCase
import com.rollinup.apiservice.domain.globalsetting.GetGlobalSettingUseCase
import com.rollinup.apiservice.domain.globalsetting.ListenGlobalSettingSSE
import com.rollinup.apiservice.domain.globalsetting.UpdateCachedGlobalSettingUseCase
import com.rollinup.apiservice.domain.permit.CancelPermitUseCase
import com.rollinup.apiservice.domain.permit.CreatePermitUseCase
import com.rollinup.apiservice.domain.permit.DoApprovalUseCase
import com.rollinup.apiservice.domain.permit.EditPermitUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByClassListUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByClassPagingUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByIdUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByStudentListUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByStudentPagingUseCase
import com.rollinup.apiservice.domain.token.ClearRefreshTokenUseCase
import com.rollinup.apiservice.domain.token.ClearTokenUseCase
import com.rollinup.apiservice.domain.token.GetRefreshTokenUseCase
import com.rollinup.apiservice.domain.token.GetTokenUseCase
import com.rollinup.apiservice.domain.token.UpdateRefreshTokenUseCase
import com.rollinup.apiservice.domain.token.UpdateTokenUseCase
import com.rollinup.apiservice.domain.uimode.GetUiModeUseCase
import com.rollinup.apiservice.domain.uimode.UpdateUiModeUseCase
import com.rollinup.apiservice.domain.user.CheckEmailOrUsernameUseCase
import com.rollinup.apiservice.domain.user.CreateResetPasswordRequestUseCase
import com.rollinup.apiservice.domain.user.DeleteUserUseCase
import com.rollinup.apiservice.domain.user.EditUserUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.domain.user.GetUserListUseCase
import com.rollinup.apiservice.domain.user.GetUserOptionsUseCase
import com.rollinup.apiservice.domain.user.GetUserPagingUseCase
import com.rollinup.apiservice.domain.user.RegisterUserUseCase
import com.rollinup.apiservice.domain.user.ResendVerificationOtpUseCase
import com.rollinup.apiservice.domain.user.SubmitResetOtpUseCase
import com.rollinup.apiservice.domain.user.SubmitResetPasswordUseCase
import com.rollinup.apiservice.domain.user.SubmitVerificationOtpUseCase
import com.rollinup.apiservice.domain.user.UpdatePasswordAndVerificationUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DomainModule {
    operator fun invoke() = module {
        authDomain()
        userDomain()
        globalSettingDomain()
        permitDomain()
        attendanceDomain()
        tokenDomain()
        uiModeDomain()
    }

    private fun Module.authDomain() {
        singleOf(::LoginUseCase)
        singleOf(::LoginJWTUseCase)
        singleOf(::UpdatePasswordAndDeviceUseCase)
        singleOf(::ClearClientTokenUseCase)
    }

    private fun Module.globalSettingDomain() {
        singleOf(::ListenGlobalSettingSSE)
        singleOf(::GetCachedGlobalSettingUseCase)
        singleOf(::GetGlobalSettingUseCase)
        singleOf(::EditGlobalSettingUseCase)
        singleOf(::UpdateCachedGlobalSettingUseCase )
    }

    private fun Module.userDomain() {
        singleOf(::GetUserListUseCase)
        singleOf(::GetUserPagingUseCase)
        singleOf(::GetUserByIdUseCase)
        singleOf(::RegisterUserUseCase)
        singleOf(::EditUserUseCase)
        singleOf(::CreateResetPasswordRequestUseCase)
        singleOf(::SubmitResetOtpUseCase)
        singleOf(::SubmitResetPasswordUseCase)
        singleOf(::GetUserOptionsUseCase)
        singleOf(::DeleteUserUseCase)
        singleOf(::CheckEmailOrUsernameUseCase)
        singleOf(::UpdatePasswordAndVerificationUseCase)
        singleOf(::ResendVerificationOtpUseCase)
        singleOf(::SubmitVerificationOtpUseCase)
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
        singleOf(::CancelPermitUseCase)
    }

    private fun Module.attendanceDomain() {
        singleOf(::GetAttendanceByStudentPagingUseCase)
        singleOf(::GetAttendanceByClassPagingUseCase)
        singleOf(::GetAttendanceByClassListUseCase)
        singleOf(::GetAttendanceByClassSummaryUseCase)
        singleOf(::GetAttendanceByStudentSummaryUseCase)
        singleOf(::GetAttendanceByStudentListUseCase)
        singleOf(::GetAttendanceByIdUseCase)
        singleOf(::CreateAttendanceDataUseCase)
        singleOf(::EditAttendanceDataUseCase)
        singleOf(::GetDashboardDataUseCase)
        singleOf(::CheckInUseCase)
        singleOf(::GetExportAttendanceDataUseCase)
    }

    private fun Module.tokenDomain() {
        singleOf(::GetTokenUseCase)
        singleOf(::GetRefreshTokenUseCase)
        singleOf(::UpdateTokenUseCase)
        singleOf(::UpdateRefreshTokenUseCase)
        singleOf(::ClearTokenUseCase)
        singleOf(::ClearRefreshTokenUseCase)
    }

    private fun Module.uiModeDomain() {
        singleOf(::GetUiModeUseCase)
        singleOf(::UpdateUiModeUseCase)
    }
}

