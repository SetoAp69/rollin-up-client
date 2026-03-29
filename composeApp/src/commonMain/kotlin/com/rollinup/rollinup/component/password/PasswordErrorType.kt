package com.rollinup.rollinup.component.password

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_password_empty_error
import rollin_up.composeapp.generated.resources.msg_password_min_length
import rollin_up.composeapp.generated.resources.msg_password_must_include_lower_and_uppercase
import rollin_up.composeapp.generated.resources.msg_password_not_match
import rollin_up.composeapp.generated.resources.msg_password_number
import rollin_up.composeapp.generated.resources.msg_password_special_character

enum class PasswordErrorType(private val res: StringResource) {
    REQUIRE_LENGTH(Res.string.msg_password_min_length),
    REQUIRE_UPPER_LOWER_CASE(Res.string.msg_password_must_include_lower_and_uppercase),
    REQUIRE_NUMBER(Res.string.msg_password_number),
    REQUIRE_SPECIAL_CHAR(Res.string.msg_password_special_character),
    EMPTY(Res.string.msg_password_empty_error),
    NOT_MATCH(Res.string.msg_password_not_match)
    ;

    @Composable
    fun getMessage(): String {
        return stringResource(res)
    }
}