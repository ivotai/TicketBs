package com.unicorn.ticket.bs.app

import androidx.multidex.MultiDexApplication
import com.chibatching.kotpref.Kotpref
import com.facebook.stetho.Stetho

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        Kotpref.init(this)
        Stetho.initializeWithDefaults(this)
    }

}