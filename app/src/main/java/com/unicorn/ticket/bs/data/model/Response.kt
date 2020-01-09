package com.unicorn.ticket.bs.data.model

import com.blankj.utilcode.util.ToastUtils

open class Response<T>(
    val message: String,
    val success: Boolean,
    val data: T
) {
    val failed: Boolean
        get() {
            val failed = !success
            if (failed) ToastUtils.showLong(message)
            return failed
        }
}