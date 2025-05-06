package com.example.jobsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jobsapp.data.local.entity.BookmarkedJob
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkJob(job: BookmarkedJob)

    @Delete
    suspend fun removeBookmark(job:BookmarkedJob)

    @Query("SELECT * FROM bookmarked_jobs ORDER BY saved_at DESC")
    fun getBookmarkedJobs(): Flow<List<BookmarkedJob>>

    @Query("SELECT EXISTS(SELECT * FROM bookmarked_jobs WHERE id = :jobId)")
    suspend fun isJobBookmarked(jobId: Int): Boolean

    @Query("SELECT * FROM bookmarked_jobs WHERE id = :jobId")
    suspend fun getBookmarkedJobById(jobId: Int): BookmarkedJob?
}