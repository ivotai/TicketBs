package com.unicorn.ticket.bs.data.model

data class Ticket(
    val buyTime: Long,
    val ticketCode: String,
    val quantity: Int,
    val stype: Int,
    val ticketId: String,
    val title: String,
    val travelDate: Long,
    val price: Double,
    val sourceType: Int,
    val ticketNo: String,
    // 多加的属性，为了打印
    var isPrinted: Boolean = false
)