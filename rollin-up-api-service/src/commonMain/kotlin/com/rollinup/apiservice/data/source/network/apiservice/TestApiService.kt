package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result

interface TestApiService {
    suspend fun get(
        param: String,
    ): Result<String, NetworkError>
}


