package com.unicorn.ticket.bs.app.di.component

import com.unicorn.ticket.bs.data.api.SimpleApi
import com.unicorn.ticket.bs.app.di.module.ApiModule
import com.unicorn.ticket.bs.app.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ApiModule::class])
interface AppComponent {

    fun api(): SimpleApi

}