package com.unicorn.ticket.bs.data.model

import androidx.annotation.DrawableRes
import com.unicorn.ticket.bs.R

enum class PayType(
    val type: Int,
    val text: String,
    @DrawableRes val img: Int,
    val prompt: String,
    @DrawableRes val paymentResId: Int) {
    Cash(
        2,
        "现金",
        R.mipmap.cash,
        "请收取顾客的现金",
        R.mipmap.payment_cash
    ),
    QRCode(
        1,
        "扫码支付",
        R.mipmap.paycode,
        "请扫描顾客的收款码，完成收款",
        R.mipmap.payment_qrcode
    ),
    Bank(
        3,
        "银联支付",
        R.mipmap.cash,
        "请刷卡",
        R.mipmap.payment_card
    );
}

