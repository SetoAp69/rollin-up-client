package com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_email_empty_error
import rollin_up.composeapp.generated.resources.msg_email_exist_error
import rollin_up.composeapp.generated.resources.msg_email_invalid_error
import rollin_up.composeapp.generated.resources.msg_full_name_empty_error
import rollin_up.composeapp.generated.resources.msg_full_name_max_error
import rollin_up.composeapp.generated.resources.msg_selector_field_incomplete_error
import rollin_up.composeapp.generated.resources.msg_student_id_contain_space_error
import rollin_up.composeapp.generated.resources.msg_student_id_empty_error
import rollin_up.composeapp.generated.resources.msg_username_empty_error
import rollin_up.composeapp.generated.resources.msg_username_exist_error
import rollin_up.composeapp.generated.resources.msg_username_invalid_error

enum class CreateEditUserFormErrorType(
    val message: StringResource,
) {
    EMAIL_EMPTY(Res.string.msg_email_empty_error),
    EMAIL_INVALID(Res.string.msg_email_invalid_error),
    EMAIL_EXIST(Res.string.msg_email_exist_error),
    FULL_NAME_EMPTY(Res.string.msg_full_name_empty_error),
    FULL_NAME_MAX(Res.string.msg_full_name_max_error),
    USERNAME_EMPTY(Res.string.msg_username_empty_error),
    USERNAME_EXIST(Res.string.msg_username_exist_error),
    USERNAME_INVALID(Res.string.msg_username_invalid_error),
    STUDENT_ID_EMPTY(Res.string.msg_student_id_empty_error),
    STUDENT_ID_INVALID(Res.string.msg_student_id_contain_space_error),
    FIELD_INCOMPLETE(Res.string.msg_selector_field_incomplete_error)
    ;

    @Composable
    fun getErrorMessage(): String {
        return stringResource(this.message)
    }
}