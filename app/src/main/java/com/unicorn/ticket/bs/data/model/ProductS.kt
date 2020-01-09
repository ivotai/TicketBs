package com.unicorn.ticket.bs.data.model

data class ProductS(
    val product: Product,
    val index :Int,
    var isSelected: Boolean = false
)