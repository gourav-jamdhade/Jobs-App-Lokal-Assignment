package com.example.jobsapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_jobs")
data class BookmarkedJob(
    @PrimaryKey val id:Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name= "company") val company: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "salary") val salary: String,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "saved_at") val savedAt: Long = System.currentTimeMillis()
)