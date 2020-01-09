package com.unicorn.ticket.bs.app.di.module

import com.unicorn.ticket.bs.data.api.SimpleApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ApiModule {

    @Provides
    fun api(retrofit: Retrofit): SimpleApi = retrofit.create(SimpleApi::class.java)

}