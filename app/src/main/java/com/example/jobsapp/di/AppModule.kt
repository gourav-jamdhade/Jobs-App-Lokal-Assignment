package com.example.jobsapp.di

import com.example.jobsapp.data.local.JobDatabase
import com.example.jobsapp.data.network.RetrofitClient
import com.example.jobsapp.data.repository.JobRepository
import com.example.jobsapp.ui.viewmodel.BookmarksViewModel
import com.example.jobsapp.ui.viewmodel.JobDetailsViewModel
import com.example.jobsapp.ui.viewmodel.JobsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// di/AppModule.kt
val viewModelModule = module {
    viewModel { JobsViewModel(get()) }
    viewModel { BookmarksViewModel(get()) }
    viewModel { JobDetailsViewModel(get()) }
}

val databaseModule = module {
    single { JobDatabase.getDatabase(get()) }
    single { get<JobDatabase>().jobDao() }
}

val networkModule = module {
    single { RetrofitClient.instance }
}

val repositoryModule = module {
    single { JobRepository(get(), get()) }
}

val appModule = listOf(
    viewModelModule,
    databaseModule,
    networkModule,
    repositoryModule
)