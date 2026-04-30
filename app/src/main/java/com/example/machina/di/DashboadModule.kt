package com.example.machina.di

import com.example.machina.data.remote.DashBoardApi
import com.example.machina.data.repository.AuthRepository
import com.example.machina.data.repository.DashBoardRepository
import com.example.machina.view_model.auth_viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val dashboardModule = module {


    single<DashBoardApi> {
        get<Retrofit>().create(DashBoardApi::class.java)
    }
    single {
        DashBoardRepository(get())
    }

    viewModel {
        AuthViewModel(get())
    }
}