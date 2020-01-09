package com.unicorn.ticket.bs.data.model

import java.io.Serializable

data class Order(
    val createdDate: Long,
    val detailInfoList: List<DetailInfo>,
    val orderId: Long,
    val payType: Int,
    val payTypeName: String,
    val quantity: Int,
    val sourceType: Int,
    var status: Int,
    var statusText: String,
    val totalPrice: Double,
    val travelDate: Long
): Serializable

data class DetailInfo(
    val printQuantity: Int,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val ticketInfoList: List<TicketInfo>
)

data class TicketInfo(
    val printStatus: Int,
    val ticketId: String,
    val ticketNo: String,
    val title: String
)