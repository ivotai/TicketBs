package com.unicorn.ticket.bs.data.model

data class ModifyPasswordParam(
    val oldPassword: String,
    val newPassword: String
)