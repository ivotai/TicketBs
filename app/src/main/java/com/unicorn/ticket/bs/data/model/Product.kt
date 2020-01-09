package com.unicorn.ticket.bs.data.model

data class Product(
    val cardType: Int,
    val content: String,
    val date: Long,
//    val datePriceList: List<DatePrice>,
    val defaultPrice: Double,
    val description: String,
    val gardenClosed: Int,
    val price: Double,
    val productId: String,
    val requireCard: Int,
    val stype: Int,
    val title: String
) {
    var quantity: Int = 0
}

data class DatePrice(
    val date: Long,
    val gardenClosed: Int,
    val inventory: Int,
    val price: Double,
    val salePrice: Double,
    val settlementPrice: Double
)