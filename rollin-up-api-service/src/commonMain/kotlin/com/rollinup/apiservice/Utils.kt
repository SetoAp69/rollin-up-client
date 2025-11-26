package com.rollinup.apiservice

import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import kotlinx.serialization.json.Json

object Utils {
    fun handleApiError(e: Exception): Result.Error<NetworkError> {
        L.w(e) { e.message.toString() }

        return when (e) {
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    inline fun <reified T> List<T>.toJsonString(): String? {
        return if (this.isEmpty()) null else Json.encodeToString(this)
    }

//    fun <T>handleError(code:Int): Result<T, NetworkError>{
//        return when(code){
//
//        }
//    }
}