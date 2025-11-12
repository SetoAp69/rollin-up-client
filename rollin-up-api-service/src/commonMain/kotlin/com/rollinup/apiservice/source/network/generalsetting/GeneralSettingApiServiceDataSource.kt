package com.rollinup.apiservice.source.network.generalsetting

import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.source.network.apiservice.GeneralSettingApiService
import com.rollinup.apiservice.source.network.model.response.ApiResponse
import com.rollinup.apiservice.source.network.model.response.sse.GeneralSettingResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.deserialize
import io.ktor.client.plugins.sse.sse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class GeneralSettingApiServiceDataSource(
    private val httpClient: HttpClient,
    private val sseClient: HttpClient,
) : GeneralSettingApiService {
    override fun listen(): Flow<ApiResponse<GeneralSettingResponse>> = flow {
        L.wtf { "GeneralSetting data source listen()" }

        sseClient.sse(
            urlString = "http://192.168.137.1:8089/general-setting/sse",
            deserialize = { typeInfo, jsonString ->
                val json = Json(Json.Default) {
                    ignoreUnknownKeys = true
                }
                val serializer = json.serializersModule.serializer(typeInfo.kotlinType!!)
                json.decodeFromString(serializer, jsonString)!!
            }
        ) {
            L.wtf { "sseClient hit" }
            incoming.collect { event ->
                when (event.event) {
                    "general-setting" -> {
                        L.wtf { "Data source receiving event" }
                        emit(
                            ApiResponse.Success(
                                data = deserialize<GeneralSettingResponse>(event.data)!!,
                                statusCode = HttpStatusCode.OK
                            )
                        )
                    }

                }
            }
        }
    }
}