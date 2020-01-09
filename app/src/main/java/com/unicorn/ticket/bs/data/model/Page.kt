package com.unicorn.ticket.bs.data.model

data class Page<T>(
    val content: List<T>,
    val totalElements: String
)


