package com.example.machina.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("http://10.245.56.219:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}