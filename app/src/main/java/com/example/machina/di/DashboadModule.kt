package com.example.machina.di

import com.example.machina.data.local.LocalInstanceStore
import com.example.machina.data.remote.DashboardApi
import com.example.machina.data.repository.DashboardRepository
import com.example.machina.data.repository.SshConnectionRepository
import com.example.machina.view_model.dashboard_viewmodel.CreateVmViewModel
import com.example.machina.view_model.dashboard_viewmodel.DashboardViewModel
import com.example.machina.view_model.dashboard_viewmodel.DeviceInfoViewModel
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val dashboardModule = module {


    single<DashboardApi> {
        get<Retrofit>().create(DashboardApi::class.java)
    }

    single {
        DashboardRepository(get())
    }

    single {
        LocalInstanceStore(androidContext())
    }
    viewModel {
        DashboardViewModel(get(), get(), get())
    }

    viewModel {
        CreateVmViewModel(get())
    }

    viewModel {
        DeviceInfoViewModel()
    }

    single {
        SshConnectionRepository()
    }

    viewModel {
        SshConnectionViewModel(get())
    }
}
