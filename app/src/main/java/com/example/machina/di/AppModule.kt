package com.example.auth.di


import com.example.machina.data.remote.AuthApi
import com.example.machina.data.repository.AuthRepository
import com.example.machina.view_model.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://yourapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API
    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
    }

    // Repository
    single {
        AuthRepository(get())
    }

    // ViewModel
    viewModel {
        AuthViewModel(get())
    }
}