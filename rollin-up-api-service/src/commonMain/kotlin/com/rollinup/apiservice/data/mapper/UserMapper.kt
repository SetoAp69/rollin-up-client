package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserByIdResponse
import com.rollinup.apiservice.data.source.network.model.response.user.GetUserListResponse

class UserMapper {
    fun mapGetUserList(data: List<GetUserListResponse.Data.UserData>): List<UserEntity> {
        return data.map {
            UserEntity(
                id = it.id,
                userName = it.userName,
                classX = it.classX ?: "",
                email = it.email,
                fullName = it.firstName + " " + it.lastName,
                studentId = it.studentId ?: "",
                address = it.address,
                gender = Gender.fromValue(it.gender),
            )
        }
    }

    fun mapGetUserById(data: GetUserByIdResponse.Data): UserDetailEntity{
        return UserDetailEntity(
            id = data.id,
            userName = data.username,
            firstName = data.firsName,
            lastName = data.lastName,
            classX = data.classX?.let{
                UserDetailEntity.Data(
                    id = it.id,
                    name = it.name,
                    key = it.key
                )
            },
            email = data.email,
            fullName = data.firsName +" "+data.lastName,
            studentId = data.studentId,
            address = data.address,
            gender = Gender.fromValue(data.gender),
            phoneNumber = data.phoneNumber?:"",
            birthDay = data.birthday,
            role = data.role
        )
    }
}