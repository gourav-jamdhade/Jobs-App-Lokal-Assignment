package com.example.jobsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsapp.data.local.entity.BookmarkedJob
import com.example.jobsapp.data.remote.model.Job
import com.example.jobsapp.data.remote.model.JobDetails
import com.example.jobsapp.data.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JobDetailsViewModel(
    private val repository: JobRepository
) : ViewModel() {

    private val _state = MutableStateFlow(JobDetailsState())
    val state: StateFlow<JobDetailsState> = _state

    private var searchJob: Job? = null

    fun loadJobDetails(jobId: Int) {
        // Don't reload if we're already showing this job
        if (_state.value.job?.id == jobId) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null,
                isSearchingThroughPages = true
            )

            try {
                val job = repository.findJobById(jobId)

                _state.value = _state.value.copy(
                    job = job,
                    isLoading = false,
                    isSearchingThroughPages = false,
                    error = if (job == null) "Job not found" else null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to load job details",
                    isLoading = false,
                    isSearchingThroughPages = false
                )
            }
        }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            _state.value.job?.let { currentJob ->
                val newBookmarkState = !currentJob.isBookmarked

                try {
                    if (newBookmarkState) {
                        repository.bookmarkJob(currentJob)
                    } else {
                        repository.removeBookmark(currentJob)
                    }

                    _state.value = _state.value.copy(
                        job = currentJob.copy(isBookmarked = newBookmarkState)
                    )
                } catch (e: Exception) {
                    _state.value = _state.value.copy(
                        error = "Failed to update bookmark: ${e.message}"
                    )
                }
            }
        }
    }
}

data class JobDetailsState(
    val job: Job? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSearchingThroughPages: Boolean = false,
    val currentSearchPage: Int = 1
)

// Extension function to convert BookmarkedJob to Job
fun BookmarkedJob.toJob(): Job = Job(
    id = this.id,
    title = this.title,
    company = this.company,
    details = JobDetails(
        location = this.location,
        salary = this.salary,
        jobType = null,
        experience = null,
        qualification = null
    ),
    phone = this.phone,
    contactLink = null,
    isBookmarked = true,
    other_details = ""
)