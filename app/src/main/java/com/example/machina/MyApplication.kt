package com.example.machina

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.machina.di.authModule
import com.example.machina.di.dashboardModule
import com.example.machina.di.networkModule
import com.google.firebase.Firebase
import com.google.firebase.initialize
import okhttp3.OkHttpClient
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        Firebase.initialize(this)

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

    override fun newImageLoader(): ImageLoader {
        val okHttpClient: OkHttpClient = getKoin().get()
        return ImageLoader.Builder(this)
            .okHttpClient(okHttpClient)
            .crossfade(true)
            .build()
    }
}
