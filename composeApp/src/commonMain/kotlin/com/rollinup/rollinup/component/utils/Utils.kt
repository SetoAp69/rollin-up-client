package com.rollinup.rollinup.component.utils

object Utils {
    fun validateEmail(email: String): Boolean {
        val emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()
        return email.matches(emailRegex)
    }

    fun validatePassword(password: String): String? {
        val hasLower = password.any { it.isLowerCase() }
        val hasUpper = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSymbol = password.any { !it.isLetterOrDigit() }
        val hasMinLength = password.length >= 8

        return when {
            !hasMinLength -> "Password must be at least 8 characters long"
            !(hasLower && hasUpper) -> "Password must include lower and uppercase characters"
            !hasDigit -> "Password must include a number"
            !hasSymbol -> "Password must include a symbol"
            else -> null
        }
    }
}