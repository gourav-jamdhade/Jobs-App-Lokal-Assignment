package com.example.jobsapp.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jobsapp.R
import com.example.jobsapp.data.remote.model.Job
import com.example.jobsapp.data.remote.model.JobDetails
import com.example.jobsapp.ui.components.PulseAnimation
import com.example.jobsapp.ui.viewmodel.JobDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(
    jobId: Int,
    viewModel: JobDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    LaunchedEffect(jobId) {
        viewModel.loadJobDetails(jobId)
    }

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> FullScreenLoading()
                state.isSearchingThroughPages -> SearchingThroughPages(state.currentSearchPage)
                state.error != null -> FullScreenError(state.error!!) {
                    viewModel.loadJobDetails(jobId)
                }

                state.job == null -> FullScreenMessage("Job not found")
                else -> JobDetailsContent(
                    job = state.job!!,
                    onBookmarkClick = { viewModel.toggleBookmark() }
                )
            }
        }
    }
}

@Composable
private fun SearchingThroughPages(currentPage: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PulseAnimation()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Searching through page $currentPage...", style = MaterialTheme.typography.bodyMedium)
        Text("This may take a moment", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun FullScreenLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        PulseAnimation()
    }
}

@Composable
private fun FullScreenError(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun FullScreenMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun JobDetailsContent(job: Job, onBookmarkClick: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header Section
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = job.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector =
                    if (job.isBookmarked) Icons.Default.Bookmark
                    else Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = job.company,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contact Information Section
        ContactInformationSection(job)

        Spacer(modifier = Modifier.height(24.dp))

        // Job Details Section
        JobDetailsSection(job.details)

        Spacer(modifier = Modifier.height(24.dp))

        // Description Section
        DescriptionSection(job.other_details)
    }
}

@Composable
fun ContactInformationSection(job: Job) {
    val context = LocalContext.current

    Column {
        Text(
            text = "Contact Information",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Regular phone call option
        job.phone?.let { phone ->
            ContactItem(
                icon = Icons.Default.Phone,
                text = phone,
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phone")
                    }
                    context.tryStartActivity(intent, "No dialer app found")
                }
            )
        }

        // WhatsApp contact option
        job.contactLink?.let { whatsappNumber ->
            ContactItem(
                icon = Icons.Default.Whatsapp, // Add WhatsApp icon
                text = "Contact via WhatsApp",
                onClick = {
                    val formattedNumber = whatsappNumber
                        .replace("[^0-9+]".toRegex(), "") // Remove all non-numeric chars except +
                        .trim()

                    val uri = Uri.parse("https://wa.me/$formattedNumber")
                    val intent = Intent(Intent.ACTION_VIEW, uri)

                    // Check if WhatsApp is installed
                    if (isPackageInstalled("com.whatsapp", context)) {
                        context.tryStartActivity(intent, "Couldn't open WhatsApp")
                    } else {
                        // Open in browser as fallback
                        val webUri =
                            Uri.parse("https://web.whatsapp.com/send?phone=$formattedNumber")
                        context.tryStartActivity(
                            Intent(Intent.ACTION_VIEW, webUri),
                            "Couldn't open WhatsApp"
                        )
                    }
                }
            )
        }
    }
}

// Helper extension function
fun Context.tryStartActivity(intent: Intent, errorMessage: String) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}

// Helper function to check if app is installed
fun isPackageInstalled(packageName: String, context: Context): Boolean {
    return try {
        context.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

@Composable
private fun ContactItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Blue
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.Blue
            )
        }
    }
}

@Composable
private fun JobDetailsSection(details: JobDetails) {
    Column {
        Text(
            text = "Job Details",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DetailItem(
            icon = Icons.Default.LocationOn,
            text = details.location
        )

        DetailItem(
            icon = painterResource(R.drawable.baseline_currency_rupee_24),
            text = details.salary
        )

        details.jobType?.let { jobType ->
            DetailItem(
                icon = Icons.Default.Work,
                text = jobType
            )
        }

        details.experience?.let { experience ->
            DetailItem(
                icon = Icons.Default.Timeline,
                text = experience
            )
        }

        details.qualification?.let { qualification ->
            DetailItem(
                icon = Icons.Default.School,
                text = qualification
            )
        }
    }
}

@Composable
private fun DetailItem(icon: Any, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (icon) {
            is ImageVector -> Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            is Painter -> Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
private fun DescriptionSection(description: String) {
    Column {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
