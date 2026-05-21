package com.example.machina.di


import com.example.machina.data.remote.AuthApi
import com.example.machina.data.repository.AuthRepository
import com.example.machina.view_model.auth_viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {


    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
    }
    single {
        AuthRepository(get())
    }

    viewModel {
        AuthViewModel(get(), get())
    }
}
