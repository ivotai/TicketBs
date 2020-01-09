package com.unicorn.ticket.bs.data.model

data class QueryPayStatusResponse(
    val orderId: Long,
    val payId: Long,
    val payStatus: Int,
    val orderStatus: Int,
    val orderStatusText: String
)