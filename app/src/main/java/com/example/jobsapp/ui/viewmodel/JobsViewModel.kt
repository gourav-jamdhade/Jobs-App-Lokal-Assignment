package com.example.jobsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsapp.data.remote.model.Job
import com.example.jobsapp.data.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JobsViewModel(private val repository: JobRepository) : ViewModel() {

    private var _jobList = MutableStateFlow<List<Job>>(emptyList())
    val jobList: StateFlow<List<Job>> = _jobList

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var currentPage = 1
    private var hasMorePages = true

    init {
        loadInitialJobs()
    }

    fun loadInitialJobs() {
        currentPage = 1
        hasMorePages = true
        loadJobs(currentPage)
    }

    fun loadMoreJobs() {
        if (!isLoading.value && !isLoadingMore.value && hasMorePages) {
            loadJobs(currentPage + 1)
        }
    }

    private fun loadJobs(page: Int) {
        viewModelScope.launch {
            if (page == 1) {
                _isLoading.value = true
            } else {
                _isLoadingMore.value = true
            }

            try {
                val response = repository.getJobs(page)
                if (page == 1) {
                    _jobList.value = response.jobs ?: emptyList()
                } else {
                    _jobList.value = _jobList.value + (response.jobs ?: emptyList())
                }

                // Check if there are more pages (you might need to adjust this based on your API response)
                hasMorePages = response.jobs?.isNotEmpty() == true
                if (hasMorePages) {
                    currentPage = page
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
                _isLoadingMore.value = false
            }
        }
    }

    fun toggleBookmark(job: Job) {
        viewModelScope.launch {
            if (repository.isJobBookmarked(job.id)) {
                repository.removeBookmark(job)
            } else {
                repository.bookmarkJob(job)
            }

            // Update the bookmarked status locally without re-fetching
            _jobList.update { jobs ->
                jobs.map {
                    if (it.id == job.id) it.copy(isBookmarked = !it.isBookmarked)
                    else it
                }
            }
        }
    }
}