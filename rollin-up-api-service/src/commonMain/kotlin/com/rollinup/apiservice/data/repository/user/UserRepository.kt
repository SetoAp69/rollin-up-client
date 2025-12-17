package com.rollinup.apiservice.data.repository.user

import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.user.CheckEmailOrUsernameQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.DeleteUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitVerificationOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.model.user.UserOptionEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserList(queryParams: GetUserQueryParams): Flow<Result<List<UserEntity>, NetworkError>>

    fun getUserPaging(queryParams: GetUserQueryParams): Flow<PagingData<UserEntity>>

    fun getUserById(id: String): Flow<Result<UserDetailEntity, NetworkError>>

    fun registerUser(body: CreateEditUserBody): Flow<Result<Unit, NetworkError>>

    fun editUser(id: String, body: CreateEditUserBody): Flow<Result<Unit, NetworkError>>

    fun createResetPasswordRequest(body: CreateResetPasswordRequestBody): Flow<Result<String, NetworkError>>

    fun submitResetOtp(body: SubmitResetPasswordOTPBody): Flow<Result<String, NetworkError>>

    fun submitResetPassword(body: SubmitResetPasswordBody): Flow<Result<Unit, NetworkError>>

    fun getOptions(): Flow<Result<UserOptionEntity, NetworkError>>

    fun deleteUser(body: DeleteUserBody): Flow<Result<Unit, NetworkError>>

    fun checkEmailOrUsername(queryParams: CheckEmailOrUsernameQueryParams): Flow<Result<Unit, NetworkError>>

    fun submitVerificationOtp(body: SubmitVerificationOTPBody): Flow<Result<String, NetworkError>>

    fun updatePasswordAndVerification(body: UpdatePasswordAndVerificationBody): Flow<Result<Unit, NetworkError>>

    fun resendVerificationOtp(): Flow<Result<Unit, NetworkError>>
}