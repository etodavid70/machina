package com.example.machina.di


import com.example.machina.data.remote.AuthApi
import com.example.machina.data.remote.AuthenticatedAuthApi
import com.example.machina.data.remote.DeviceApi
import com.example.machina.data.repository.AuthRepository
import com.example.machina.data.repository.DeviceRepository
import com.example.machina.view_model.notification.NotificationSettingsViewModel
import com.example.machina.view_model.auth_viewmodel.AuthViewModel
import com.example.machina.view_model.auth_viewmodel.UserSession
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {


    single<AuthApi> {
        get<Retrofit>(named(PUBLIC_RETROFIT)).create(AuthApi::class.java)
    }

    single<AuthenticatedAuthApi> {
        get<Retrofit>(named(AUTHENTICATED_RETROFIT)).create(AuthenticatedAuthApi::class.java)
    }

    single<DeviceApi> {
        get<Retrofit>(named(AUTHENTICATED_RETROFIT)).create(DeviceApi::class.java)
    }

    single {
        AuthRepository(get(), get())
    }

    single {
        DeviceRepository(get())
    }

    single { UserSession() }

    viewModel {
        AuthViewModel(
            get(), get(), get()
        )
    }

//    viewModel {
//        NotificationSettingsViewModel(get(), androidContext())
//    }

    viewModel {
        NotificationSettingsViewModel(
            deviceRepository = get(),
            context = androidContext(),
            userSession = get()
        )
    }
}
