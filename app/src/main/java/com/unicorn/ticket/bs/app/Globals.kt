package com.unicorn.ticket.bs.app

import com.unicorn.ticket.bs.data.model.LoginResponse

object Globals {

    val session: String get() = loginResponse.session
    val token: String get() = loginResponse.loginToken

    lateinit var loginResponse: LoginResponse

}