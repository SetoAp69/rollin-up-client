package com.rollinup.apiservice.data.repository.user

import androidx.paging.PagingData
import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.model.user.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserList(queryParams: GetUserQueryParams): Flow<Result<List<UserEntity>, NetworkError>>

    fun getUserPaging(queryParams: GetUserQueryParams): Flow<PagingData<UserEntity>>

    fun getUserById(id: String): Flow<Result<UserDetailEntity, NetworkError>>

    fun registerUser(body: CreateEditUserBody): Flow<Result<Unit, NetworkError>>

    fun editUser(id: String, body: CreateEditUserBody): Flow<Result<Unit, NetworkError>>

    fun createResetPasswordRequest(body: CreateResetPasswordRequestBody): Flow<Result<String, NetworkError>>

    fun submitResetOtp(body: SubmitOTPBody): Flow<Result<String, NetworkError>>

    fun submitResetPassword(body: SubmitResetPasswordBody): Flow<Result<Unit, NetworkError>>


}