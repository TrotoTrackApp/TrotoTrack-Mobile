package com.trototrackapp.trototrack.ui.detail

import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val reportId = intent.getStringExtra(REPORT_ID)

        binding.voteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Vote Confirmation")
                .setMessage("Are you sure you want to vote on this report?")
                .setPositiveButton("Yes") { dialog, _ ->
                    lifecycleScope.launch {
                        if (reportId != null) {
                            detailReportViewModel.voteReport(reportId).observe(this@DetailReportActivity) { result ->
                                when (result) {
                                    is ResultState.Loading -> {
                                        binding.progressIndicator.visibility = View.VISIBLE
                                    }

                                    is ResultState.Success -> {
                                        binding.progressIndicator.visibility = View.GONE
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
                    }
                }
                is ResultState.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
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
    }
}
