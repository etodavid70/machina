package com.example.machina.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("http://192.168.180.219:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}