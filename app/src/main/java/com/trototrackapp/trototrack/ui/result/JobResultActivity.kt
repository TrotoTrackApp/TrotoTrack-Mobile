package com.trototrackapp.trototrack.ui.result

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.trototrackapp.trototrack.R
import com.trototrackapp.trototrack.databinding.ActivityJobResultBinding

class JobResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra("name")
        val nik = intent.getStringExtra("nik")
        val address = intent.getStringExtra("address")
        val phone = intent.getStringExtra("phone")
        val status = intent.getStringExtra("status")
        val fileUrl = intent.getStringExtra("file")

        binding.nameUser.text = name
        binding.nikUser.text = nik
        binding.addressUser.text = address
        binding.phoneUser.text = phone
        binding.status.text = status

        val backgroundColor = when (status) {
            "Pending" -> ContextCompat.getColor(this, R.color.yellow)
            "Rejected" -> ContextCompat.getColor(this, R.color.red)
            "Approved" -> ContextCompat.getColor(this, R.color.green)
            else -> Color.TRANSPARENT
        }

        binding.status.backgroundTintList = ColorStateList.valueOf(backgroundColor)

        binding.downloadButton.setOnClickListener {
            if (fileUrl != null) {
                downloadFile(fileUrl)
            } else {
                Toast.makeText(this, "File URL is not available", Toast.LENGTH_SHORT).show()
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

    private fun downloadFile(fileUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(fileUrl)
        startActivity(intent)
    }
}
