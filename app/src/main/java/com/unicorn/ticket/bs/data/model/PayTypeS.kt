package com.unicorn.ticket.bs.data.model

data class PayTypeS(
    val payType: PayType,
    var isSelect: Boolean
) {
    companion object {
        val all get() = listOf(
            PayTypeS(PayType.Cash, false),
            PayTypeS(PayType.QRCode, true),
            PayTypeS(PayType.Bank, false)
        )
    }
}