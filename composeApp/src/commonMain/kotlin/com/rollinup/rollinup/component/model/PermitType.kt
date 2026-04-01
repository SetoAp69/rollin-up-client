package com.rollinup.rollinup.component.model

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.permit.PermitType
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_absent
import rollin_up.composeapp.generated.resources.label_dispensation

@Composable
fun PermitType.getLabel(): String {
    val res = when (this) {
        PermitType.DISPENSATION -> Res.string.label_dispensation
        PermitType.ABSENCE -> Res.string.label_absent
    }
    return stringResource(res)
}