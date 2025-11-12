package com.rollinup.apiservice.source.network.apiservice

import com.rollinup.apiservice.model.NetworkError
import com.rollinup.apiservice.model.Result

interface TestApiService {
    suspend fun get(
        param: String,
    ): Result<String, NetworkError>
}


