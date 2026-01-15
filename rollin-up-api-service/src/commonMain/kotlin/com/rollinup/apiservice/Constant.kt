package com.rollinup.apiservice

object Constant {
    const val APP_DATA_PATH=".rollin-up"
    const val LOCAL_DATA_STORE_PATH = "$APP_DATA_PATH/LocalDataStore/"
    const val LOG_PATH = "$APP_DATA_PATH/log"
    const val LOCAL_DATA_STORE_NAME = "local_data_store.preferences_pb"
    const val ACCESS_TOKEN_KEY = "access_token"
    const val REFRESH_TOKEN_KEY = "refresh_token"
    const val GLOBAL_SETTING_KEY = "general_setting"
    const val UI_MODE_KEY = "ui_mode"

    const val PLAIN_TEXT_TYPE = "text/plain"

    const val HTTP_CLIENT = "HTTPClient"
    const val SSE_CLIENT = "SSEClient"
}