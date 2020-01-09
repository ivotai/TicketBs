package com.unicorn.ticket.bs.app.di

import com.unicorn.ticket.bs.app.di.component.AppComponent
import com.unicorn.ticket.bs.app.di.component.DaggerAppComponent

object ComponentHolder {

    val appComponent: AppComponent by lazy { DaggerAppComponent.create() }

}