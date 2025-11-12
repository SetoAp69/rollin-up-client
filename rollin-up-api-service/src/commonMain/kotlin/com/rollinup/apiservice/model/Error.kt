package com.rollinup.apiservice.model

interface Error

enum class NetworkError : Error {
    REQUEST_TIMEOUT,
    EMPTY_TOKEN,
    UNAUTHORIZED,
    CONFLICT,
    TOO_MANY_REQUEST,
    NO_INTERNET,
    PAYLOAD_TOO_LARGE,
    INTERNAL_SERVER_ERROR,
    BAD_GATEWAY,
    SERIALIZATION,
    RESPONSE_ERROR,
    UNKNOWN;
}