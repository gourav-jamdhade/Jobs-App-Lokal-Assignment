package com.example.jobsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsapp.data.repository.JobRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BookmarksViewModel(
    private val jobRepository: JobRepository
):ViewModel() {

    val bookmarkedJobs = jobRepository.getBookmarkedJob()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}