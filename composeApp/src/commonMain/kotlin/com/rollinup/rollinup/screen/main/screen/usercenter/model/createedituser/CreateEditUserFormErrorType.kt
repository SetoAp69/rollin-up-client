package com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_email_invalid_error
import rollin_up.composeapp.generated.resources.msg_first_name_empty_error
import rollin_up.composeapp.generated.resources.msg_first_name_max_error
import rollin_up.composeapp.generated.resources.msg_last_name_empty_error
import rollin_up.composeapp.generated.resources.msg_email_empty_error
import rollin_up.composeapp.generated.resources.msg_student_id_empty_error
import rollin_up.composeapp.generated.resources.msg_username_empty_error
import rollin_up.composeapp.generated.resources.msg_username_exist_error
import rollin_up.composeapp.generated.resources.msg_username_invalid_error
import rollin_up.composeapp.generated.resources.msg_email_exist_error
import rollin_up.composeapp.generated.resources.msg_last_name_max_error
import rollin_up.composeapp.generated.resources.msg_student_id_contain_space_error

enum class CreateEditUserFormErrorType {
    EMAIL_EMPTY,
    EMAIL_INVALID,
    EMAIL_EXIST,
    FIRST_NAME_EMPTY,
    FIRST_NAME_MAX,
    LAST_NAME_EMPTY,
    LAST_NAME_MAX,
    USERNAME_EMPTY,
    USERNAME_EXIST,
    USERNAME_INVALID,
    STUDENT_ID_EMPTY,
    STUDENT_ID_INVALID
    ;

    @Composable
    fun getErrorMessage(): String {
        return when (this) {
            USERNAME_EXIST->stringResource(Res.string.msg_username_exist_error)
            USERNAME_EMPTY->stringResource(Res.string.msg_username_empty_error)
            USERNAME_INVALID->stringResource(Res.string.msg_username_invalid_error)
            STUDENT_ID_INVALID->stringResource(Res.string.msg_student_id_contain_space_error)
            STUDENT_ID_EMPTY->stringResource(Res.string.msg_student_id_empty_error)
            EMAIL_EMPTY -> stringResource(Res.string.msg_email_empty_error)
            EMAIL_EXIST -> stringResource(Res.string.msg_email_exist_error)
            EMAIL_INVALID -> stringResource(Res.string.msg_email_invalid_error)
            FIRST_NAME_EMPTY -> stringResource(Res.string.msg_first_name_empty_error)
            LAST_NAME_EMPTY -> stringResource(Res.string.msg_last_name_empty_error)
            FIRST_NAME_MAX -> stringResource(Res.string.msg_first_name_max_error)
            LAST_NAME_MAX -> stringResource(Res.string.msg_last_name_max_error)
        }
    }
}