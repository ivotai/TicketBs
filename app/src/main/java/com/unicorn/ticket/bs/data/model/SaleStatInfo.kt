package com.unicorn.ticket.bs.data.model

data class SaleStatInfo(
    val ticketPayTypeStatInfoList: List<PayTypeStatInfo>,
    val ticketSaleStatInfoList: List<TicketSaleStatInfo>
)



data class TicketSaleStatInfo(
    val buy_price: Double,
    val product_id: String,
    val product_name: String,
    val quantity: String,
    val total_amount: Double
)