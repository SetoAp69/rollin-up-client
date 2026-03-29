package com.rollinup.rollinup.component.model

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.common.Gender
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_female
import rollin_up.composeapp.generated.resources.label_male

@Composable
fun Gender.getLabel(): String {
    val res = when (this) {
        Gender.MALE -> Res.string.label_male
        Gender.FEMALE -> Res.string.label_female
    }
    return stringResource(res)
}