package com.rollinup.apiservice.domain.user

import com.rollinup.apiservice.data.repository.user.UserRepository
import com.rollinup.apiservice.data.source.network.model.request.user.CheckEmailOrUsernameQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.DeleteUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitVerificationOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody

class GetUserListUseCase(private val repository: UserRepository) {
    operator fun invoke(queryParams: GetUserQueryParams) = repository.getUserList(queryParams)
}

class GetUserPagingUseCase(private val repository: UserRepository) {
    operator fun invoke(queryParams: GetUserQueryParams) = repository.getUserPaging(queryParams)
}

class GetUserByIdUseCase(private val repository: UserRepository) {
    operator fun invoke(id: String) = repository.getUserById(id)
}

class RegisterUserUseCase(private val repository: UserRepository) {
    operator fun invoke(body: CreateEditUserBody) = repository.registerUser(body)
}

class EditUserUseCase(private val repository: UserRepository) {
    operator fun invoke(id: String, body: CreateEditUserBody) = repository.editUser(id, body)
}

class CreateResetPasswordRequestUseCase(private val repository: UserRepository) {
    operator fun invoke(body: CreateResetPasswordRequestBody) =
        repository.createResetPasswordRequest(body)
}

class SubmitResetOtpUseCase(private val repository: UserRepository) {
    operator fun invoke(body: SubmitResetPasswordOTPBody) = repository.submitResetOtp(body)
}

class SubmitResetPasswordUseCase(private val repository: UserRepository) {
    operator fun invoke(body: SubmitResetPasswordBody) = repository.submitResetPassword(body)
}

class GetUserOptionsUseCase(private val repository: UserRepository) {
    operator fun invoke() = repository.getOptions()
}

class DeleteUserUseCase(private val repository: UserRepository) {
    operator fun invoke(body: DeleteUserBody) = repository.deleteUser(body)
}

class CheckEmailOrUsernameUseCase(private val repository: UserRepository) {
    operator fun invoke(queryParams: CheckEmailOrUsernameQueryParams) =
        repository.checkEmailOrUsername(queryParams)
}

class SubmitVerificationOtpUseCase(private val repository: UserRepository) {
    operator fun invoke(body: SubmitVerificationOTPBody) = repository.submitVerificationOtp(body)
}

class UpdatePasswordAndVerificationUseCase(private val repository: UserRepository) {
    operator fun invoke(body: UpdatePasswordAndVerificationBody) =
        repository.updatePasswordAndVerification(body)
}

class ResendVerificationOtpUseCase(private val repository: UserRepository) {
    operator fun invoke() = repository.resendVerificationOtp()
}
