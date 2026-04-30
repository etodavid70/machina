package com.example.machina

import android.app.Application
import com.example.machina.di.authModule
import com.example.machina.di.dashboardModule
import com.example.machina.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule,
                    authModule,
                    dashboardModule
                )
            )
        }
    }
}