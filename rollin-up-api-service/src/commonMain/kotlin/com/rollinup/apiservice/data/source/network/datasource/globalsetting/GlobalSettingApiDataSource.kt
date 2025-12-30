package com.rollinup.apiservice.data.source.network.datasource.globalsetting

import com.rollinup.apiservice.data.source.network.apiservice.GlobalSettingApiService
import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.globalsetting.GetGlobalSettingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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

/**
 * Implementation of [GlobalSettingApiService].
 * Handles reading, updating, and listening to real-time changes in global application settings.
 *
 * @property httpClient The Ktor client for standard HTTP requests (GET/PATCH).
 * @property sseClient The Ktor client specifically configured for Server-Sent Events (SSE).
 */
class GlobalSettingApiDataSource(
    private val httpClient: HttpClient,
    private val sseClient: HttpClient,
) : GlobalSettingApiService {
    private val url = "/global-setting"

    /**
     * Establishes a Server-Sent Events (SSE) connection to listen for setting updates.
     * Emits an [ApiResponse] whenever a "global-setting-update" event is received.
     *
     * @return A [Flow] that emits [ApiResponse.Success] on update events.
     */
    override fun listen(): Flow<ApiResponse<Unit>> = flow {
        sseClient.sse(
            urlString = "$url/sse",
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
                    "global-setting-update" -> {
                        emit(
                            ApiResponse.Success(
                                data = Unit,
                                statusCode = HttpStatusCode.OK
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * Retrieves the current global settings configuration.
     *
     * @return [ApiResponse] containing [GetGlobalSettingResponse].
     */
    override suspend fun getGlobalSetting(): ApiResponse<GetGlobalSettingResponse> {
        return try {
            val response = httpClient.get(url).body<GetGlobalSettingResponse>()
            ApiResponse.Success(response, HttpStatusCode.fromValue(response.status))
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    /**
     * Updates the global settings configuration.
     *
     * @param body The [EditGlobalSettingBody] containing the new settings values.
     * @return [ApiResponse] containing [Unit] on success.
     */
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