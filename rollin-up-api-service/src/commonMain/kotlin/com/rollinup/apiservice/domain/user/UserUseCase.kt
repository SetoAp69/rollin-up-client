package com.rollinup.apiservice.domain.user

import com.rollinup.apiservice.data.repository.user.UserRepository
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody

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
    operator fun invoke(body: SubmitOTPBody) = repository.submitResetOtp(body)
}

class SubmitResetPasswordUseCase(private val repository: UserRepository) {
    operator fun invoke(body: SubmitResetPasswordBody) = repository.submitResetPassword(body)
}