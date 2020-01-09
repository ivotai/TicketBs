package com.unicorn.ticket.bs.data.model

class CreateOrderParam(
    private val detailList: List<Product>,
    val totalPrice: Double,
    val category: Int = 1,  // 1表示标准票
    val stypeNum: Int = detailList.size,  // 选了几种票
    val sourceType: Int = 11 // 窗口售票
)

