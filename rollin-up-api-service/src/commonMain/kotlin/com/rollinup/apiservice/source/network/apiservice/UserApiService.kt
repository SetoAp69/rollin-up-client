package com.rollinup.apiservice.source.network.apiservice

import com.rollinup.apiservice.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.source.network.model.request.user.SubmitOTPBody
import com.rollinup.apiservice.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.source.network.model.response.ApiResponse
import com.rollinup.apiservice.source.network.model.response.user.GetUserByIdResponse
import com.rollinup.apiservice.source.network.model.response.user.GetUserListResponse
import com.rollinup.apiservice.source.network.model.response.user.ResetPasswordRequestResponse
import com.rollinup.apiservice.source.network.model.response.user.SubmitResetOtpResponse

interface UserApiService {
    suspend fun getUsersList(
        queryParams: GetUserQueryParams,
    ): ApiResponse<GetUserListResponse>

    suspend fun getUserById(userId: String): ApiResponse<GetUserByIdResponse>

    suspend fun registerUser( body: CreateEditUserBody): ApiResponse<Unit>

    suspend fun editUser(id:String, body: CreateEditUserBody): ApiResponse<Unit>

    suspend fun createResetPasswordRequest(
        body: CreateResetPasswordRequestBody,
    ): ApiResponse<ResetPasswordRequestResponse>

    suspend fun submitResetOtp( body: SubmitOTPBody): ApiResponse<SubmitResetOtpResponse>

    suspend fun submitResetPassword(body: SubmitResetPasswordBody): ApiResponse<Unit>

}