package com.trototrackapp.trototrack.ui.detail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.data.local.UserPreference
import com.trototrackapp.trototrack.databinding.ActivityDetailReportBinding
import com.trototrackapp.trototrack.ui.viewmodel.DetailReportViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import com.trototrackapp.trototrack.util.convertIso8601ToDate
import kotlinx.coroutines.launch

class DetailReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailReportBinding
    private val detailReportViewModel: DetailReportViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var userPreference: UserPreference
    private lateinit var sharedPreferences: SharedPreferences
    private var hasVoted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userPreference = UserPreference.getInstance(this)
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val reportId = intent.getStringExtra(REPORT_ID)

        reportId?.let { nonNullReportId ->
            hasVoted = sharedPreferences.getBoolean("$HAS_VOTED_KEY$nonNullReportId", false)
        }

        binding.voteButton.setOnClickListener {
            if (!hasVoted) {
                AlertDialog.Builder(this)
                    .setTitle("Vote Confirmation")
                    .setMessage("Are you sure you want to vote on this report?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        lifecycleScope.launch {
                            val reportId = intent.getStringExtra(REPORT_ID)
                            if (reportId != null) {
                                detailReportViewModel.voteReport(reportId).observe(this@DetailReportActivity) { result ->
                                    when (result) {
                                        is ResultState.Loading -> {
                                            binding.progressIndicator.visibility = View.VISIBLE
                                        }

                                        is ResultState.Success -> {
                                            binding.progressIndicator.visibility = View.GONE
                                            hasVoted = true
                                            // Simpan status hasVoted ke SharedPreferences
                                            reportId.let { nonNullReportId ->
                                                sharedPreferences.edit().putBoolean("$HAS_VOTED_KEY$nonNullReportId", true).apply()
                                            }
                                            updateButtonState()
                                            Toast.makeText(this@DetailReportActivity, "Vote success", Toast.LENGTH_SHORT).show()
                                        }

                                        is ResultState.Error -> {
                                            binding.progressIndicator.visibility = View.GONE
                                            Toast.makeText(this@DetailReportActivity, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                Toast.makeText(this,"You have voted on this report", Toast.LENGTH_SHORT).show()
            }
        }

        reportId?.let { nonNullReportId ->
            setupObserver(nonNullReportId)
        } ?: run {
            Toast.makeText(this, "Report ID is missing", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupObserver(reportId: String) {
        detailReportViewModel.getReportDetail(reportId).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    val reportDetail = result.data.data
                    if (reportDetail != null) {
                        Glide.with(binding.root)
                            .load(reportDetail.image)
                            .into(binding.reportImage)
                        binding.reportStatus.text = reportDetail.status
                        binding.reportDate.text = convertIso8601ToDate(reportDetail.createdAt ?: "")
                        binding.reportVote.text = reportDetail.like.toString()
                        binding.reportLocation.text = reportDetail.location
                        binding.reportDescription.text = reportDetail.description

                        binding.buttonCheckLocation.setOnClickListener {
                            val latitude = reportDetail.latitude
                            val longitude = reportDetail.longitude

                            if (latitude != null && longitude != null) {
                                val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")

                                mapIntent.putExtra("marker", "true")
                                mapIntent.putExtra("zoom", 15)

                                if (mapIntent.resolveActivity(packageManager) != null) {
                                    startActivity(mapIntent)
                                } else {
                                    Toast.makeText(this, "Google Maps app not found", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this, "Latitude or longitude is missing", Toast.LENGTH_SHORT).show()
                            }
                        }

                        updateButtonState()
                    }
                }
                is ResultState.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateButtonState() {
        if (hasVoted) {
            binding.voteButton.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
            binding.voteButton.isEnabled = false
        } else {
            binding.voteButton.backgroundTintList = null
            binding.voteButton.isEnabled = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val REPORT_ID = "report_id"
        private const val PREF_NAME = "MyPrefs"
        private const val HAS_VOTED_KEY = "hasVoted"
    }
}
