package com.example.machina.di

import com.example.machina.utils.TokenManager
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val PUBLIC_RETROFIT = "publicRetrofit"
const val AUTHENTICATED_RETROFIT = "authenticatedRetrofit"

private const val BASE_URL = "https://etotronics.pythonanywhere.com/"
private const val PUBLIC_CLIENT = "publicClient"
private const val AUTHENTICATED_CLIENT = "authenticatedClient"

val networkModule = module {

    single {
        TokenManager(androidContext())
    }

    single {
        AuthInterceptor(get())
    }

    single(named(PUBLIC_CLIENT)) {
        createOkHttpClient()
    }

    single(named(AUTHENTICATED_CLIENT)) {
        createOkHttpClient(get<AuthInterceptor>())
    }

    single {
        get<OkHttpClient>(named(AUTHENTICATED_CLIENT))
    }

    single(named(AUTHENTICATED_RETROFIT)) {
        createRetrofit(get(named(AUTHENTICATED_CLIENT)))
    }

    single(named(PUBLIC_RETROFIT)) {
        createRetrofit(get(named(PUBLIC_CLIENT)))
    }

    single {
        get<Retrofit>(named(AUTHENTICATED_RETROFIT))
    }
}

private fun createOkHttpClient(authInterceptor: AuthInterceptor? = null): OkHttpClient {
    return OkHttpClient.Builder()
        .apply {
            authInterceptor?.let { addInterceptor(it) }
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}

private fun createRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
