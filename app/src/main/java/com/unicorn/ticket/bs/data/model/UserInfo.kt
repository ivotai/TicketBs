package com.unicorn.ticket.bs.data.model

import com.chibatching.kotpref.KotprefModel

object UserInfo : KotprefModel() {
    var username by stringPref()
    var password by stringPref()
    var keepPwd by booleanPref()
}