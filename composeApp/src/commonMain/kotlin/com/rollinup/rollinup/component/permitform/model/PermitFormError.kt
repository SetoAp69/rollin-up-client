package com.rollinup.rollinup.component.permitform.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_duration_error_invalid
import rollin_up.composeapp.generated.resources.msg_duration_error_too_early
import rollin_up.composeapp.generated.resources.msg_duration_error_too_late
import rollin_up.composeapp.generated.resources.msg_file_error_max_size
import rollin_up.composeapp.generated.resources.msg_permit_attachment_error_empty
import rollin_up.composeapp.generated.resources.msg_permit_current_date_missing
import rollin_up.composeapp.generated.resources.msg_permit_note_max_char_120
import rollin_up.composeapp.generated.resources.msg_permit_reason_error_empty

enum class PermitFormErrorType {
    DURATION_EMPTY,
    REASON_EMPTY,
    DURATION_INVALID,
    DURATION_NEED_TO_INCLUDE_CURRENT_DATE,
    ATTACHMENT_EMPTY,
    ATTACHMENT_TOO_LARGE,
    NOTE_TOO_LONG,
    DURATION_TOO_EARLY,
    DURATION_TOO_LATE
    ;


    @Composable
    fun getErrorMessage(): String {
        return when (this) {
            DURATION_EMPTY -> stringResource(Res.string.msg_duration_error_invalid)
            REASON_EMPTY -> stringResource(Res.string.msg_permit_reason_error_empty)
            DURATION_INVALID -> stringResource(Res.string.msg_duration_error_invalid)
            DURATION_NEED_TO_INCLUDE_CURRENT_DATE -> stringResource(Res.string.msg_permit_current_date_missing)
            ATTACHMENT_EMPTY -> stringResource(Res.string.msg_permit_attachment_error_empty)
            ATTACHMENT_TOO_LARGE -> stringResource(Res.string.msg_file_error_max_size)
            NOTE_TOO_LONG -> stringResource(Res.string.msg_permit_note_max_char_120)
            DURATION_TOO_EARLY -> stringResource(Res.string.msg_duration_error_too_early)
            DURATION_TOO_LATE -> stringResource(Res.string.msg_duration_error_too_late)
        }
    }
}