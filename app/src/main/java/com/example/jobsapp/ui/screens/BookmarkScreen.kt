package com.example.jobsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.jobsapp.R
import com.example.jobsapp.data.local.entity.BookmarkedJob
import com.example.jobsapp.ui.viewmodel.BookmarksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookmarkScreen(
    viewModel: BookmarksViewModel = koinViewModel(),
    onItemClick: (BookmarkedJob) -> Unit
) {
    val bookmarkedJobs by viewModel.bookmarkedJobs.collectAsState()

    if (bookmarkedJobs.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No bookmarked jobs yet")
        }
    } else {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(bookmarkedJobs) { job ->
                BookmarkedJobCard(job = job, onItemClick = { onItemClick(job) })
            }
        }
    }
}

@Composable
fun BookmarkedJobCard(job: BookmarkedJob, onItemClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = job.company, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = "Location")
                Text(text = job.location, modifier = Modifier.padding(start = 4.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(R.drawable.baseline_attach_money_24),
                    contentDescription = "Salary"
                )
                Text(text = job.salary, modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}
