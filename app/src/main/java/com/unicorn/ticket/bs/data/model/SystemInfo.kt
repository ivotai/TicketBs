package com.unicorn.ticket.bs.data.model

import com.chibatching.kotpref.KotprefModel
import com.unicorn.ticket.bs.app.baseUrl

object SystemInfo : KotprefModel() {
    var autoPrint by booleanPref()
    var address by stringPref(default = baseUrl)
}