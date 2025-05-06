package com.example.jobsapp.data.network

import com.example.jobsapp.data.remote.model.Job
import com.example.jobsapp.data.remote.model.JobResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JobApiService {
    @GET("/common/jobs")
    suspend fun getJobs(
        @Query("page") page: Int = 1
    ): JobResponse

    @GET("/common/jobs/{id}")
    suspend fun getJobById(
        @Path("id") id: Int
    ): Job
}