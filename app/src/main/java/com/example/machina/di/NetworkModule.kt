package com.example.machina.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://etotronics.pythonanywhere.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}