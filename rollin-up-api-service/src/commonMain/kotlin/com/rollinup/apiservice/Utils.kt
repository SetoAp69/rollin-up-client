package com.rollinup.apiservice

import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.model.NetworkError
import com.rollinup.apiservice.model.Result

object Utils {
    fun handleApiError(e: Exception): Result.Error<NetworkError> {
        L.w (e) { e.message.toString() }

        return when (e) {
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

//    fun <T>handleError(code:Int): Result<T, NetworkError>{
//        return when(code){
//
//        }
//    }
}