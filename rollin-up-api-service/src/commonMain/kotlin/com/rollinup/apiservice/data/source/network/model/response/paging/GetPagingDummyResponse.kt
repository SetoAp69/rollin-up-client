package com.rollinup.apiservice.data.source.network.model.response.paging

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GetPagingDummyResponse(
    @SerialName("products")
    val data: List<Data> = emptyList(),
    @SerialName("total")
    val total: Long = 0L,
) {
    @Serializable
    data class Data(
        @SerialName("id")
        val id: Long = 0L,
        @SerialName("title")
        val title: String = "",
        @SerialName("price")
        val price: Double = 0.0,
    )
}
