package com.rollinup.apiservice.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

fun  main() = runBlocking{
    val httpClient = HttpClient {
        expectSuccess = true
    }

    val response =httpClient.get("http://127.0.0.1:8089/test") {

    }.body<String>()

    println(response)

}