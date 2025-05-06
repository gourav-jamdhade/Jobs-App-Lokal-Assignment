package com.example.jobsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class JobResponse(
    @SerializedName("results") val jobs: List<Job>?,
    @SerializedName("page") val currentPage: Int?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("total_results") val totalResults: Int?
)

data class Job(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("company_name") val company: String,
    @SerializedName("primary_details") val details: JobDetails,
    @SerializedName("whatsapp_no") val phone: String?,
    @SerializedName("whatsapp_link") val contactLink: String?,
    @SerializedName("is_bookmarked") var isBookmarked: Boolean,
    @SerializedName("other_details") val other_details: String
)

data class JobDetails(
    @SerializedName("Place") val location: String,
    @SerializedName("Salary") val salary: String,
    @SerializedName("Job_Type") val jobType: String?,  // New field
    @SerializedName("Experience") val experience: String?, // New field
    @SerializedName("Qualification") val qualification: String? // New field
)