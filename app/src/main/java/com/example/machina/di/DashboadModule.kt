package com.example.machina.di

import com.example.machina.data.remote.CloudInstanceApi
import com.example.machina.data.repository.CloudInstanceRepository
import com.example.machina.data.repository.SshConnectionRepository
import com.example.machina.view_model.dashboard_viewmodel.CloudInstanceViewModel
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val dashboardModule = module {


    single<CloudInstanceApi> {
        get<Retrofit>().create(CloudInstanceApi::class.java)
    }

    single {
        CloudInstanceRepository(get())
    }

    viewModel {
        CloudInstanceViewModel(get())
    }

    single {
        SshConnectionRepository()
    }

    viewModel {
        SshConnectionViewModel(get())
    }
}
