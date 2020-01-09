package com.unicorn.ticket.bs.data.model

data class TakeTicketParam(
    val orderId: Long,
    val printType: Int = 1  //  1-全部 2-未打印票
)