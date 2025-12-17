package com.rollinup.rollinup.screen.main.screen.globalsetting.model

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.dataToJsonString
import com.multiplatform.webview.jsbridge.processParams
import com.multiplatform.webview.web.WebViewNavigator

class UpdateMapMessageHandler(
    var onUpdateMap: (UpdateMapParams) -> Unit,
) : IJsMessageHandler {
    override fun methodName(): String {
        return "updateMapKmpBridge"
    }

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        val param = processParams<UpdateMapParams>(message)
        val data = UpdateMapParams(
            long = param.long,
            lat = param.lat,
            rad = param.rad
        )
        onUpdateMap(param)
        callback(dataToJsonString(data))
    }
}

class SubmitMapDataHandler(val onSubmitMapData: (UpdateMapParams) -> Unit) : IJsMessageHandler {
    override fun methodName(): String {
        return "submitMapDataKmpBridge"
    }

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        val param = processParams<UpdateMapParams>(message)
        onSubmitMapData(param)
        callback(dataToJsonString(param))
    }
}





