package com.unicorn.ticket.bs.data.model

data class ProductQ(
    val product: Product,
    val index:Int,
    var quantity: Int = 1
)