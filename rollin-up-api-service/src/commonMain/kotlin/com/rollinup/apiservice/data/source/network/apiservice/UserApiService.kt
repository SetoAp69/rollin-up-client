package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.request.user.CreateEditUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.DeleteUserBody
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitVerificationOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserListResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserOptionsResponse
import com.rollinup.apiservice.data.source.network.model.response.user.ResetPasswordRequestResponse
import com.rollinup.apiservice.data.source.network.model.response.user.SubmitResetOtpResponse
import com.rollinup.apiservice.data.source.network.model.response.user.ValidateVerificationOtpResponse

interface UserApiService {
    suspend fun getUsersList(
        queryParams: GetUserQueryParams,
    ): ApiResponse<GetUserListResponse>

    suspend fun getUserById(userId: String): ApiResponse<GetUserByIdResponse>

    suspend fun registerUser(body: CreateEditUserBody): ApiResponse<Unit>

    suspend fun editUser(id: String, body: CreateEditUserBody): ApiResponse<Unit>

    suspend fun createResetPasswordRequest(
        body: CreateResetPasswordRequestBody,
    ): ApiResponse<ResetPasswordRequestResponse>

    suspend fun submitResetOtp(body: SubmitResetPasswordOTPBody): ApiResponse<SubmitResetOtpResponse>

    suspend fun submitResetPassword(body: SubmitResetPasswordBody): ApiResponse<Unit>

    suspend fun getUserOptions(): ApiResponse<GetUserOptionsResponse>

    suspend fun deleteUser(body: DeleteUserBody): ApiResponse<Unit>

    suspend fun checkEmailOrUsername(queryParams: Map<String, String?>): ApiResponse<Unit>

    suspend fun submitVerificationOtp(body: SubmitVerificationOTPBody): ApiResponse<ValidateVerificationOtpResponse>

    suspend fun updatePasswordAndVerification(body: UpdatePasswordAndVerificationBody): ApiResponse<Unit>

    suspend fun resetVerificationOtp(): ApiResponse<Unit>
}