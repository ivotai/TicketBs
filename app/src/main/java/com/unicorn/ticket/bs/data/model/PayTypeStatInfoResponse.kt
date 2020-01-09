package com.unicorn.ticket.bs.data.model

data class PayTypeStatInfoResponse(
    val payTypeStatInfoList: List<PayTypeStatInfo>
)

data class PayTypeStatInfo(
    val pay_type: Int,
    val pay_type_name: String,
    val total_amount: Double
)