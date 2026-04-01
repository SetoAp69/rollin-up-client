package com.rollinup.rollinup.component.model

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.common.Role
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_admin
import rollin_up.composeapp.generated.resources.label_student
import rollin_up.composeapp.generated.resources.label_teacher
import rollin_up.composeapp.generated.resources.label_unknown

@Composable
fun Role.getLabel(): String {
    val res = when (this) {
        Role.ADMIN -> Res.string.label_admin
        Role.STUDENT -> Res.string.label_student
        Role.TEACHER -> Res.string.label_teacher
        Role.UNKNOWN -> Res.string.label_unknown
    }
    return stringResource(res)
}