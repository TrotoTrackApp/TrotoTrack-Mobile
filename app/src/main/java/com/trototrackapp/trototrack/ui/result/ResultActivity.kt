package com.trototrackapp.trototrack.ui.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trototrackapp.trototrack.R
import com.trototrackapp.trototrack.databinding.ActivityAddReportBinding
import com.trototrackapp.trototrack.databinding.ActivityResultBinding
import com.trototrackapp.trototrack.ui.add.AddReportActivity
import com.trototrackapp.trototrack.ui.add.MapsActivity
import com.trototrackapp.trototrack.ui.home.MainActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addReportButton.setOnClickListener {
            startActivity(Intent(this, AddReportActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}