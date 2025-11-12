package com.rollinup.apiservice.source.network.datasource

import com.rollinup.apiservice.model.GeneralSetting
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.sse.deserialize
import io.ktor.client.plugins.sse.sse
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class SSE(private val client: HttpClient) {
    fun connect() = flow {
        client.sse(
            path = "/sse",
            deserialize = { typeInfo, jsonString ->
                val serializer = Json.serializersModule.serializer(typeInfo.kotlinType!!)
                Json.decodeFromString(serializer, jsonString)!!
            }
        ) {
            this.incoming.collect { event ->
                when (event.event) {
                    "" -> {
                        emit(deserialize<GeneralSetting>(event.data))
                    }
                }
            }
        }
    }

    val x = connect()
}

