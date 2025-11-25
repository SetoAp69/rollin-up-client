package com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser

data class CreateEditUserFormData(
    val id: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
    val userName: String? = null,
    val gender: String? = null,
    val birthDay: Long? = null,
    val classId: String? = null,
    val role: String? = null,
    val studentId: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val email: String? = null,

    val userNameError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val studentIdError: String? = null,
    val addressError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,

    //SelectorError
    val genderError: Boolean = false,
    val birthDayError: Boolean = false,
    val classError: Boolean = false,
    val roleError: Boolean = false,
) {
    val selectorData
        get() = listOf(gender, birthDay, classId, role)

    val selectorError
        get() = listOf(genderError, birthDayError, classError, roleError)

    val selectorErrorMsg: String?
        get() = if (selectorError.any()) "Please select all fields" else null

    fun isValid(): Boolean {
        return listOf(
            firstNameError,
            lastNameError,
            studentIdError,
            addressError,
            phoneError,
            emailError,
            selectorErrorMsg
        ).all { it == null }
    }
}
