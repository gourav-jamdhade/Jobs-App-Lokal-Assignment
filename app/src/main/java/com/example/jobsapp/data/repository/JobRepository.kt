package com.example.jobsapp.data.repository

import android.util.Log
import com.example.jobsapp.data.local.dao.JobDao
import com.example.jobsapp.data.local.entity.BookmarkedJob
import com.example.jobsapp.data.network.JobApiService
import com.example.jobsapp.data.remote.model.Job
import com.example.jobsapp.data.remote.model.JobResponse
import com.example.jobsapp.ui.viewmodel.toJob
import kotlinx.coroutines.flow.Flow

class JobRepository(private val apiService: JobApiService, private val jobDao: JobDao) {
    suspend fun getJobs(page: Int = 1): JobResponse {
        val response = apiService.getJobs(page)
        Log.d("JobRepository", "Fetched jobs: ${response.jobs?.size}")
        return response
    }


    suspend fun bookmarkJob(job: Job) {
        jobDao.bookmarkJob(
            BookmarkedJob(
                id = job.id,
                title = job.title,
                company = job.company,
                location = job.details.location,
                salary = job.details.salary,
                phone = job.phone
            )
        )
    }

    suspend fun removeBookmark(job: Job) {
        jobDao.removeBookmark(
            BookmarkedJob(
                id = job.id,
                title = job.title,
                company = job.company,
                location = job.details.location,
                salary = job.details.salary,
                phone = job.phone
            )
        )
    }

    fun getBookmarkedJob(): Flow<List<BookmarkedJob>> = jobDao.getBookmarkedJobs()

    suspend fun isJobBookmarked(jobId: Int): Boolean = jobDao.isJobBookmarked(jobId)

    suspend fun getBookmarkedJobById(jobId: Int): BookmarkedJob? {
        return jobDao.getBookmarkedJobById(jobId)
    }

    suspend fun findJobById(jobId: Int, maxPagesToSearch: Int = 5): Job? {
        // First check local database (bookmarked jobs)
        jobDao.getBookmarkedJobById(jobId)?.let {
            return it.toJob().copy(isBookmarked = true)
        }

        // Search through API pages
        var currentPage = 1
        var hasMorePages = true
        var foundJob: Job? = null

        while (hasMorePages && currentPage <= maxPagesToSearch && foundJob == null) {
            try {
                val response = apiService.getJobs(currentPage)
                foundJob = response.jobs?.find { it.id == jobId }

                // Check if we should continue searching
                hasMorePages = response.jobs?.isNotEmpty() == true
                currentPage++
            } catch (e: Exception) {
                // Log error but continue searching
                Log.e("JobRepository", "Error fetching page $currentPage", e)
            }
        }

        // Update bookmark status if found
        foundJob?.let { job ->
            job.isBookmarked = jobDao.isJobBookmarked(jobId)
        }

        return foundJob

    }
}