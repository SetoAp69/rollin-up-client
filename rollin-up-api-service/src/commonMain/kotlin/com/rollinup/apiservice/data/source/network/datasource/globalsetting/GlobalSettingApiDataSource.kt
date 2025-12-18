package com.rollinup.apiservice.data.source.network.datasource.globalsetting

import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.data.source.network.apiservice.GlobalSettingApiService
import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.globalsetting.GetGlobalSettingResponse
import com.rollinup.apiservice.data.source.network.model.response.sse.GeneralSettingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.sse.deserialize
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class GlobalSettingApiDataSource(
    private val httpClient: HttpClient,
    private val sseClient: HttpClient,
) : GlobalSettingApiService {
    private val url = "global-setting"
    override fun listen(): Flow<ApiResponse<GeneralSettingResponse>> = flow {
        sseClient.sse(
            urlString = "/sse",
            deserialize = { typeInfo, jsonString ->
                val json = Json(Json.Default) {
                    ignoreUnknownKeys = true
                }
                val serializer = json.serializersModule.serializer(typeInfo.kotlinType!!)
                json.decodeFromString(serializer, jsonString)!!
            }
        ) {
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

    override suspend fun getGlobalSetting(): ApiResponse<GetGlobalSettingResponse> {
        return try {
            val response = httpClient.get(url).body<GetGlobalSettingResponse>()
            ApiResponse.Success(response, HttpStatusCode.fromValue(response.status))
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun editGlobalSetting(body: EditGlobalSettingBody): ApiResponse<Unit> {
        return try {
            val response = httpClient.patch("$url/edit") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            ApiResponse.Success(Unit, response.status)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }
}
