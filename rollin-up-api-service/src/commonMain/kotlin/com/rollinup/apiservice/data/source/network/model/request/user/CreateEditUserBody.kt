package com.rollinup.apiservice.data.source.network.model.request.user

data class CreateEditUserBody(
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val role: String? = null,
    val address: String? = null,
    val studentId: String? = null,
    val classX: String? = null,
    val phoneNumber: String? = null,
    val gender: String? = null,
    val birthDay: Long? = null,
) {
    fun toHashMap(): HashMap<String, String> {
        val hashMap = hashMapOf<String, String>()

        username?.let {
            hashMap["username"] = it
        }
        firstName?.let {
            hashMap["firstname"] = it
        }
        lastName?.let {
            hashMap["lastname"] = it
        }
        email?.let {
            hashMap["email"] = it
        }
        studentId?.ifBlank { null }?.let {
            hashMap["studentId"] = it
        }
        role?.let {
            hashMap["role"] = it
        }
        address?.let {
            hashMap["address"] = it
        }
        classX?.ifBlank { null }?.let {
            hashMap["class"] = it
        }
        phoneNumber?.let {
            hashMap["phoneNumber"] = it
        }
        gender?.let {
            hashMap["gender"] = it
        }
        birthDay?.let {
            hashMap["birthday"] = it.toString()
        }
        return hashMap
    }
}
