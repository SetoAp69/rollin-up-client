package com.rollinup.rollinup.screen.main.screen.permit.model

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.permit.ApprovalStatus
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_approval_pending
import rollin_up.composeapp.generated.resources.label_approved
import rollin_up.composeapp.generated.resources.label_canceled
import rollin_up.composeapp.generated.resources.label_declined

@Composable
fun ApprovalStatus.getLabel(): String {
    val res = when (this) {
        ApprovalStatus.APPROVED -> Res.string.label_approved
        ApprovalStatus.APPROVAL_PENDING -> Res.string.label_approval_pending
        ApprovalStatus.DECLINED -> Res.string.label_declined
        ApprovalStatus.CANCELED -> Res.string.label_canceled
    }
    return stringResource(res)
}