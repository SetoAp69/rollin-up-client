package com.rollinup.apiservice.data.source.network.httpclient

import com.rollinup.apiservice.BuildConfig
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.apiservice.utils.JSON_TYPE
import io.github.orioneee.Axer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.SSEBufferPolicy
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

class NetworkClient (
    private val localDataStore: LocalDataStore
){

    fun getSSEClient() = HttpClient {
        expectSuccess = true
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP //TODO:Changes this to HTTPS on productions
                host = BuildConfig.BASE_URL
            }
        }

        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    this.prettyPrint = true
                }
            )
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            this.sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = localDataStore.getToken()
                    val refreshToken = localDataStore.getRefreshToken()
                    BearerTokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                }

                refreshTokens {
                    val refreshToken = oldTokens?.refreshToken ?: ""
                    val body = hashMapOf("refreshToken" to refreshToken)

                    val response = client.post("auth/refresh-token") {
                        headers.remove(HttpHeaders.Authorization)
                        contentType(JSON_TYPE)
                        setBody(body)
                    }.body<HashMap<String, String>>()

                    val newAccessToken = response["accessToken"] ?: ""

                    localDataStore.updateToken(refreshToken)
                    localDataStore.updateRefreshToken(newAccessToken)

                    BearerTokens(
                        accessToken = newAccessToken,
                        refreshToken = refreshToken
                    )
                }
            }
        }

        install(SSE) {
            maxReconnectionAttempts = 4
            reconnectionTime = 2.seconds
            bufferPolicy = SSEBufferPolicy.LastEvents(10)
        }
    }

    fun getHttpClient() = HttpClient {
        expectSuccess = true

        defaultRequest {
            url {
                protocol = URLProtocol.HTTP //TODO:Changes this to HTTPS on productions
                host = BuildConfig.BASE_URL
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 2000
        }

        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    this.prettyPrint = true
                }
            )
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            this.sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = localDataStore.getToken()
                    val refreshToken = localDataStore.getRefreshToken()
                    BearerTokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                }

                refreshTokens {
                    val refreshToken = oldTokens?.refreshToken ?: ""
                    val body = hashMapOf("refreshToken" to refreshToken)

                    val response = client.post("auth/refresh-token") {
                        headers.remove(HttpHeaders.Authorization)
                        contentType(JSON_TYPE)
                        setBody(body)
                    }.body<HashMap<String, String>>()

                    val newAccessToken = response["accessToken"] ?: ""

                    localDataStore.updateToken(refreshToken)
                    localDataStore.updateRefreshToken(newAccessToken)

                    BearerTokens(
                        accessToken = newAccessToken,
                        refreshToken = refreshToken
                    )
                }
            }
        }

        install(Axer.ktorPlugin){
        }
    }
}