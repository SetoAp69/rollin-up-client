@file:OptIn(ExperimentalTime::class, ExperimentalTime::class)

package com.rollinup.rollinup.component.utils

import com.rollinup.rollinup.component.password.PasswordErrorType
import kotlin.time.ExperimentalTime

object Utils {
    fun validateEmail(email: String): Boolean {
        val emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()
        return email.matches(emailRegex)
    }

    fun validatePassword(password: String): PasswordErrorType? {
        val hasLower = password.any { it.isLowerCase() }
        val hasUpper = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSymbol = password.any { !it.isLetterOrDigit() }
        val hasMinLength = password.length >= 8

        return when {
            !hasMinLength -> PasswordErrorType.REQUIRE_LENGTH
            !(hasLower && hasUpper) -> PasswordErrorType.REQUIRE_UPPER_LOWER_CASE
            !hasDigit -> PasswordErrorType.REQUIRE_NUMBER
            !hasSymbol -> PasswordErrorType.REQUIRE_SPECIAL_CHAR
            else -> null
        }
    }

}