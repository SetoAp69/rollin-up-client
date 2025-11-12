package com.rollinup.apiservice.data.source.network.model.request.user

data class CreateEditUserBody(
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val role: String? = null,
    val address: String? = null,
    val classX: String? = null,
    val phoneNumber: String? = null,
    val gender: String? = null,
) {
    fun toHashMap(): HashMap<String, String> {

        val hashMap = hashMapOf<String, String>()

        username?.let {
            hashMap["username"] = it
        }
        firstName?.let {
            hashMap["firstName"] = it
        }
        lastName?.let {
            hashMap["lastName"] = it
        }
        email?.let {
            hashMap["email"] = it
        }
        password?.let {
            hashMap["password"] = it
        }
        role?.let {
            hashMap["role"] = it
        }
        address?.let {
            hashMap["address"] = it
        }
        classX?.let {
            hashMap["class"] = it
        }
        phoneNumber?.let {
            hashMap["phoneNumber"] = it
        }
        gender?.let {
            hashMap["gender"] = it
        }
        return hashMap
    }
}
